package com.mg.community.controller;

import com.mg.community.Provider.GithubProvider;
import com.mg.community.dto.AccessTokenDTO;
import com.mg.community.dto.GithubUser;
import com.mg.community.mapper.UserMapper;
import com.mg.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class GithubOAuthController {

    @Autowired
    private GithubProvider githubProvider;

    @Autowired
    private UserMapper userMapper;

    @Value("${github.client.id}")
    private String githubClientId;

    @Value("${github.client.secret}")
    private String githubClientSecret;

    @Value("${github.redirect.uri}")
    private String githubRedirectUri;

    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code,
                           @RequestParam("state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response,
                           Model model) {
        if (code == null || code.equals("")){
            model.addAttribute("error","账户验证失败，请重新验证");
            return "/";
        }
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(githubClientId);
        accessTokenDTO.setClient_secret(githubClientSecret);
        accessTokenDTO.setRedirect_uri(githubRedirectUri);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        System.out.println(accessToken);

        String accessCode = accessToken.split("&")[0].split("=")[1];
        accessTokenDTO.setCode(accessCode);
        GithubUser githubUser = githubProvider.getGithubUser(accessTokenDTO);

        if (githubUser != null) {
            User accUser = userMapper.findUserByAccountId(githubUser.getLogin());
            String token = null;
            if(accUser == null){
                //登录成功，写入数据库
                User user = new User();
                user.setAccountId(githubUser.getLogin());
                user.setName(githubUser.getName());
                token = UUID.randomUUID().toString();
                user.setToken(token);
                user.setAvatarUrl(githubUser.getAvatarUrl());
                user.setGmtCreate(System.currentTimeMillis());
                user.setGmtModified(user.getGmtCreate());
                userMapper.insertUser(user);
            }else{
                token = accUser.getToken();
            }
            //写入cookie
            Cookie cookie = new Cookie("token", token);
            response.addCookie(cookie);

            return "redirect:/";
        } else {
            //登录失败
            return "redirect:/";

        }
    }
}
