package com.lsxuan.udilabel.infrastructure.codesymbol;

import com.lsxuan.udilabel.domain.label.CodeType;
import org.springframework.stereotype.Component;

@Component("anotherCodeSymbolImageGenerator")
public class AnotherCodeSymbolImageGenerator implements CodeSymbolImageGenerator {
    
    @Override
    public byte[] generate(CodeType codeType, String codeContent) {
        return new byte[0];
    }
}
