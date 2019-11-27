package com.mg.community.controller;

import com.mg.community.mapper.QuestionMapper;
import com.mg.community.mapper.UserMapper;
import com.mg.community.model.Question;
import com.mg.community.model.User;
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
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @GetMapping("/publish/{toptitle}")
    public String publish(@PathVariable(value = "toptitle") String toptitle,
                           Model model) {
        //Set top title
        if(toptitle.equals("publish")){
            model.addAttribute("toptitle", "【发布】---MG-COMMUNITYMG");
        }
        return "/publish";
    }

    @PostMapping("/dopublish")
    public String dopublish(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "tag", required = false) String tag,
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
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        User user = (User) request.getSession().getAttribute("user");
        question.setCreator(user.getId());
        questionMapper.insertQuestion(question);

        return "redirect:/";
    }

}
