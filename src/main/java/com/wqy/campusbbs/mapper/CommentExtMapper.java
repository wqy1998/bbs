package com.wqy.campusbbs.mapper;

import com.wqy.campusbbs.model.Comment;

public interface CommentExtMapper {
    void incCommentCount(Comment comment);
}
