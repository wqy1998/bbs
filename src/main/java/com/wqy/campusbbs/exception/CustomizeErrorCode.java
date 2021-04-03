package com.wqy.campusbbs.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {
    QUESTION_NOT_FOUND(2001, "啊噢，你找的问题不见了，再去看看其它的吧"),
    TARGET_PARAM_NOT_FOUND(2002, "未选择任何问题或评论进行回复"),
    NOT_LOGIN(2003, "当前操作需要登录，请登录后重试"),
    SYS_ERROR(2004, "服务太热啦，要不稍等下再来试试~"),
    TYPE_PARAM_WRONG(2005, "评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006, "评论的回复不存在"),
    CONTENT_IS_EMPTY(2007, "输入内容不能为空"),
    READ_NOTIFICATION_FAIL(2008, "非法读取他人信息"),
    NOTIFICATION_NOT_FOUND(2009, "消息不翼而飞了"),
    ;

    private Integer code;
    private String message;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }
}
