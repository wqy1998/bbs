package com.wqy.campusbbs.mapper;

import com.wqy.campusbbs.model.Question;

public interface QuestionExtMapper {
    void incView(Question record);
    void incCommentCount(Question record);
}
