package com.example.demo.api;

import com.example.demo.model.Comment;
import com.example.demo.model.Result;

import com.example.demo.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CommentAPI {

    @Autowired
    private CommentService commentService;

    @PostMapping("/api/comment/post")
    public Result<Comment> post(@RequestParam String refId, @RequestParam long parentId, @RequestParam String content, HttpServletRequest request) {
        long userId = (long) request.getSession().getAttribute("userId");
        return commentService.post(refId,userId,parentId,content);
    }

    @GetMapping("/api/comment/query")
    public Result<List<Comment>> query(@RequestParam String refId) {
        return commentService.query(refId);
    }
}
