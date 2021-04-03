package com.wqy.campusbbs.mapper;

import com.wqy.campusbbs.model.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentExtMapper {
    void incCommentCount(Comment comment);
}
