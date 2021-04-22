package com.wqy.campusbbs.service;

import com.wqy.campusbbs.dto.PaginationDTO;
import com.wqy.campusbbs.dto.UserDTO;
import com.wqy.campusbbs.enums.UserRoleEnum;
import com.wqy.campusbbs.mapper.SpecialtyMapper;
import com.wqy.campusbbs.mapper.StudentClassMapper;
import com.wqy.campusbbs.mapper.UserMapper;
import com.wqy.campusbbs.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StudentClassMapper studentClassMapper;

    @Autowired
    private SpecialtyMapper specialtyMapper;

    public void createOrUpdate(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andEmailEqualTo(user.getEmail());
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() == 0) {
            //插入
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insertSelective(user);
        } else {
            //更新
            User dbUser = users.get(0);
            User updateUser = new User();
            updateUser.setGmtModified(System.currentTimeMillis());
            updateUser.setAvatarUrl(user.getAvatarUrl());
            updateUser.setName(user.getName());
            updateUser.setToken(user.getToken());
            UserExample example = new UserExample();
            example.createCriteria().andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser, example);
        }
    }

    public PaginationDTO list(User user, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        int totalPage;
        int totalCount;
        UserExample userExample = new UserExample();
        if (user.getRole() == UserRoleEnum.TEACHER.getType()) {
            userExample.createCriteria().andRoleEqualTo(UserRoleEnum.STUDENT.getType());
        }
        totalCount = (int) userMapper.countByExample(userExample);
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage, page);
        Integer offset = size * (page - 1);
        userExample.setOrderByClause("gmt_create desc");
        List<User> users = userMapper.selectByExampleWithRowbounds(userExample, new RowBounds(offset, size));

        List<UserDTO> userDTOS = new ArrayList<>();

        for (User user1 : users) {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user1, userDTO);
            if (user1.getRole() == UserRoleEnum.STUDENT.getType()) {
                userDTO.setRoleName(UserRoleEnum.STUDENT.getName());
                StudentClass studentClass = studentClassMapper.selectByPrimaryKey(userDTO.getBelongToId());
                userDTO.setStudentClass(studentClass);
                Specialty specialty = specialtyMapper.selectByPrimaryKey(studentClass.getSpecialtyId());
                userDTO.setSpecialty(specialty);
            }
            if (user1.getRole() == UserRoleEnum.ADMINISTRATOR.getType()) {
                userDTO.setRoleName(UserRoleEnum.ADMINISTRATOR.getName());
                Specialty specialty = new Specialty();
                specialty.setName("");
                userDTO.setSpecialty(specialty);
                StudentClass studentClass = new StudentClass();
                studentClass.setClassName("");
                userDTO.setStudentClass(studentClass);
            }
            if (user1.getRole() == UserRoleEnum.TEACHER.getType()) {
                userDTO.setRoleName(UserRoleEnum.TEACHER.getName());
                Specialty specialty = specialtyMapper.selectByPrimaryKey(userDTO.getBelongToId());
                userDTO.setSpecialty(specialty);
                StudentClass studentClass = new StudentClass();
                studentClass.setClassName("");
                userDTO.setStudentClass(studentClass);
            }
            userDTOS.add(userDTO);
        }

        paginationDTO.setData(userDTOS);
        return paginationDTO;
    }
}
