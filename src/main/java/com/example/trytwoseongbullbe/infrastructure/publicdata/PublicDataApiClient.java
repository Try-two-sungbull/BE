package com.example.trytwoseongbullbe.infrastructure.publicdata;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.time.Duration;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class PublicDataApiClient {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(15);
    private static final int MAX_ERROR_BODY_LENGTH = 500;

    private final WebClient webClient;
    private final PublicDataApiProperties props;
    private final ObjectMapper objectMapper;

    public PublicDataApiClient(PublicDataApiProperties props, ObjectMapper objectMapper) {
        this.props = props;
        this.objectMapper = objectMapper;

        if (!StringUtils.hasText(props.baseUrl())) {
            throw new IllegalStateException("public-data.base-url is required");
        }
        if (!StringUtils.hasText(props.serviceKey())) {
            throw new IllegalStateException("public-data.service-key is required");
        }

        this.webClient = WebClient.builder()
                .baseUrl(props.baseUrl())
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public String getRaw(String path, Map<String, ?> queryParams) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> buildUri(uriBuilder, path, queryParams))
                    .exchangeToMono(this::readBodyOrThrow)
                    .timeout(DEFAULT_TIMEOUT)
                    .block();
        } catch (PublicDataApiException e) {
            throw e;
        } catch (Exception e) {
            throw new PublicDataApiException("PublicData API call failed: path=" + path, e);
        }
    }

    private Mono<String> readBodyOrThrow(ClientResponse res) {
        return res.bodyToMono(String.class)
                .defaultIfEmpty("")
                .flatMap(body -> {
                    if (res.statusCode().is2xxSuccessful()) {
                        return Mono.just(body);
                    }
                    return Mono.error(new PublicDataApiException(
                            "PublicData API HTTP error. status=" + res.statusCode().value()
                                    + ", body=" + truncate(body)
                    ));
                });
    }

    public <TBody> PublicDataEnvelope<TBody> get(
            String path,
            Map<String, ?> queryParams,
            TypeReference<PublicDataEnvelope<TBody>> typeRef
    ) {
        String raw = getRaw(path, queryParams);

        try {
            PublicDataEnvelope<TBody> parsed = objectMapper.readValue(raw, typeRef);

            if (parsed == null || parsed.response() == null || parsed.response().header() == null) {
                throw new PublicDataApiException("Invalid response structure");
            }

            String resultCode = parsed.response().header().resultCode();
            String resultMsg = parsed.response().header().resultMsg();

            if (!isSuccess(resultCode)) {
                throw new PublicDataApiException(
                        "PublicData API error. code=" + resultCode + ", msg=" + resultMsg
                );
            }

            return parsed;
        } catch (PublicDataApiException e) {
            throw e;
        } catch (Exception e) {
            throw new PublicDataApiException("Failed to parse PublicData response as JSON", e);
        }
    }

    private boolean isSuccess(String resultCode) {
        return "00".equals(resultCode) || "200".equals(resultCode);
    }

    private URI buildUri(UriBuilder uriBuilder, String path, Map<String, ?> queryParams) {
        UriBuilder builder = uriBuilder.path(path)
                .queryParam("serviceKey", props.serviceKey());

        boolean hasType = queryParams != null &&
                (queryParams.containsKey("type") || queryParams.containsKey("_type"));

        if (!hasType) {
            builder = builder.queryParam("type", "json");
        }

        if (queryParams != null) {
            for (Map.Entry<String, ?> entry : queryParams.entrySet()) {
                Object value = entry.getValue();
                if (value != null) {
                    builder = builder.queryParam(entry.getKey(), value);
                }
            }
        }

        return builder.build();
    }

    private String truncate(String s) {
        if (s == null) {
            return "";
        }
        return s.length() <= MAX_ERROR_BODY_LENGTH
                ? s
                : s.substring(0, MAX_ERROR_BODY_LENGTH) + "...(truncated)";
    }
}