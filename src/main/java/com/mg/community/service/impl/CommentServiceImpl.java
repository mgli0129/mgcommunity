package com.mg.community.service.impl;

import com.mg.community.dto.CommentDTO;
import com.mg.community.enums.CommentTypeEnum;
import com.mg.community.exception.CustomizeErrorCode;
import com.mg.community.exception.CustomizeException;
import com.mg.community.mapper.CommentExtMapper;
import com.mg.community.mapper.CommentMapper;
import com.mg.community.model.Comment;
import com.mg.community.model.CommentExample;
import com.mg.community.model.Question;
import com.mg.community.model.User;
import com.mg.community.service.CommentService;
import com.mg.community.service.QuestionService;
import com.mg.community.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service("CommentService")
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @Override
    public void createOrUpdate(Comment comment) {
        Comment comm = null;
        Question question = null;

        //校验
        if (comment.getParentId() == null || comment.getParentId() == 0L) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_NOT_FOUND);
        }

        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            //回复comment
            comm = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (comm == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
        } else {
            //回复question
            question = questionService.findById(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }

        //插入数据或修改数据
        if (comment.getId() == null) {
            //Insert
            comment.setGmtCreate(System.currentTimeMillis());
            comment.setGmtModified(System.currentTimeMillis());
            comment.setLikeCount(0L);
            comment.setCommentCount(0L);
            commentMapper.insert(comment);

            //给问题的评论增加1
            if (comment.getType() == CommentTypeEnum.QUESTION.getType()) {
                question.setCommentCount(1L);
                questionService.incComment(question);
            }else{
                //给评论的评论增加1
                Comment parentComment = new Comment();
                parentComment.setId(comment.getParentId());
                parentComment.setCommentCount(1L);
                commentExtMapper.incComment(parentComment);
            }

        } else {
            //Update
            comment.setGmtModified(System.currentTimeMillis());
            CommentExample commentExample = new CommentExample();
            commentExample.createCriteria().andIdEqualTo(comment.getId());
            commentMapper.updateByExampleSelective(comment, commentExample);
        }
    }

    @Override
    public List<CommentDTO> listByTargetId(Long id, Integer type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andParentIdEqualTo(id).andTypeEqualTo(type);
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if (comments.size() == 0) {
            return new ArrayList<>();
        }
        //获取去重的评论人
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList();
        userIds.addAll(commentators);

        //获取评论人并转为map
        List<User> users = userService.listByIds(userIds);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        //转换comment to commentDTO
        List<CommentDTO> commentDTOs = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOs;
    }
}
