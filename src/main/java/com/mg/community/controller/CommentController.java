package com.mg.community.controller;

import com.mg.community.dto.CommentDTO;
import com.mg.community.dto.CommentInputDTO;
import com.mg.community.dto.ResultDTO;
import com.mg.community.enums.CommentTypeEnum;
import com.mg.community.exception.CustomizeErrorCode;
import com.mg.community.model.Comment;
import com.mg.community.model.User;
import com.mg.community.service.CommentService;
import com.mg.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private QuestionService questionService;

    /**
     * 回复问题或者评论
     * @param commentInputDTO
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentInputDTO commentInputDTO,
                       HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        if (commentInputDTO == null || StringUtils.isBlank(commentInputDTO.getContent())) {
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        Comment comment = new Comment();
        comment.setParentId(commentInputDTO.getParentId());
        comment.setContent(commentInputDTO.getContent());
        comment.setType(commentInputDTO.getType());
        comment.setCommentator(user.getId());
        commentService.createOrUpdate(comment, user);
        return ResultDTO.okOf();
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
    public ResultDTO<List<CommentDTO>> comments(@PathVariable("id") Long id,
                                                      Model model) {
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.COMMENT.getType());
        model.addAttribute("comments", commentDTOS);
        model.addAttribute("toptitle", "【问题】---MG-COMMUNITY");
        return ResultDTO.okOf(commentDTOS);
    }
}
