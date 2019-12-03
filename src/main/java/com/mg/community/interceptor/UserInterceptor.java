package com.mg.community.interceptor;

import com.mg.community.mapper.UserMapper;
import com.mg.community.model.User;
import com.mg.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
                    UserExample userExample = new UserExample();
                    userExample.createCriteria().andTokenEqualTo(c.getValue());
                    List<User> userList = userMapper.selectByExample(userExample);
                    if(userList.size() != 0){
                        request.getSession().setAttribute("user", userList.get(0));
                    }
                    break;
                }
            }
        }
        return true;
    }
}
