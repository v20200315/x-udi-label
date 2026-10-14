package com.lsxuan.udilabel.interfaces.api.v1.label.dto;

import com.lsxuan.udilabel.domain.label.CodeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateLabelRequest(@NotNull UUID productId, @NotBlank String name, @NotNull CodeType codeType) {
}
