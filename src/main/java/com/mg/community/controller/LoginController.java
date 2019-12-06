package com.mg.community.controller;

import com.mg.community.model.User;
import com.mg.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String dologin(
            @RequestParam(value = "username", required = true) String username,
            @RequestParam(value = "password", required = true) String password,
            HttpServletResponse response,
            Model model) {

        //页面回显字段信息
        model.addAttribute("username", username);

        User user = userService.findByName(username);
        if (user == null || !password.equals(user.getPwd())) {
            model.addAttribute("error", "用户名或密码验证不通过");
            return "login";
        }

        //写入cookie
        Cookie cookie = new Cookie("token", user.getToken());
        response.addCookie(cookie);

        return "redirect:/";
    }
}
