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
import java.util.Objects;
import java.util.UUID;

@Controller
public class RegisterController {
    @Autowired
    UserMapper userMapper;

    @GetMapping("/register")
    public String toRegister() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam("name") String name,
                           @RequestParam("inputPassword") String inputPassword,
                           @RequestParam("confirmPassword") String confirmPassword,
                           @RequestParam("email") String email,
                           Model model,
                           HttpServletResponse response) {
        model.addAttribute("name", name);
        model.addAttribute("email", email);
        if (name == null || name.equals("")) {
            model.addAttribute("error", "用户名不能为空");
            return "register";
        }
        if (inputPassword == null || inputPassword.equals("")) {
            model.addAttribute("error", "密码不能为空");
            return "register";
        }
        if (inputPassword.length() > 20) {
            model.addAttribute("error", "密码长度不能超过20");
        }
        if (!Objects.equals(inputPassword, confirmPassword)) {
            model.addAttribute("error", "两次密码不一致");
            return "register";
        }
        User user = userMapper.findByEmail(email);
        if (user != null) {
            model.addAttribute("error", "邮箱 " + name + " 已被注册");
            return "register";
        }
        user = new User();
        String token = UUID.randomUUID().toString();
        user.setEmail(email);
        String accountId = "mail" + user.getEmail().split("@")[0];
        user.setToken(token);
        user.setName(name);
        user.setPassword(inputPassword);
        user.setGmtCreate(System.currentTimeMillis());
        user.setGmtModified(user.getGmtCreate());
        user.setAccountId(accountId);
        user.setAvatarUrl("https://avatars.githubusercontent.com/u/45116739?v=4");
        userMapper.register(user);
        response.addCookie(new Cookie("token", token));
        return "redirect:/";
    }
}
