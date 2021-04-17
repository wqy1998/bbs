package com.wqy.campusbbs.controller;

import com.wqy.campusbbs.enums.UserRoleEnum;
import com.wqy.campusbbs.mapper.SpecialtyMapper;
import com.wqy.campusbbs.mapper.StudentClassMapper;
import com.wqy.campusbbs.mapper.UserMapper;
import com.wqy.campusbbs.model.*;
import com.wqy.campusbbs.service.StudentClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Controller
public class RegisterController {
    @Autowired
    UserMapper userMapper;

    @Autowired
    SpecialtyMapper specialtyMapper;

    @Autowired
    StudentClassMapper studentClassMapper;

    @Autowired
    StudentClassService studentClassService;

    @GetMapping("/register")
    public String toRegister(Model model) {
        Map<Specialty, List<StudentClass>> specialties = studentClassService.listMap();
        model.addAttribute("specialties", specialties);
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam("name") String name,
                           @RequestParam("inputPassword") String inputPassword,
                           @RequestParam("confirmPassword") String confirmPassword,
                           @RequestParam("email") String email,
                           @RequestParam("specialty") String specialty,
                           @RequestParam("studentClass") String studentClass,
                           Model model,
                           HttpServletResponse response) {
        model.addAttribute("name", name);
        model.addAttribute("email", email);
        Map<Specialty, List<StudentClass>> specialties = studentClassService.listMap();
        model.addAttribute("specialties", specialties);
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
        UserExample userExample = new UserExample();
        userExample.createCriteria().andEmailEqualTo(email);
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() != 0) {
            model.addAttribute("error", "邮箱 " + email + " 已被注册");
            return "register";
        }
        UserExample userExample1 = new UserExample();
        userExample1.createCriteria().andNameEqualTo(name);
        List<User> users1 = userMapper.selectByExample(userExample1);
        if (users1.size() != 0) {
            model.addAttribute("error", "用户名 " + name + " 已存在");
            return "register";
        }
        if (specialty == null || specialty.equals("")) {
            model.addAttribute("error", "请选择您的专业");
            return "register";
        }
        if (studentClass == null || studentClass.equals("")) {
            model.addAttribute("error", "请选择您的班级");
            return "register";
        }
        User user = new User();
        String token = UUID.randomUUID().toString();
        user.setEmail(email);
        user.setToken(token);
        user.setName(name);
        user.setPassword(inputPassword);
        user.setGmtCreate(System.currentTimeMillis());
        user.setGmtModified(user.getGmtCreate());
        user.setAvatarUrl("/images/default-avatar.png");
        if (studentClass.equals("teacher")) {
            user.setRole(UserRoleEnum.TEACHER.getType());
            user.setBelongToId(Long.parseLong(specialty));
        } else {
            user.setRole(UserRoleEnum.STUDENT.getType());
            user.setBelongToId(Long.parseLong(studentClass));
        }
        userMapper.insert(user);
        response.addCookie(new Cookie("token", token));
        return "redirect:/";
    }
}
