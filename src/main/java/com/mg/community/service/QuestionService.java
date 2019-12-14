package com.mg.community.service;

import com.mg.community.dto.QuestionDTO;
import com.mg.community.model.Comment;
import com.mg.community.model.Question;
import org.springframework.stereotype.Service;

import java.util.List;

public interface QuestionService {

    /**
     * 供pagehelper使用
     * @return
     */
    List<Question> findAllBySearch(String search);

    /**
     * 补全当前页的question结果集
     * @param questions
     * @return
     */
    List<QuestionDTO> findAllDTO(List<Question> questions);

    List<Question> findQuestionByCreatorOrSearch(Long creator, String search);

    QuestionDTO findDTOById(Long id);

    Question findById(Long id);

    void createOrUpdate(Question question);

    void incView(Question question);

    void incComment(Question comment);

    List<QuestionDTO> findRelatedByTag(QuestionDTO questionDTO);
}
