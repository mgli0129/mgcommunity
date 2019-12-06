package com.mg.community.service;

import com.mg.community.model.Comment;

public interface CommentService {
    void createOrUpdate(Comment comment);
}
