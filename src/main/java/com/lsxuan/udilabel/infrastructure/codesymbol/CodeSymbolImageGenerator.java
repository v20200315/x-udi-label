package com.lsxuan.udilabel.infrastructure.codesymbol;

import com.lsxuan.udilabel.domain.label.CodeType;

public interface CodeSymbolImageGenerator {

    byte[] generate(CodeType codeType, String codeContent);
}
