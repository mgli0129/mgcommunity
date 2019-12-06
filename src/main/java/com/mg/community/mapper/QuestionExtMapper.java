package com.mg.community.mapper;

import com.mg.community.model.Question;

public interface QuestionExtMapper {

    /**
     * 自动增加一次View
     * @param record
     * @return
     */
    int incView(Question record);

    int incComment(Question question);
}
