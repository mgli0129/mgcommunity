package com.mg.community.service.impl;

import com.mg.community.dto.QuestionDTO;
import com.mg.community.mapper.QuestionMapper;
import com.mg.community.model.Question;
import com.mg.community.model.QuestionExample;
import com.mg.community.model.User;
import com.mg.community.service.QuestionService;
import com.mg.community.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("questionService")
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public List<Question> findAll() {
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria();
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
    public List<Question> findQuestionByCreator(int creator) {
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(creator);
        return questionMapper.selectByExample(questionExample);
    }

    @Override
    public QuestionDTO findQuestionById(int id) {
        QuestionDTO questionDTO = new QuestionDTO();
        Question question = questionMapper.selectByPrimaryKey(id);
        //copy object
        BeanUtils.copyProperties(question, questionDTO);
        User user = userService.findById(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    @Override
    public void createOrUpdate(Question question) {
        if(question.getId() == null){
            //Insert
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setCommentCount(0);
            question.setLikeCount(0);
            question.setViewCount(0);
            questionMapper.insert(question);
        }else{
            //Update
            question.setGmtModified(System.currentTimeMillis());
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria().andIdEqualTo(question.getId());
            questionMapper.updateByExampleSelective(question,questionExample);
        }
    }
}
