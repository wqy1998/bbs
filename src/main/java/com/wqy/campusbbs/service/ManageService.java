package com.wqy.campusbbs.service;

import com.wqy.campusbbs.mapper.SpecialtyMapper;
import com.wqy.campusbbs.mapper.StudentClassMapper;
import com.wqy.campusbbs.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ManageService {
    @Autowired
    private SpecialtyMapper specialtyMapper;

    @Autowired
    private StudentClassMapper studentClassMapper;

    public Map<Specialty, List<StudentClass>> specialtyListMap() {
        Map<Specialty, List<StudentClass>> map = new HashMap<>();
        SpecialtyExample specialtyExample = new SpecialtyExample();
        List<Specialty> specialties = specialtyMapper.selectByExample(specialtyExample);
        for (Specialty specialty : specialties) {
            StudentClassExample studentClassExample = new StudentClassExample();
            studentClassExample.createCriteria().andSpecialtyIdEqualTo(specialty.getId());
            List<StudentClass> studentClasses = studentClassMapper.selectByExample(studentClassExample);
            map.put(specialty, studentClasses);
        }
        return map;
    }
}
