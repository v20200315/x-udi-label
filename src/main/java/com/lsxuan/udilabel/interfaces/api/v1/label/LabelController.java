package com.lsxuan.udilabel.interfaces.api.v1.label;

import com.lsxuan.udilabel.application.label.LabelApplicationService;
import com.lsxuan.udilabel.interfaces.api.v1.label.dto.CreateLabelRequest;
import com.lsxuan.udilabel.interfaces.api.v1.label.dto.LabelResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/labels")
public class LabelController {

    private final LabelApplicationService labelApplicationService;

    public LabelController(LabelApplicationService labelApplicationService) {

        this.labelApplicationService = labelApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LabelResponse create(@Valid @RequestBody CreateLabelRequest request) {

        return labelApplicationService.create(request);
    }
}
