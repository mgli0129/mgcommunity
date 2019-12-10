package com.mg.community.service.impl;

import com.mg.community.dto.QuestionDTO;
import com.mg.community.mapper.QuestionExtMapper;
import com.mg.community.mapper.QuestionMapper;
import com.mg.community.model.Question;
import com.mg.community.model.QuestionExample;
import com.mg.community.model.User;
import com.mg.community.service.QuestionService;
import com.mg.community.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service("questionService")
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Override
    public List<Question> findAll() {
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria();
        questionExample.setOrderByClause("gmt_create desc");
        return questionMapper.selectByExample(questionExample);
    }

    @Override
    public List<QuestionDTO> findAllDTO(List<Question> questions) {

        if (questions == null) {
            return null;
        }
        List<QuestionDTO> questionDTOs = new ArrayList<QuestionDTO>();
        for (Question question : questions) {
            QuestionDTO questionDTO = new QuestionDTO();
            //copy object
            BeanUtils.copyProperties(question, questionDTO);
            User user = userService.findById(question.getCreator());
            questionDTO.setUser(user);
            questionDTOs.add(questionDTO);
        }
        return questionDTOs;
    }

    @Override
    public List<Question> findQuestionByCreator(Long creator) {
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(creator);
        return questionMapper.selectByExample(questionExample);
    }

    @Override
    public QuestionDTO findDTOById(Long id) {
        QuestionDTO questionDTO = new QuestionDTO();
        Question question = questionMapper.selectByPrimaryKey(id);
        //copy object
        BeanUtils.copyProperties(question, questionDTO);
        User user = userService.findById(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    @Override
    public Question findById(Long id) {
        return questionMapper.selectByPrimaryKey(id);
    }

    @Override
    public void createOrUpdate(Question question) {
        if(question.getId() == null){
            //Insert
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setCommentCount(0L);
            question.setLikeCount(0L);
            question.setViewCount(0L);
            questionMapper.insert(question);
        }else{
            //Update
            question.setGmtModified(System.currentTimeMillis());
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria().andIdEqualTo(question.getId());
            questionMapper.updateByExampleSelective(question,questionExample);
        }
    }

    @Override
    public void incView(Question question) {
        question.setId(question.getId());
        question.setViewCount(1L);
        questionExtMapper.incView(question);
    }

    @Override
    public void incComment(Question question) {
        question.setId(question.getId());
        question.setCommentCount(1L);
        questionExtMapper.incComment(question);
    }

    @Override
    public List<QuestionDTO> findRelatedByTag(QuestionDTO qdto) {
        if(StringUtils.isBlank(qdto.getTag())){
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(qdto.getTag(),",");
        String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(qdto.getId());
        question.setTag(regexpTag);
        List<Question> questions = questionExtMapper.selectRelatedByTag(question);
        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q,questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());
        return questionDTOS;
    }
}
