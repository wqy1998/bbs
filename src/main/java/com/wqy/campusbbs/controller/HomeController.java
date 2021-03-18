package com.wqy.campusbbs.controller;

import com.wqy.campusbbs.dto.PaginationDTO;
import com.wqy.campusbbs.mapper.UserMapper;
import com.wqy.campusbbs.model.User;
import com.wqy.campusbbs.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String home(HttpServletRequest request,
                       Model model,
                       @RequestParam(name = "page", defaultValue = "1") Integer page,
                       @RequestParam(name = "size", defaultValue = "6") Integer size) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }
        PaginationDTO pagination = questionService.list(page, size);
        model.addAttribute("pagination", pagination);
        return "home";
    }
}
