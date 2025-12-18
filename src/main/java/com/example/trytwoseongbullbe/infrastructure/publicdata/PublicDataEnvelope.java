package com.example.trytwoseongbullbe.infrastructure.publicdata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PublicDataEnvelope<TBody>(
        @JsonProperty("response") Response<TBody> response
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response<TBody>(
            @JsonProperty("header") PublicDataHeader header,
            @JsonProperty("body") TBody body
    ) {
    }
}