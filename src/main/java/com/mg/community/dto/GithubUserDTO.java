package com.mg.community.dto;

import lombok.Data;

@Data
public class GithubUserDTO {
    private String login;
    private String id;
    private String name;
    private String bio;
    private String url;
}
