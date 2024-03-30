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
}
