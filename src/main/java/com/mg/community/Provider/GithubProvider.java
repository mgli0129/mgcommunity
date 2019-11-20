package com.mg.community.Provider;

import com.alibaba.fastjson.JSON;
import com.mg.community.dto.AccessTokenDTO;
import com.mg.community.dto.GithubUserDTO;
import okhttp3.*;
import org.springframework.stereotype.Component;

@Component
public class GithubProvider {

    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        MediaType mediaType
                = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String str = response.body().string();
            System.out.println(str);
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public GithubUserDTO getGithubUser(AccessTokenDTO accessTokenDTO) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessTokenDTO.getCode())
                .build();
        try {
            Response response = client.newCall(request).execute();
            String str = response.body().string();
            GithubUserDTO githubUserDTO = JSON.parseObject(str,GithubUserDTO.class);
            System.out.println(githubUserDTO);
            return githubUserDTO;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
