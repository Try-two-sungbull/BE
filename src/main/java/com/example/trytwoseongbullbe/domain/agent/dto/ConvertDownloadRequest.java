package com.example.trytwoseongbullbe.domain.agent.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ConvertDownloadRequest(
        @NotNull Long id,
        @NotBlank @Pattern(regexp = "pdf|docx") String format
) {
}