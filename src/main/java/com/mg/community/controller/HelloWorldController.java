package com.mg.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HelloWorldController {

    @GetMapping("/demo")
    public String demo(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model, HttpServletRequest request){
        model.addAttribute("name", name);
        return "demo";
    }

}
