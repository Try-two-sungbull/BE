package com.example.trytwoseongbullbe.infrastructure.publicdata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PublicDataHeader(
        @JsonProperty("resultCode") String resultCode,
        @JsonProperty("resultMsg") String resultMsg
) {
}