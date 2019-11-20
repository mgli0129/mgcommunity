package com.mg.community.controller;

import com.mg.community.Provider.GithubProvider;
import com.mg.community.dto.AccessTokenDTO;
import com.mg.community.dto.GithubUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GithubOAuthController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String githubClientId;

    @Value("${github.client.secret}")
    private String githubClientSecret;

    @Value("${github.redirect.uri}")
    private String githubRedirectUri;

    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code,
                           @RequestParam("state") String state,
                           Model model){
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
        GithubUserDTO githubUser = githubProvider.getGithubUser(accessTokenDTO);
        System.out.println(githubUser.getName());

        model.addAttribute("gitname", githubUser.getName());
        return "index";
    }
}
