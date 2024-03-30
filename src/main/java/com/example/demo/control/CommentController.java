package com.example.demo.control;

import com.example.demo.dao.CommentDAO;
import com.example.demo.dataobject.CommentDO;
import com.example.demo.model.Comment;
import com.example.demo.model.Paging;
import com.example.demo.model.Result;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentDAO commentDAO;

    @ResponseBody
    @PostMapping("/comment")
    public CommentDO insert(@RequestBody CommentDO commentDO) {
        commentDAO.insert(commentDO);
        return commentDO;
    }


    @ResponseBody
    @PostMapping("/comment/update")
    public CommentDO update(@RequestBody CommentDO commentDO) {
        commentDAO.update(commentDO);
        return commentDO;
    }

    @ResponseBody
    @GetMapping("/comment/del")
    public boolean delete(@RequestParam Long id) {
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
        return commentDAO.findByRefId(refId);
    }


    @ResponseBody
    @PostMapping("/comment/batchAdd")
    public List<CommentDO> batchAdd(@RequestBody List<CommentDO> commentDOS) {
        commentDAO.batchAdd(commentDOS);
        return commentDOS;
    }

    @ResponseBody
    @GetMapping("/comment/findByIds")
    public List<CommentDO> findByIds(@RequestParam List<Long> ids) {
        return commentDAO.findByIds(ids);
    }



}
