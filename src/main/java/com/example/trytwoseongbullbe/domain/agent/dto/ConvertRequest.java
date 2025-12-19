package com.example.trytwoseongbullbe.domain.agent.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ConvertRequest(
        @NotBlank String html,
        @NotBlank
        @Pattern(regexp = "pdf|docx|hwp", message = "format must be one of: pdf, docx, hwp")
        String format,
        @NotBlank String filename
) {
}