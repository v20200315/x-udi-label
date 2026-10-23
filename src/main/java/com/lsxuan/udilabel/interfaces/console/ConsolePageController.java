package com.lsxuan.udilabel.interfaces.console;

import com.lsxuan.udilabel.application.label.LabelApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ConsolePageController {

    private final LabelApplicationService labelApplicationService;

    @GetMapping({"/", "/index", "/console"})
    public String indexPage() {

        return "console/index";
    }

    @GetMapping("/udi")
    public String udiPage() {

        return "console/udi";
    }
}
