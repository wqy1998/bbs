package com.wqy.campusbbs.controller;

import com.wqy.campusbbs.dto.CommentCreateDTO;
import com.wqy.campusbbs.dto.CommentDTO;
import com.wqy.campusbbs.dto.ResultDTO;
import com.wqy.campusbbs.enums.CommentTypeEnum;
import com.wqy.campusbbs.exception.CustomizeErrorCode;
import com.wqy.campusbbs.model.Comment;
import com.wqy.campusbbs.model.User;
import com.wqy.campusbbs.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    //@ResponseBody 把对象自动序列化成Json，发送到前端
    //@RequestBody 接收Json格式
    @ResponseBody
    @PostMapping("/comment")
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NOT_LOGIN);
        }
        if (commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())) {
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setCommentator(user.getId());
        commentService.insert(comment, user);
        return ResultDTO.okOf();
    }

    @ResponseBody
    @GetMapping("/comment/{id}")
    public ResultDTO<List<CommentDTO>> comments(@PathVariable(name = "id") Long id) {
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(commentDTOS);
    }
}
