package com.wqy.campusbbs.controller;

import com.wqy.campusbbs.dto.CommentDTO;
import com.wqy.campusbbs.dto.QuestionDTO;
import com.wqy.campusbbs.enums.CommentTypeEnum;
import com.wqy.campusbbs.mapper.CommentMapper;
import com.wqy.campusbbs.mapper.QuestionMapper;
import com.wqy.campusbbs.model.Comment;
import com.wqy.campusbbs.model.CommentExample;
import com.wqy.campusbbs.model.Question;
import com.wqy.campusbbs.service.CommentService;
import com.wqy.campusbbs.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private CommentMapper commentMapper;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id,
                           Model model) {
        QuestionDTO questionDTO = questionService.getById(id);
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        List<CommentDTO> comments = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);
        //累加阅读数
        questionService.incView(id);
        model.addAttribute("question", questionDTO);
        model.addAttribute("comments", comments);
        model.addAttribute("relatedQuestions", relatedQuestions);
        return "question";
    }

    @GetMapping("/question/{id}/delete")
    public String deleteQuestion(@PathVariable(name = "id") Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        //1.查找question的所有评论
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andTypeEqualTo(CommentTypeEnum.QUESTION.getType()).andParentIdEqualTo(question.getId());
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if (comments.size() != 0) {
            for (Comment comment : comments) {
                //2.查找question每个评论的所有二级评论
                commentExample.clear();
                commentExample.createCriteria().andTypeEqualTo(CommentTypeEnum.COMMENT.getType()).andParentIdEqualTo(comment.getId());
                List<Comment> comments1 = commentMapper.selectByExample(commentExample);
                if (comments1.size() != 0) {
                    //3.删除所有二级评论
                    for (Comment comment1 : comments1) {
                        commentMapper.deleteByPrimaryKey(comment1.getId());
                    }
                }
                //4.删除所有一级评论
                commentMapper.deleteByPrimaryKey(comment.getId());
            }
        }
        //5.删除question
        questionMapper.deleteByPrimaryKey(question.getId());
        return "redirect:/profile/questions";
    }
}
