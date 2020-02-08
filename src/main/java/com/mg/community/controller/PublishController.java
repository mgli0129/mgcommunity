package com.mg.community.controller;

import com.mg.community.cache.TagCache;
import com.mg.community.dto.QuestionDTO;
import com.mg.community.model.Question;
import com.mg.community.model.User;
import com.mg.community.service.QuestionService;
import com.mg.community.util.RedisUtil;
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

    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Long id,
                       Model model) {

        QuestionDTO questionDTO=null;
        //先从Redis中获取，若不存在，再从数据库中读取
        if(redisUtil.testConnection()) {
            questionDTO = (QuestionDTO) redisUtil.hget(redisUtil.QUESTION, id.toString());
        }

        if(questionDTO == null){
            questionDTO = questionService.findDTOById(id);
            if(redisUtil.testConnection()) {
                redisUtil.hset(redisUtil.QUESTION, id.toString(), questionDTO);
            }
        }

        //页面回显字段信息
        model.addAttribute("title", questionDTO.getTitle());
        model.addAttribute("content", questionDTO.getContent());
        model.addAttribute("tag", questionDTO.getTag());
        model.addAttribute("id", id);
        model.addAttribute("selectTags", TagCache.getHotTags());

        return "publish";
    }

    @GetMapping("/publish")
    public String publish(Model model) {
        model.addAttribute("selectTags", TagCache.getHotTags());
        return "publish";
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
            return "publish";
        }

        if (content == null || content.equals("")) {
            model.addAttribute("error", "问题补充不能为空");
            return "publish";
        }

        if (tag == null || tag.trim().equals("") || tag.trim().equals(",")) {
            model.addAttribute("error", "标签不合法");
            model.addAttribute("tag", "");
            return "publish";
        }

        String checkInvalidTag = TagCache.checkInvalid(tag);
        if (!StringUtils.isBlank(checkInvalidTag)) {
            model.addAttribute("error", "标签不合法:" + checkInvalidTag);
            return "publish";
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

        //更新Redis中question数据
        if(id !=null && redisUtil.testConnection()){
            QuestionDTO questionDTO = questionService.findDTOById(id);
            redisUtil.hset(redisUtil.QUESTION, id.toString(), questionDTO);

            //是否需要更新相关问题，此部分暂不考虑，因为相关问题一天更新一次也是可以的；
        }

        return "redirect:/";
    }

}
