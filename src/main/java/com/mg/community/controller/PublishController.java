package com.mg.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @GetMapping("/publish")
    public String publish(HttpServletRequest request) {
        return "publish";
    }

}
