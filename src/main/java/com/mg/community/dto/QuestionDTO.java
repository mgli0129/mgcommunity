package com.mg.community.dto;

import com.mg.community.model.User;
import lombok.Data;

@Data
public class QuestionDTO {

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
    private User user;

}
