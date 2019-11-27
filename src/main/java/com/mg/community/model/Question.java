package com.mg.community.model;

import lombok.Data;

@Data
public class Question {

    private int id;
    private String title;
    private String content;
    private String tag;
    private int commentCount;
    private int viewCount;
    private int likeCount;
    private Long gmtCreate;
    private Long gmtModified;
    private int creator;

}
