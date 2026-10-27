package com.lsxuan.udilabel.infrastructure.codesymbol;

import com.lsxuan.udilabel.domain.label.CodeType;
import com.lsxuan.udilabel.domain.label.LabelTemplate;

public interface CodeSymbolImageGenerator {

    byte[] generate(LabelTemplate labelTemplate, String codeContent);
}
