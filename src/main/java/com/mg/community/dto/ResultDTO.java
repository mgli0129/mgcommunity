package com.mg.community.dto;

import com.mg.community.exception.CustomizeErrorCode;
import com.mg.community.exception.CustomizeException;
import lombok.Data;

@Data
public class ResultDTO<T> {
    private String code;
    private String message;
    private T data;

    public static <T> ResultDTO okOf(T t){
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(CustomizeErrorCode.SUCCESS.getCode());
        resultDTO.setMessage(CustomizeErrorCode.SUCCESS.getMessage());
        resultDTO.setData(t);
        return resultDTO;
    }

    public static ResultDTO errorOf(String code, String message){
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }
    public static ResultDTO errorOf(CustomizeErrorCode errorCode) {
        return errorOf(errorCode.getCode(), errorCode.getMessage());
    }
    public static ResultDTO errorOf(CustomizeException e) {
        return errorOf(e.getCode(),e.getMessage());
    }

    public static ResultDTO okOf() {
        return errorOf(CustomizeErrorCode.SUCCESS);
    }
}
