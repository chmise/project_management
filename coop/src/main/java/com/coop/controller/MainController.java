package com.coop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//Index, Admin, Chat, Gantt, MindMap 라우팅

@Controller
public class MainController {
    @GetMapping("/")
    public String welcome() {
        return "welcome";
    }
    
    @GetMapping("/index")
    public String index() {
        return "index";
    }
}