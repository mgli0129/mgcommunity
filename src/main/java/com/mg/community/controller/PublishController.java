package com.mg.community.controller;

import com.mg.community.cache.TagCache;
import com.mg.community.dto.QuestionDTO;
import com.mg.community.model.Question;
import com.mg.community.model.User;
import com.mg.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.stream.Collectors;

@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Long id,
                       Model model) {
        QuestionDTO question = questionService.findDTOById(id);

        //页面回显字段信息
        model.addAttribute("title", question.getTitle());
        model.addAttribute("content", question.getContent());
        model.addAttribute("tag", question.getTag());
        model.addAttribute("id", id);
        model.addAttribute("selectTags", TagCache.getHotTags());

        return "/publish";
    }

    @GetMapping("/publish")
    public String publish(Model model) {
        model.addAttribute("selectTags", TagCache.getHotTags());
        return "/publish";
    }

    @PostMapping("/publish")
    public String dopublish(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "id", required = false) Long id,
            HttpServletRequest request,
            Model model) {

        //页面回显字段信息
        model.addAttribute("title", title);
        model.addAttribute("content", content);
        model.addAttribute("tag", tag);
        model.addAttribute("selectTags", TagCache.getHotTags());

        if (id != null) {
            model.addAttribute("id", id);
        }

        //校验页面字段
        if (title == null || title.equals("")) {
            model.addAttribute("error", "问题标题不能为空");
            return "/publish";
        }

        if (content == null || content.equals("")) {
            model.addAttribute("error", "问题补充不能为空");
            return "/publish";
        }

        if (tag == null || tag.trim().equals("") || tag.trim().equals(",")) {
            model.addAttribute("error", "标签不合法");
            model.addAttribute("tag", "");
            return "/publish";
        }

        String checkInvalidTag = TagCache.checkInvalid(tag);
        if (!StringUtils.isBlank(checkInvalidTag)) {
            model.addAttribute("error", "标签不合法:" + checkInvalidTag);
            return "/publish";
        }

        //处理tag多余的逗号
        String[] tagSplit = tag.split(",");
        String tagNew = Arrays.stream(tagSplit).map(t -> t.trim())
                .distinct()
                .filter(t -> t != null && t.length() > 0 && t!="")
                .collect(Collectors.joining(","));
        tag = tagNew;

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
