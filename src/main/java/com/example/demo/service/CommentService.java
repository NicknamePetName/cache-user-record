package com.example.demo.service;

import com.example.demo.model.Comment;
import com.example.demo.model.Result;

public interface CommentService {

    /**
     * 发布评论
     *
     * @param refId
     * @param userId
     * @param parentId
     * @param content
     * @return
     */

    public Result<Comment> post(String refId, long userId, long parentId, String content);
}
