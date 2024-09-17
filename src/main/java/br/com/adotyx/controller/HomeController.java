package br.com.adotyx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping(name = "/.")
    public String principal() {
        return "/home.html";
    }
}
