package com.mg.community.service;

import com.mg.community.dto.QuestionDTO;
import com.mg.community.model.Question;
import org.springframework.stereotype.Service;

import java.util.List;

public interface QuestionService {

    /**
     * 供pagehelper使用
     * @return
     */
    List<Question> findAll();

    /**
     * 补全当前页的question结果集
     * @param questions
     * @return
     */
    List<QuestionDTO> findAllDTO(List<Question> questions);

    List<Question> findQuestionByCreator(int creator);
}
