package com.wqy.campusbbs.controller;

import com.wqy.campusbbs.exception.CustomizeErrorCode;
import com.wqy.campusbbs.exception.CustomizeException;
import com.wqy.campusbbs.mapper.UserMapper;
import com.wqy.campusbbs.model.User;
import com.wqy.campusbbs.model.UserExample;
import com.wqy.campusbbs.service.InformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class InformationController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private InformationService informationService;

    @GetMapping("/information/{id}")
    public String information(@PathVariable(name = "id") String id,
                              Model model,
                              HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        if (Long.parseLong(id) != user.getId()) {
            UserExample userExample = new UserExample();
            userExample.createCriteria().andIdEqualTo(Long.parseLong(id));
            List<User> users = userMapper.selectByExample(userExample);
            if (users.size() == 0) {
                throw new CustomizeException(CustomizeErrorCode.USER_NOT_EXIST);
            }
            user = users.get(0);
            informationService.searchInformation(model, user);
        } else {
            informationService.searchInformation(model, user);
            model.addAttribute("self", true);
        }
        return "information";
    }

    @PostMapping("/information/{id}/editName")
    public String editName(@PathVariable String id,
                           @RequestParam("newName") String newName,
                           @RequestParam("action") String action,
                           Model model,
                           HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (action.equals("editUserName")) {
            //修改用户名
            if (user.getName().equals(newName)) {
                model.addAttribute("error", "请输入新的用户名");
                model.addAttribute("self", true);
                informationService.searchInformation(model, user);
                return "information";
            }
            if (newName == null || newName.equals("")) {
                model.addAttribute("error", "新用户名不能为空");
                model.addAttribute("self", true);
                informationService.searchInformation(model, user);
                return "information";
            }
            UserExample userExample = new UserExample();
            userExample.createCriteria().andNameEqualTo(newName);
            List<User> users = userMapper.selectByExample(userExample);
            if (users.size() != 0) {
                model.addAttribute("error", "该用户名已存在");
                model.addAttribute("self", true);
                informationService.searchInformation(model, user);
                return "information";
            }
            User newUser = new User();
            newUser.setId(user.getId());
            newUser.setName(newName);
            int i = userMapper.updateByPrimaryKeySelective(newUser);
            if (i == 0) {
                model.addAttribute("error", "用户名修改失败");
                model.addAttribute("self", true);
                informationService.searchInformation(model, user);
                return "information";
            }
        }
        return "redirect:/information/" + id;
    }

    @PostMapping("/information/{id}/editEmail")
    public String editEmail(@PathVariable String id,
                            @RequestParam("newEmail") String newEmail,
                            @RequestParam("action") String action,
                            Model model,
                            HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (action.equals("editEmail")) {
            //修改邮箱
            if (user.getEmail().equals(newEmail)) {
                model.addAttribute("error", "请输入新的邮箱");
                model.addAttribute("self", true);
                informationService.searchInformation(model, user);
                return "information";
            }
            if (newEmail == null || newEmail.equals("")) {
                model.addAttribute("error", "新邮箱不能为空");
                model.addAttribute("self", true);
                informationService.searchInformation(model, user);
                return "information";
            }
            UserExample userExample = new UserExample();
            userExample.createCriteria().andEmailEqualTo(newEmail);
            List<User> users = userMapper.selectByExample(userExample);
            if (users.size() != 0) {
                model.addAttribute("error", "该邮箱已存在");
                model.addAttribute("self", true);
                informationService.searchInformation(model, user);
                return "information";
            }
            User newUser = new User();
            newUser.setId(user.getId());
            newUser.setEmail(newEmail);
            int i = userMapper.updateByPrimaryKeySelective(newUser);
            if (i == 0) {
                model.addAttribute("error", "邮箱修改失败");
                model.addAttribute("self", true);
                informationService.searchInformation(model, user);
                return "information";
            }
        }
        return "redirect:/information/" + id;
    }

    @PostMapping("/information/{id}/editPassword")
    public String editPassword(@PathVariable String id,
                               @RequestParam("inputNewPassword") String inputNewPassword,
                               @RequestParam("confirmNewPassword") String confirmNewPassword,
                               @RequestParam("action") String action,
                               Model model,
                               HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (action.equals("editPassword")) {
            //修改邮箱
            if (inputNewPassword == null || inputNewPassword.equals("") || confirmNewPassword == null || confirmNewPassword.equals("")) {
                model.addAttribute("error", "新密码不能为空");
                model.addAttribute("self", true);
                informationService.searchInformation(model, user);
                return "information";
            }
            if (user.getPassword().equals(inputNewPassword)) {
                model.addAttribute("error", "请输入新的密码");
                model.addAttribute("self", true);
                informationService.searchInformation(model, user);
                return "information";
            }
            if (!inputNewPassword.equals(confirmNewPassword)) {
                model.addAttribute("error", "两次密码不一致");
                model.addAttribute("self", true);
                informationService.searchInformation(model, user);
                return "information";
            }
            User newUser = new User();
            newUser.setId(user.getId());
            newUser.setPassword(confirmNewPassword);
            int i = userMapper.updateByPrimaryKeySelective(newUser);
            if (i == 0) {
                model.addAttribute("error", "密码修改失败");
                model.addAttribute("self", true);
                informationService.searchInformation(model, user);
                return "information";
            }
        }
        return "redirect:/information/" + id;
    }
}
