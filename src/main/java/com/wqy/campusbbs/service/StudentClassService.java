package com.wqy.campusbbs.service;

import com.wqy.campusbbs.mapper.SpecialtyMapper;
import com.wqy.campusbbs.mapper.StudentClassMapper;
import com.wqy.campusbbs.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentClassService {
    @Autowired
    private SpecialtyMapper specialtyMapper;

    @Autowired
    private StudentClassMapper studentClassMapper;

    public Map<Specialty, List<StudentClass>> listMap() {
        SpecialtyExample specialtyExample = new SpecialtyExample();
        specialtyExample.setOrderByClause("id");
        List<Specialty> specialties = specialtyMapper.selectByExample(specialtyExample);
        Map<Specialty, List<StudentClass>> map = new LinkedHashMap<>();
        for (Specialty specialty : specialties) {
            StudentClassExample studentClassExample = new StudentClassExample();
            studentClassExample.setOrderByClause("class_name");
            studentClassExample.createCriteria().andSpecialtyIdEqualTo(specialty.getId());
            List<StudentClass> studentClasses = studentClassMapper.selectByExample(studentClassExample);
            map.put(specialty, studentClasses);
        }
        return map;
    }
}
