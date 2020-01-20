package com.mg.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @ClassName BootStrapTestController
 * @Description 对我描述吧
 * @Author MGLi
 * @Date 2019/12/17 14:28
 * @Version 1.0
 */
@Controller
public class BootStrapTestController {

    @GetMapping("/test")
    public String testB(){
        return "test";
    }

}
