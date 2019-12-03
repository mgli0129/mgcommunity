package com.mg.community.controller;

import com.mg.community.dto.QuestionDTO;
import com.mg.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Integer id,
                           Model model) {
        QuestionDTO questionDTO = questionService.findQuestionById(id);
        model.addAttribute("question", questionDTO);
        model.addAttribute("toptitle", "【问题】---MG-COMMUNITY");
        return "/questiondetail";
    }

    @PostMapping("/reply/{id}")
    public String reply(@PathVariable("id") Integer id,
                        Model model){
        return null;
    }
}
