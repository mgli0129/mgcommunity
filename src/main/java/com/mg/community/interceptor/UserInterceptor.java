package com.mg.community.interceptor;

import com.mg.community.mapper.UserMapper;
import com.mg.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserInterceptor implements HandlerInterceptor {

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //用户验证
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("token")) {
                    User user = userMapper.findUserByToken(c.getValue());
                    request.getSession().setAttribute("user", user);
                    break;
                }
            }
        }
        return true;
    }
}
