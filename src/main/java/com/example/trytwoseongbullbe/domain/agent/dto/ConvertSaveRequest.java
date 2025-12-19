package com.example.trytwoseongbullbe.domain.agent.dto;


import jakarta.validation.constraints.NotBlank;

public record ConvertSaveRequest(
        @NotBlank String filename,
        @NotBlank String html
) {
}