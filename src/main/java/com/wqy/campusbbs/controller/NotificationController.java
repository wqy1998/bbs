package com.wqy.campusbbs.controller;

import com.wqy.campusbbs.dto.NotificationDTO;
import com.wqy.campusbbs.enums.NotificationTypeEnum;
import com.wqy.campusbbs.mapper.CommentMapper;
import com.wqy.campusbbs.mapper.QuestionMapper;
import com.wqy.campusbbs.model.Comment;
import com.wqy.campusbbs.model.Question;
import com.wqy.campusbbs.model.User;
import com.wqy.campusbbs.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private CommentMapper commentMapper;

    @GetMapping("/notification/{id}")
    public String profile(@PathVariable(name = "id") Long id,
                          HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        NotificationDTO notificationDTO = notificationService.read(id, user);
        if (NotificationTypeEnum.REPLY_COMMENT.getType() == notificationDTO.getType()) {
            Comment comment = commentMapper.selectByPrimaryKey(notificationDTO.getOuterId());
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            return "redirect:/question/" + question.getId();
        } else if (NotificationTypeEnum.REPLY_QUESTION.getType() == notificationDTO.getType()){
            return "redirect:/question/" + notificationDTO.getOuterId();
        } else {
            return "redirect:/";
        }
    }
}
