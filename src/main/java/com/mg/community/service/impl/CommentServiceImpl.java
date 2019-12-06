package com.mg.community.service.impl;

import com.mg.community.dto.QuestionDTO;
import com.mg.community.enums.CommentTypeEnum;
import com.mg.community.exception.CustomizeErrorCode;
import com.mg.community.exception.CustomizeException;
import com.mg.community.mapper.CommentMapper;
import com.mg.community.model.Comment;
import com.mg.community.model.CommentExample;
import com.mg.community.model.Question;
import com.mg.community.service.CommentService;
import com.mg.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("CommentService")
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionService questionService;

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
            if(comm == null){
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
            commentMapper.insert(comment);

            if (comment.getType() == CommentTypeEnum.QUESTION.getType()) {
                question.setCommentCount(1L);
                questionService.incComment(question);
            }
        } else {
            //Update
            comment.setGmtModified(System.currentTimeMillis());
            CommentExample commentExample = new CommentExample();
            commentExample.createCriteria().andIdEqualTo(comment.getId());
            commentMapper.updateByExampleSelective(comment, commentExample);
        }
    }
}
