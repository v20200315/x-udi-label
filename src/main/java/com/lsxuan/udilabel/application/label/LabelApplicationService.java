package com.lsxuan.udilabel.application.label;

import com.lsxuan.udilabel.domain.label.Label;
import com.lsxuan.udilabel.domain.label.LabelRepository;
import com.lsxuan.udilabel.infrastructure.security.CurrentUserContext;
import com.lsxuan.udilabel.interfaces.api.v1.label.dto.CreateLabelRequest;
import com.lsxuan.udilabel.interfaces.api.v1.label.dto.LabelResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class LabelApplicationService {

    private final LabelRepository labelRepository;
    private final CurrentUserContext currentUserContext;

    public LabelApplicationService(LabelRepository labelRepository, CurrentUserContext currentUserContext) {

        this.labelRepository = labelRepository;
        this.currentUserContext = currentUserContext;
    }

    @Transactional
    public LabelResponse create(CreateLabelRequest request) {

        UUID userId = currentUserContext.userId();
        Label label = Label.create(request.productId(), request.name(), request.codeType(), userId);
        labelRepository.save(label);

        return LabelResponse.from(label);
    }
}
