package com.example.demo.service.impl;

import com.example.demo.dao.CommentDAO;
import com.example.demo.dataobject.CommentDO;
import com.example.demo.model.Comment;
import com.example.demo.model.Result;
import com.example.demo.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDAO commentDAO;
    @Override
    public Result<Comment> post(String refId, long userId, long parentId, String content) {
        Result<Comment> result = new Result<>();

        if (StringUtils.isEmpty(refId) || userId == 0 || StringUtils.isEmpty(content)) {
            result.setCode("500");
            result.setMessage("refId、userId、content不能为空");
            return result;
        }

        String body = StringEscapeUtils.escapeHtml4(content);

        CommentDO commentDO = new CommentDO();
        commentDO.setId(userId);
        commentDO.setRefId(refId);
        commentDO.setParentId(parentId);
        commentDO.setContent(content);
        commentDAO.insert(commentDO);

        result.setData(commentDO.toModel());
        result.setSuccess(true);

        return result;
    }

    @Override
    public Result<List<Comment>> query(String refId) {

        Result<List<Comment>> result = new Result<>();

        //查询所有的评论记录包含回复的
        List<Comment> comments = commentDAO.findByRefId(refId);
        //构建 map 结构
        Map<Long, Comment> commentMap = new HashMap<>();
        //初始化一个虚拟根节点，0 可以对应的是所有一级评论的父亲
        commentMap.put(0L, new Comment());
        //把所有的评论转换为 map 数据
        comments.forEach(comment -> commentMap.put(comment.getId(), comment));


        // 再次遍历评论数据
        comments.forEach(comment -> {
            //得到父评论的 ID
            Comment parent = commentMap.get(comment.getParentId());
            if (parent != null) {
                // 如果父评论存在，并且它的 children 列表尚未初始化
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<>());
                }
                // 在父评论里添加回复数据
                parent.getChildren().add(comment);
            } else { // 如果评论没有父评论（即为一级评论），则将其添加到虚拟根节点的 children 列表中
                if (commentMap.get(0L) != null) {
                    commentMap.get(0L).getChildren().add(comment); // 假设 0L 是一级评论的标识
                }
            }
        });

        // 得到所有的一级评论
        List<Comment> data = commentMap.get(0L) != null ? commentMap.get(0L).getChildren() : new ArrayList<>();

        result.setData(data);
        result.setSuccess(true);


        return result;
    }
}
