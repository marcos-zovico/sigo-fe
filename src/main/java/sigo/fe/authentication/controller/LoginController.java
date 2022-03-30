package sigo.fe.authentication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/secure/{page}")
    public String secure(@PathVariable String page) {
        return "secure/" + page;
    }
}
