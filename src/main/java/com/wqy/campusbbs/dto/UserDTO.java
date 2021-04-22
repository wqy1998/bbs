package com.wqy.campusbbs.dto;

import com.wqy.campusbbs.model.Specialty;
import com.wqy.campusbbs.model.StudentClass;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private Long belongToId;
    private String name;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer role;
    private String avatarUrl;
    private String password;
    private String email;
    private Specialty specialty;
    private StudentClass studentClass;
    private String roleName;
}
