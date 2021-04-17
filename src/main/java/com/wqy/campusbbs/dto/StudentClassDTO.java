package com.wqy.campusbbs.dto;

import com.wqy.campusbbs.model.Specialty;
import lombok.Data;

@Data
public class StudentClassDTO {
    private Long id;
    private Long specialtyId;
    private String className;
    private Long gmtCreate;
    private Specialty specialty;
}
