package com.wqy.campusbbs.controller;

import com.wqy.campusbbs.mapper.UserMapper;
import com.wqy.campusbbs.model.User;
import com.wqy.campusbbs.model.UserExample;
import com.wqy.campusbbs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@Controller
public class LoginController {
    @Autowired
    UserMapper userMapper;

    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String toLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        Model model,
                        HttpServletResponse response) {
        model.addAttribute("email", email);
        if (email == null || email.equals("") || password == null || password.equals("")) {
            model.addAttribute("error", "请输入用户名密码");
            return "login";
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        UserExample userExample = new UserExample();
        userExample.createCriteria().andEmailEqualTo(email).andPasswordEqualTo(password);
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() == 0) {
            model.addAttribute("error", "邮箱或密码错误");
            return "login";
        }
        String token = UUID.randomUUID().toString();
        User loginUser = users.get(0);
        loginUser.setToken(token);
        loginUser.setAvatarUrl("https://avatars.githubusercontent.com/u/45116739?v=4");
        userService.createOrUpdate(loginUser);
        response.addCookie(new Cookie("token", token));
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
