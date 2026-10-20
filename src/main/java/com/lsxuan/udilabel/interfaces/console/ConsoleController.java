package com.lsxuan.udilabel.interfaces.console;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ConsoleController {

    @GetMapping({"/", "/index", "/console"})
    public String index() {

        return "console/index";
    }

    @GetMapping("/product")
    public String product() {

        return "console/product";
    }

    @GetMapping("/udi")
    public String udi() {

        return "console/udi";
    }

    @GetMapping({"/index2"})
    public String index2() {

        return "console/index2";
    }

}
