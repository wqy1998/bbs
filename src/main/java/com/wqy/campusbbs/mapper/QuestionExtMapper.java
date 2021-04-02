package com.wqy.campusbbs.mapper;

import com.wqy.campusbbs.model.Question;

import java.util.List;

public interface QuestionExtMapper {
    void incView(Question record);
    void incCommentCount(Question record);
    List<Question> selectRelated(Question question);
}
