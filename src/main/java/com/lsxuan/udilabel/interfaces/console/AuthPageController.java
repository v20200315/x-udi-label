package com.lsxuan.udilabel.interfaces.console;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthPageController {

    @GetMapping("/logged-out")
    public String loggedOut() {
        return "console/logged-out";
    }
}
