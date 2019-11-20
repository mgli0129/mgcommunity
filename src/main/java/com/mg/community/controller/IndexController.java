package com.mg.community.controller;

import com.mg.community.mapper.UserMapper;
import com.mg.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/")
    public String index(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies) {
            if (c.getName().equals("token")) {
                User user = userMapper.findUserByToken(c.getValue());
                request.getSession().setAttribute("user", user);
                break;
            }
        }
        return "index";
    }

}
