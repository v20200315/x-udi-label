package com.lsxuan.udilabel.infrastructure.codesymbol;

import com.lsxuan.udilabel.domain.label.CodeType;
import com.lsxuan.udilabel.domain.label.LabelTemplate;
import org.springframework.stereotype.Component;

@Component("anotherCodeSymbolImageGenerator")
public class AnotherCodeSymbolImageGenerator implements CodeSymbolImageGenerator {
    
    @Override
    public byte[] generate(LabelTemplate labelTemplate, String codeContent) {
        return new byte[0];
    }
}
