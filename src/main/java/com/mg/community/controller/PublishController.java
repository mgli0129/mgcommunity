package com.mg.community.controller;

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

@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name="id") int id,
                       Model model) {
        QuestionDTO question = questionService.findQuestionById(id);

        //页面回显字段信息
        model.addAttribute("title", question.getTitle());
        model.addAttribute("content", question.getContent());
        model.addAttribute("tag", question.getTag());
        model.addAttribute("id", id);

        return "/publish";
    }

    @GetMapping("/publish")
    public String publish() {
        return "/publish";
    }

    @PostMapping("/publish")
    public String dopublish(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "id", required = false) int id,
            HttpServletRequest request,
            Model model) {

        //页面回显字段信息
        model.addAttribute("title", title);
        model.addAttribute("content", content);
        model.addAttribute("tag", tag);

        //校验页面字段
        if (title == null || title.equals("")) {
            model.addAttribute("error", "问题标题不能为空");
            return "/publish";
        }

        if (content == null || content.equals("")) {
            model.addAttribute("error", "问题补充不能为空");
            return "/publish";
        }

        if (tag == null || tag.equals("")) {
            model.addAttribute("error", "标签不能为空");
            return "/publish";
        }

        //写入数据库
        Question question = new Question();
        question.setTitle(title);
        question.setContent(content);
        question.setTag(tag);
        User user = (User) request.getSession().getAttribute("user");
        question.setCreator(user.getId());
        question.setId(id);
        questionService.createOrUpdate(question);

        return "redirect:/";
    }

}
