package com.mg.community.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mg.community.mapper.UserMapper;
import com.mg.community.model.Question;
import com.mg.community.model.User;
import com.mg.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ProfileController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/profile/question/{toptitle}")
    public String myQuestion(@RequestParam(value = "pageNum", required = false, defaultValue="1") int pageNum,
                             @RequestParam(required = false, defaultValue="8") int pageSize,
                             @PathVariable("toptitle") String toptitle,
                             HttpServletRequest request,
                             Model model) {

        //Set top title
        if(toptitle.equals("question")){
            model.addAttribute("toptitle", "【我的问题】---MG-COMMUNITY");
        }

        User user = (User) request.getSession().getAttribute("user");
        //pagehelper分页处理
        PageHelper.startPage(pageNum, pageSize);
        List<Question> questions = questionService.findQuestionByCreator(user.getId());
        PageInfo<Question> pageInfo = new PageInfo<Question>(questions);

        model.addAttribute("questions", questions);
        model.addAttribute("pageInfo", pageInfo);

        return "/question";

    }

    @GetMapping("/profile/reply/{toptitle}")
    public String myFollow(@RequestParam(value = "pageNum", required = false, defaultValue="1") int pageNum,
                             @RequestParam(required = false, defaultValue="8") int pageSize,
                             @PathVariable("toptitle") String toptitle,
                             HttpServletRequest request,
                             Model model) {

        //Set top title
        if(toptitle.equals("reply")){
            model.addAttribute("toptitle", "【最新回复】---MG-COMMUNITY");
        }

        return "/reply";
    }

}
