package com.wqy.campusbbs.mapper;

import com.wqy.campusbbs.dto.QuestionQueryDTO;
import com.wqy.campusbbs.model.Question;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QuestionExtMapper {

    void incView(Question record);

    void incCommentCount(Question record);

    List<Question> selectRelated(Question question);

    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
}
