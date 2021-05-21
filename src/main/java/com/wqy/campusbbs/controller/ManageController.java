package com.wqy.campusbbs.controller;

import com.wqy.campusbbs.dto.PaginationDTO;
import com.wqy.campusbbs.enums.UserRoleEnum;
import com.wqy.campusbbs.exception.CustomizeErrorCode;
import com.wqy.campusbbs.exception.CustomizeException;
import com.wqy.campusbbs.mapper.*;
import com.wqy.campusbbs.model.*;
import com.wqy.campusbbs.service.QuestionService;
import com.wqy.campusbbs.service.StudentClassService;
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
    private SpecialtyMapper specialtyMapper;

    @Autowired
    private StudentClassMapper studentClassMapper;

    @Autowired
    private StudentClassService studentClassService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @GetMapping("/manage/{action}")
    public String manage(@PathVariable(name = "action") String action,
                         @RequestParam(name = "page", defaultValue = "1") Integer page,
                         @RequestParam(name = "size", defaultValue = "6") Integer size,
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
            model.addAttribute("sectionName", "文章管理");
        } else if ("users".equals(action)) {
            PaginationDTO paginationDTO = userService.list(user, page, size);
            model.addAttribute("pagination", paginationDTO);
            model.addAttribute("section", "users");
            model.addAttribute("sectionName", "用户管理");
        } else if ("specialtyClass".equals(action)) {
            model.addAttribute("section", "specialtyClass");
            model.addAttribute("sectionName", "专业班级管理");
            Map<Specialty, List<StudentClass>> map = studentClassService.listMap();
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

    @GetMapping("/manage/{action}/{part}/{id}")
    public String manage(@PathVariable(name = "action") String action,
                         @PathVariable(name = "part") String part,
                         @PathVariable(name = "id") String id,
                         HttpServletRequest request,
                         Model model) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || user.getRole() != UserRoleEnum.ADMINISTRATOR.getType()) {
            return "redirect:/";
        }
        if (action.equals("edit")) {
            if (part.equals("specialty")) {
                Specialty specialty = specialtyMapper.selectByPrimaryKey(Long.parseLong(id));
                model.addAttribute("specialty", specialty);
                model.addAttribute("action", "edit");
                model.addAttribute("section", "edit");
                model.addAttribute("sectionName", "修改专业名");
                model.addAttribute("part", "specialty");
                return "manage";
            } else if (part.equals("class")) {
                StudentClass studentClass = studentClassMapper.selectByPrimaryKey(Long.parseLong(id));
                Specialty specialty = specialtyMapper.selectByPrimaryKey(studentClass.getSpecialtyId());
                model.addAttribute("class", studentClass);
                model.addAttribute("specialty", specialty);
                model.addAttribute("action", "edit");
                model.addAttribute("section", "edit");
                model.addAttribute("sectionName", "修改班级名");
                model.addAttribute("part", "class");
                return "manage";
            }
        } else if (action.equals("delete")) {
            if (part.equals("specialty")) {
                UserExample userExample = new UserExample();
                userExample.createCriteria().andBelongToIdEqualTo(Long.parseLong(id)).andRoleEqualTo(UserRoleEnum.TEACHER.getType());
                long count = userMapper.countByExample(userExample);
                StudentClassExample studentClassExample = new StudentClassExample();
                studentClassExample.createCriteria().andSpecialtyIdEqualTo(Long.parseLong(id));
                List<StudentClass> studentClasses = studentClassMapper.selectByExample(studentClassExample);
                if (studentClasses.size() != 0) {
                    model.addAttribute("error", "该专业下还有班级，不可删除");
                    return "forward:/manage/specialtyClass";
                }
                if (count != 0) {
                    model.addAttribute("error", "该专业下还有老师，不能删除");
                    return "forward:/manage/specialtyClass";
                }
                specialtyMapper.deleteByPrimaryKey(Long.parseLong(id));
                return "redirect:/manage/specialtyClass";
            } else if (part.equals("class")) {
                UserExample userExample = new UserExample();
                userExample.createCriteria().andBelongToIdEqualTo(Long.parseLong(id)).andRoleEqualTo(UserRoleEnum.STUDENT.getType());
                long count = userMapper.countByExample(userExample);
                if (count != 0) {
                    model.addAttribute("error", "该班级下还有学生，不可删除");
                    return "forward:/manage/specialtyClass";
                }
                studentClassMapper.deleteByPrimaryKey(Long.parseLong(id));
                return "redirect:/manage/specialtyClass";
            }
        }
        model.addAttribute("error", "删除失败");
        return "forward:/manage/specialtyClass";
    }

    @PostMapping("/manage/edit/{part}/{id}")
    public String manage(@PathVariable(name = "part") String part,
                         @PathVariable(name = "id") String id,
                         @RequestParam(name = "newSpecialtyName", required = false) String newSpecialtyName,
                         @RequestParam(name = "newClassName", required = false) String newClassName,
                         HttpServletRequest request,
                         Model model) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || user.getRole() != UserRoleEnum.ADMINISTRATOR.getType()) {
            return "redirect:/";
        }
        if (part.equals("specialty")) {
            Specialty specialty = specialtyMapper.selectByPrimaryKey(Long.parseLong(id));
            if (specialty == null) {
                throw new CustomizeException(CustomizeErrorCode.SPECIALTY_NOT_EXIST);
            }
            if (newSpecialtyName == null || newSpecialtyName.equals("")) {
                model.addAttribute("specialty", specialty);
                model.addAttribute("error", "请输入新的专业名");
                model.addAttribute("section", "edit");
                model.addAttribute("sectionName", "修改专业名");
                return "manage";
            }
            SpecialtyExample specialtyExample = new SpecialtyExample();
            specialtyExample.createCriteria().andNameEqualTo(newSpecialtyName);
            List<Specialty> specialties = specialtyMapper.selectByExample(specialtyExample);
            if (specialties.size() > 0) {
                model.addAttribute("specialty", specialty);
                model.addAttribute("error", "专业名已存在");
                model.addAttribute("section", "edit");
                model.addAttribute("sectionName", "修改专业名");
                return "manage";
            }
            Specialty newSpecialty = new Specialty();
            newSpecialty.setId(specialty.getId());
            newSpecialty.setName(newSpecialtyName);
            specialtyMapper.updateByPrimaryKeySelective(newSpecialty);
        } else if (part.equals("class")) {
            StudentClass studentClass = studentClassMapper.selectByPrimaryKey(Long.parseLong(id));
            if (studentClass == null) {
                throw new CustomizeException(CustomizeErrorCode.CLASS_NOT_EXIST);
            }
            Specialty specialty = specialtyMapper.selectByPrimaryKey(studentClass.getSpecialtyId());
            if (newSpecialtyName == null || newSpecialtyName.equals("")) {
                model.addAttribute("specialty", specialty);
                model.addAttribute("class", studentClass);
                model.addAttribute("error", "请输入新的班级名");
                model.addAttribute("section", "edit");
                model.addAttribute("sectionName", "修改班级名");
                return "manage";
            }
            StudentClassExample studentClassExample = new StudentClassExample();
            studentClassExample.createCriteria().andSpecialtyIdEqualTo(specialty.getId()).andClassNameEqualTo(newClassName);
            List<StudentClass> studentClasses = studentClassMapper.selectByExample(studentClassExample);
            if (studentClasses.size() > 0) {
                model.addAttribute("specialty", specialty);
                model.addAttribute("class", studentClass);
                model.addAttribute("error", "班级名已存在");
                model.addAttribute("section", "edit");
                model.addAttribute("sectionName", "修改班级名");
                return "manage";
            }
            StudentClass newStudentClass = new StudentClass();
            newStudentClass.setId(studentClass.getId());
            newStudentClass.setClassName(newClassName);
            studentClassMapper.updateByPrimaryKeySelective(newStudentClass);
        }
        return "redirect:/manage/specialtyClass";
    }

    @GetMapping("/manage/add/{part}")
    public String add(@PathVariable(name = "part") String part,
                      Model model) {
        if ("specialty".equals(part)) {
            model.addAttribute("section", "add");
            model.addAttribute("sectionName", "增加专业");
            model.addAttribute("action", "specialty");
        } else if ("class".equals(part)) {
            List<Specialty> specialties = specialtyMapper.selectByExample(new SpecialtyExample());
            model.addAttribute("specialties", specialties);
            model.addAttribute("section", "add");
            model.addAttribute("sectionName", "增加班级");
            model.addAttribute("action", "class");
        }
        return "manage";
    }

    @PostMapping("/manage/add/{part}")
    public String add(@PathVariable(name = "part") String action,
                      @RequestParam(name = "addSpecialty", required = false) String specialtyName,
                      @RequestParam(name = "specialty", required = false) String specialtyId,
                      @RequestParam(name = "addClass", required = false) String className,
                      HttpServletRequest request,
                      Model model) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || user.getRole() != UserRoleEnum.ADMINISTRATOR.getType()) {
            return "redirect:/";
        }
        if (action.equals("specialty")) {
            if (specialtyName == null || specialtyName.equals("")) {
                model.addAttribute("error", "请输入专业名");
                model.addAttribute("sectionName", "增加专业");
                model.addAttribute("action", "specialty");
                return "manage";
            }
            SpecialtyExample specialtyExample = new SpecialtyExample();
            specialtyExample.createCriteria().andNameEqualTo(specialtyName);
            List<Specialty> specialties = specialtyMapper.selectByExample(specialtyExample);
            if (specialties.size() > 0) {
                model.addAttribute("error", "专业名已存在");
                model.addAttribute("section", "add");
                model.addAttribute("sectionName", "增加专业");
                model.addAttribute("action", "specialty");
                return "manage";
            }
            Specialty specialty = new Specialty();
            specialty.setName(specialtyName);
            specialty.setGmtCreate(System.currentTimeMillis());
            specialtyMapper.insert(specialty);
        } else if (action.equals("class")) {
            List<Specialty> specialties = specialtyMapper.selectByExample(new SpecialtyExample());
            model.addAttribute("specialties", specialties);
            if (specialtyId == null || specialtyId.equals("")) {
                model.addAttribute("section", "add");
                model.addAttribute("sectionName", "增加班级");
                model.addAttribute("action", "class");
                model.addAttribute("error", "请选择专业");
                return "manage";
            }
            if (className == null || className.equals("")) {
                model.addAttribute("section", "add");
                model.addAttribute("sectionName", "增加班级");
                model.addAttribute("action", "class");
                model.addAttribute("error", "请填写班级");
                return "manage";
            }
            StudentClassExample studentClassExample = new StudentClassExample();
            studentClassExample.createCriteria().andSpecialtyIdEqualTo(Long.parseLong(specialtyId)).andClassNameEqualTo(className);
            List<StudentClass> studentClasses = studentClassMapper.selectByExample(studentClassExample);
            if (studentClasses.size() > 0) {
                model.addAttribute("error", "班级名已存在");
                model.addAttribute("section", "add");
                model.addAttribute("sectionName", "增加班级");
                model.addAttribute("action", "class");
                return "manage";
            }
            StudentClass studentClass = new StudentClass();
            studentClass.setSpecialtyId(Long.parseLong(specialtyId));
            studentClass.setClassName(className);
            studentClass.setGmtCreate(System.currentTimeMillis());
            studentClassMapper.insert(studentClass);
        }
        return "redirect:/manage/specialtyClass";
    }
}
