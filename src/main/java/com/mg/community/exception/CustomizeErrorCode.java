package com.mg.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode{

    SUCCESS("0000", "处理成功"),
    GENERAL_ERROR("2000", "服务冒烟了，要不您稍后再试试"),
    QUESTION_NOT_FOUND("2001","您找到问题不在了，要不要换个试"),
    COMMENT_NOT_FOUND("2002","当前评论不存在，要不要换个试"),
    NO_LOGIN("2003","当前操作需要登录，请登录后重试"),
    TARGET_PARAM_NOT_FOUND("2004","未选中任何问题或评论进行回复"),
    TYPE_PARAM_NOT_FOUND("2005","回复的类型不存在"),
    CONTENT_IS_EMPTY("2006","评论的内容不能为空"),
    ;

    private String code;
    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getCode() {
        return code;
    }

    CustomizeErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
