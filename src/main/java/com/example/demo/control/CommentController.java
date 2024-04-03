package com.example.demo.control;

import com.example.demo.dao.CommentDAO;
import com.example.demo.dataobject.CommentDO;
import com.example.demo.model.Comment;
import com.example.demo.model.Paging;
import com.example.demo.model.Result;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentDAO commentDAO;
    @Autowired
    private RedisTemplate redisTemplate;

    @ResponseBody
    @PostMapping("/comment")
    public CommentDO insert(@RequestBody CommentDO commentDO) {
        if (commentDO == null) {
            return null;
        }
        commentDAO.insert(commentDO);
        redisTemplate.opsForValue().set(commentDO.getId(),commentDO);
        return commentDO;
    }


    @ResponseBody
    @PostMapping("/comment/update")
    public CommentDO update(@RequestBody CommentDO commentDO) {
        if (commentDO == null) {
            return null;
        }
        commentDAO.update(commentDO);
        redisTemplate.opsForValue().set(commentDO.getId(), commentDO);
        return commentDO;
    }

    @ResponseBody
    @GetMapping("/comment/del")
    public boolean delete(@RequestParam Long id) {
        if (id == null || id < 0) {
            return false;
        }
        CommentDO commentDO = (CommentDO) redisTemplate.opsForValue().get(id);
        if (commentDO == null) {
            List<Long> ids = new ArrayList<>();
            ids.add(id);
            List<CommentDO> commentDOS = commentDAO.findByIds(ids);
            if (CollectionUtils.isEmpty(commentDOS)) {
                return false;
            }
            commentDO = commentDOS.get(0);
        }
        redisTemplate.delete(commentDO.getId());
        return commentDAO.delete(id) > 0;
    }


    @ResponseBody
    @GetMapping("/comments")
    public Result<Paging<CommentDO>> getAll(Integer pageNum, Integer pageSize) {
        Result<Paging<CommentDO>> result = new Result<>();
        if (pageNum == null) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 15;
        }
        Page<CommentDO> page = PageHelper.startPage(pageNum,pageSize).doSelectPage(() -> commentDAO.findAll());
        result.setSuccess(true);
        result.setData(new Paging<>(page.getPageNum(),page.getPageSize(),page.getPages(),page.getTotal(),page.getResult()));
        return result;
    }

    @ResponseBody
    @GetMapping("/comment/findByRefId")
    public List<Comment> findByRefId(@RequestParam String refId) {
        if (StringUtils.isBlank(refId)) {
            return null;
        }

        return commentDAO.findByRefId(refId);
    }


    @ResponseBody
    @PostMapping("/comment/batchAdd")
    public List<CommentDO> batchAdd(@RequestBody List<CommentDO> commentDOS) {
        if (CollectionUtils.isEmpty(commentDOS)) {
            return null;
        }

        commentDAO.batchAdd(commentDOS);
        commentDOS.forEach(commentDO -> redisTemplate.opsForValue().set(commentDO.getId(), commentDO));
        return commentDOS;
    }

    @ResponseBody
    @GetMapping("/comment/findByIds")
    public List<CommentDO> findByIds(@RequestParam List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }

        return commentDAO.findByIds(ids);
    }



}
