package com.mg.community.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mg.community.dto.CommentDTO;
import com.mg.community.dto.NotificationDTO;
import com.mg.community.dto.QuestionDTO;
import com.mg.community.enums.CommentTypeEnum;
import com.mg.community.model.Question;
import com.mg.community.model.User;
import com.mg.community.service.CommentService;
import com.mg.community.service.NotificationService;
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
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Long id,
                           Model model) {

        //点击一次Question将增加一个View
        questionService.incView(questionService.findById(id));



        QuestionDTO questionDTO = questionService.findDTOById(id);
        List<CommentDTO> comments = commentService.listByTargetId(id, CommentTypeEnum.QUESTION.getType());

        List<QuestionDTO> relatedQuestions = questionService.findRelatedByTag(questionDTO);

        model.addAttribute("question", questionDTO);
        model.addAttribute("comments", comments);
        model.addAttribute("relatedQuestions", relatedQuestions);
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
                             @RequestParam(value = "search",required = false) String search,
                             HttpServletRequest request,
                             Model model) {

        String searchStr=null;
        if(!StringUtils.isBlank(search)){
            String[] splits = search.split(" ");
            searchStr = Arrays.stream(splits).collect(Collectors.joining("|"));
        }

        User user = (User) request.getSession().getAttribute("user");
        //pagehelper分页处理
        PageHelper.startPage(pageNum, pageSize);
        List<Question> questions = questionService.findQuestionByCreatorOrSearch(user.getId(),searchStr);
        PageInfo<Question> pageInfo = new PageInfo<Question>(questions);

        model.addAttribute("questions", questions);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("search", search);

        return "/question";
    }

    @GetMapping("/question/reply")
    public String yourLatelyReply(@RequestParam(value = "pageNum", required = false, defaultValue="1") int pageNum,
                           @RequestParam(required = false, defaultValue="8") int pageSize,
                           HttpServletRequest request,
                           Model model) {

        User user = (User) request.getSession().getAttribute("user");
        //pagehelper分页处理
        PageHelper.startPage(pageNum, pageSize);
        List<NotificationDTO> notifications = notificationService.findNotificationByReceiver(user.getId());
        PageInfo<NotificationDTO> pageInfo = new PageInfo<NotificationDTO>(notifications);

        model.addAttribute("notifications", notifications);
        model.addAttribute("pageInfo", pageInfo);
        return "/reply";
    }

}
