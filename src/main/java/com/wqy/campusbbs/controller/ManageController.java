package com.wqy.campusbbs.controller;

import com.wqy.campusbbs.dto.PaginationDTO;
import com.wqy.campusbbs.mapper.*;
import com.wqy.campusbbs.model.*;
import com.wqy.campusbbs.service.ManageService;
import com.wqy.campusbbs.service.QuestionService;
import com.wqy.campusbbs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
public class ManageController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private ManageService manageService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @GetMapping("/manage/{action}")
    public String manage(@PathVariable(name = "action") String action,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "5") Integer size,
                          @RequestParam(name = "search", required = false) String search,
                          HttpServletRequest request,
                          Model model) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        if ("questions".equals(action)) {
            PaginationDTO pagination = questionService.list(search, page, size);
            model.addAttribute("pagination", pagination);
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "问题管理");
        } else if ("users".equals(action)) {
            PaginationDTO paginationDTO = userService.list(user, page, size);
            model.addAttribute("pagination", paginationDTO);
            model.addAttribute("section", "users");
            model.addAttribute("sectionName", "用户管理");
        } else if ("specialtyClass".equals(action)) {
            model.addAttribute("section", "specialtyClass");
            model.addAttribute("sectionName", "专业班级管理");
            Map<Specialty, List<StudentClass>> map = manageService.specialtyListMap();
            model.addAttribute("specialtyClasses", map);
        }
        return "manage";
    }

    @PostMapping("/manage/{id}/delete")
    public String deleteUser(@PathVariable(name = "id") String id) {
        //1.删除用户所有评论
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andCommentatorEqualTo(Long.parseLong(id));
        commentMapper.deleteByExample(commentExample);
        //2.删除用户所有问题
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(Long.parseLong(id));
        questionMapper.deleteByExample(questionExample);
        //3.删除用户
        userMapper.deleteByPrimaryKey(Long.parseLong(id));
        return "redirect:/manage/users";
    }
}
