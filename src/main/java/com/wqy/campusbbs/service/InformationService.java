package com.wqy.campusbbs.service;

import com.wqy.campusbbs.enums.UserRoleEnum;
import com.wqy.campusbbs.mapper.QuestionMapper;
import com.wqy.campusbbs.mapper.SpecialtyMapper;
import com.wqy.campusbbs.mapper.StudentClassMapper;
import com.wqy.campusbbs.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class InformationService {

    @Autowired
    private SpecialtyMapper specialtyMapper;

    @Autowired
    private StudentClassMapper studentClassMapper;

    @Autowired
    private QuestionMapper questionMapper;

    public void searchInformation(Model model, User user) {
        List<Specialty> specialties;
        Specialty specialty;
        SpecialtyExample specialtyExample = new SpecialtyExample();
        if (user.getRole().equals(UserRoleEnum.TEACHER.getType())) {
            specialtyExample.createCriteria().andIdEqualTo(user.getBelongToId());
            specialties = specialtyMapper.selectByExample(specialtyExample);
            specialty = specialties.get(0);
            model.addAttribute("user", user);
            model.addAttribute("specialty", specialty.getName());
            model.addAttribute("role", UserRoleEnum.TEACHER.getName());
        } else if (user.getRole().equals(UserRoleEnum.STUDENT.getType())) {
            StudentClassExample studentClassExample = new StudentClassExample();
            studentClassExample.createCriteria().andIdEqualTo(user.getBelongToId());
            List<StudentClass> studentClasses = studentClassMapper.selectByExample(studentClassExample);
            StudentClass studentClass = studentClasses.get(0);
            specialtyExample.createCriteria().andIdEqualTo(studentClass.getSpecialtyId());
            specialties = specialtyMapper.selectByExample(specialtyExample);
            specialty = specialties.get(0);
            model.addAttribute("user", user);
            model.addAttribute("specialty", specialty.getName());
            model.addAttribute("studentClass", studentClass.getClassName());
            model.addAttribute("role", UserRoleEnum.STUDENT.getName());
        } else if (user.getRole().equals(UserRoleEnum.ADMINISTRATOR.getType())) {
            model.addAttribute("user", user);
            model.addAttribute("role", UserRoleEnum.ADMINISTRATOR.getName());
        }
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(user.getId());
        questionExample.setOrderByClause("gmt_create desc");
        List<Question> questions = questionMapper.selectByExample(questionExample);
        if (questions.size() <= 15) {
            model.addAttribute("questions", questions);
        } else {
            questions = questions.subList(0,15);
            model.addAttribute("questions", questions);
        }
    }
}
