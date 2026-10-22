package com.lsxuan.udilabel.interfaces.api.v1.label.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record GenerateLabelPreviewRequest(@NotNull(message = "产品 ID 不能为空") UUID productId,
                                          @NotBlank(message = "标签模板不能为空") String templateCode) {
}