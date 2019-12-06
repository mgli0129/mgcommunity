package com.mg.community.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mg.community.dto.QuestionDTO;
import com.mg.community.model.Question;
import com.mg.community.model.User;
import com.mg.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Long id,
                           Model model) {

        //点击一次Question将增加一个View
        questionService.incView(questionService.findById(id));

        QuestionDTO questionDTO = questionService.findDTOById(id);
        model.addAttribute("question", questionDTO);
        model.addAttribute("toptitle", "【问题】---MG-COMMUNITY");


        return "/questiondetail";
    }

    @PostMapping("/reply/{id}")
    public String reply(@PathVariable("id") Long id,
                        Model model){
        return null;
    }

    @GetMapping("/question/publish")
    public String myQuestion(@RequestParam(value = "pageNum", required = false, defaultValue="1") int pageNum,
                             @RequestParam(required = false, defaultValue="8") int pageSize,
                             HttpServletRequest request,
                             Model model) {

        User user = (User) request.getSession().getAttribute("user");
        //pagehelper分页处理
        PageHelper.startPage(pageNum, pageSize);
        List<Question> questions = questionService.findQuestionByCreator(user.getId());
        PageInfo<Question> pageInfo = new PageInfo<Question>(questions);

        model.addAttribute("questions", questions);
        model.addAttribute("pageInfo", pageInfo);

        return "/question";
    }

    @GetMapping("/question/reply")
    public String yourLatelyReply(@RequestParam(value = "pageNum", required = false, defaultValue="1") int pageNum,
                           @RequestParam(required = false, defaultValue="8") int pageSize,
                           HttpServletRequest request,
                           Model model) {
        return "/reply";
    }

}
