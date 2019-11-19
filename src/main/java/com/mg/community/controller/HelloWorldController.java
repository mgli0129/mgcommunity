package com.mg.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HelloWorldController {

    @GetMapping("/demo/{name}")
    public String demo(@PathVariable String name, Model model){
        name = name + " ^_^";
        model.addAttribute("name", name);
        return "demo";
    }

}
