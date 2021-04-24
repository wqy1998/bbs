package com.wqy.campusbbs.controller;

import com.wqy.campusbbs.dto.PaginationDTO;
import com.wqy.campusbbs.mapper.QuestionMapper;
import com.wqy.campusbbs.model.Question;
import com.wqy.campusbbs.model.QuestionExample;
import com.wqy.campusbbs.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionMapper questionMapper;

    @GetMapping("/")
    public String home(Model model,
                       @RequestParam(name = "page", defaultValue = "1") Integer page,
                       @RequestParam(name = "size", defaultValue = "5") Integer size,
                       @RequestParam(name = "search", required = false) String search) {
        PaginationDTO pagination = questionService.list(search, page, size);
        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);
        QuestionExample questionExample = new QuestionExample();
        questionExample.setOrderByClause("view_count desc");
        List<Question> questions = questionMapper.selectByExampleWithBLOBs(questionExample);
        if (questions.size()<=10) {
            model.addAttribute("hotQuestions", questions);
        } else {
            List<Question> questionList =  questions.subList(0,10);
            model.addAttribute("hotQuestions", questionList);
        }
        return "home";
    }
}
