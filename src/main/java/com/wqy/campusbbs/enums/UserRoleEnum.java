package com.wqy.campusbbs.enums;

public enum UserRoleEnum {
    ADMINISTRATOR(0,"管理员"),
    STUDENT(1,"学生"),
    TEACHER(2,"老师"),
    ;

    private int type;
    private String name;

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    UserRoleEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }
}
