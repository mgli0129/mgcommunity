package com.mg.community.service.impl;

import com.mg.community.dto.QuestionDTO;
import com.mg.community.mapper.QuestionMapper;
import com.mg.community.mapper.UserMapper;
import com.mg.community.model.Question;
import com.mg.community.model.User;
import com.mg.community.service.QuestionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("questionService")
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public List<Question> findAll() {
        return questionMapper.selectAll();
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
            User user = userMapper.findUserById(question.getCreator());
            questionDTO.setUser(user);
            questionDTOs.add(questionDTO);
        }
        return questionDTOs;
    }

    @Override
    public List<Question> findQuestionByCreator(int creator) {
        return questionMapper.selectQuestionByCreator(creator);
    }
}
