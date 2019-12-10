package com.mg.community.service;

import com.mg.community.dto.CommentDTO;
import com.mg.community.model.Comment;

import java.util.List;
import java.util.function.Supplier;

public interface CommentService {
    void createOrUpdate(Comment comment);

    List<CommentDTO> listByTargetId(Long id, Integer type);
}
