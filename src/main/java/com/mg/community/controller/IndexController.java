package com.mg.community.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mg.community.dto.QuestionDTO;
import com.mg.community.model.Question;
import com.mg.community.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(@RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                        @RequestParam(required = false, defaultValue = "15") int pageSize,
                        @RequestParam(value = "search", required = false) String search,
                        Model model) {
        String searchStr =null;
        if(!StringUtils.isBlank(search)){
            String[] splits = search.split(" ");
            searchStr = Arrays.stream(splits).collect(Collectors.joining("|"));
        }

        //pagehelper分页处理
        PageHelper.startPage(pageNum, pageSize);
        List<Question> questions = questionService.findAllBySearch(searchStr);
        PageInfo<Question> pageInfo = new PageInfo<Question>(questions);
        //Add users into Questions
        List<QuestionDTO> questionDTOs = questionService.findAllDTO(questions);

        model.addAttribute("questions", questionDTOs);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("search", search);

        return "index";
    }

}
