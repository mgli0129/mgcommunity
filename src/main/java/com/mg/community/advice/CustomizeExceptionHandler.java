package com.mg.community.advice;

import com.alibaba.fastjson.JSON;
import com.mg.community.dto.ResultDTO;
import com.mg.community.exception.CustomizeErrorCode;
import com.mg.community.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)
    ModelAndView handler(Throwable e,
                         Model model,
                         HttpServletRequest request,
                         HttpServletResponse response){
        String contentType = request.getContentType();
        ResultDTO resultDTO = new ResultDTO();
        if("application/json".equals(contentType)){
            PrintWriter writer = null;
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");

            try {
                writer = response.getWriter();
            } catch (IOException ee) {
            }
            if(e instanceof CustomizeException){
                resultDTO = ResultDTO.errorOf((CustomizeException) e);
            }else{
                resultDTO = ResultDTO.errorOf((CustomizeErrorCode.GENERAL_ERROR));
            }
            writer.write(JSON.toJSONString(resultDTO));
            writer.flush();
            writer.close();
            return null;

        }else{
            if(e instanceof CustomizeException){
                model.addAttribute("code", ((CustomizeException) e).getCode());
                model.addAttribute("message", e.getMessage());
            }else{
                model.addAttribute("code", CustomizeErrorCode.GENERAL_ERROR.getCode());
                model.addAttribute("message", CustomizeErrorCode.GENERAL_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }

    }

}
