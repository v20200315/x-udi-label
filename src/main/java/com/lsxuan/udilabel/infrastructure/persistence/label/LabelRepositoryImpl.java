package com.lsxuan.udilabel.infrastructure.persistence.label;

import com.lsxuan.udilabel.domain.label.Label;
import com.lsxuan.udilabel.domain.label.LabelRepository;
import org.springframework.stereotype.Repository;

@Repository
public class LabelRepositoryImpl implements LabelRepository {

    private final LabelMapper mapper;

    public LabelRepositoryImpl(LabelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void save(Label label) {

        mapper.insert(toDO(label));
    }

    private LabelDO toDO(Label label) {

        LabelDO data = new LabelDO();

        data.setId(label.getId());
        data.setProductId(label.getProductId());
        data.setName(label.getName());
        data.setProductName("");
        data.setUdiDi("");
        data.setCodeType(label.getCodeType());
        data.setCodeContent("");
        data.setImageUrl("");
        data.setCreatedAt(label.getCreatedAt());
        data.setCreatedBy(label.getCreatedBy());
        data.setUpdatedAt(label.getUpdatedAt());
        data.setUpdatedBy(label.getUpdatedBy());

        return data;
    }
}
