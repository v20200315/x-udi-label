package com.lsxuan.udilabel.interfaces.api.v1.label;

import com.lsxuan.udilabel.application.label.LabelApplicationService;
import com.lsxuan.udilabel.interfaces.api.v1.label.dto.GenerateLabelPreviewRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/labels")
@RequiredArgsConstructor
public class LabelController {

    private final LabelApplicationService labelApplicationService;

    @PostMapping(value = "/generate", produces = "image/svg+xml")
    public ResponseEntity<byte[]> generate(@Valid @ModelAttribute GenerateLabelPreviewRequest request) {

        //        byte[] svg = labelApplicationService.generate(request.productId(), request.templateCode());
        byte[] svg = null;
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("image/svg+xml;charset=UTF-8")).body(svg);
    }
}