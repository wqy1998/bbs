package com.wqy.campusbbs.controller;

import com.wqy.campusbbs.mapper.UserMapper;
import com.wqy.campusbbs.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class LoginController {
    @Autowired
    UserMapper userMapper;

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
        User loginUser = userMapper.findByEmailAndPassword(user);
        if (loginUser == null) {
            model.addAttribute("error", "邮箱或密码错误");
            return "login";
        }
        String token = loginUser.getToken();
        response.addCookie(new Cookie("token", token));
        return "redirect:/";
    }
}
