package com.lsxuan.udilabel.interfaces.console;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/console")
public class ConsoleController {

    @GetMapping
    public String index() {

        return "console/index";
    }

}

