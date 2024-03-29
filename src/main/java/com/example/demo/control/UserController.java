package com.example.demo.control;

import com.example.demo.dao.UserDAO;
import com.example.demo.model.Paging;
import com.example.demo.dataobject.UserDO;
import com.example.demo.model.Result;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserDAO userDAO;

    @ResponseBody
    @PostMapping("/user")
    public UserDO add(@RequestBody UserDO userDO) {
        userDAO.add(userDO);
        return userDO;
    }

    @ResponseBody
    @PostMapping("/user/update")
    public UserDO update(@RequestBody UserDO userDO) {
        userDAO.update(userDO);
        return userDO;
    }


    @ResponseBody
    @GetMapping("/user/del")
    public boolean delete(@RequestParam Long id) {
        return userDAO.delete(id) > 0;
    }

    @ResponseBody
    @GetMapping("/users")
    public Result<Paging<UserDO>> getAll(@RequestParam(defaultValue = "1") Integer pageNum,@RequestParam(defaultValue = "15") Integer pageSize) {
        Result<Paging<UserDO>> result = new Result<>();
        Page<UserDO> page = PageHelper.startPage(pageNum,pageSize).doSelectPage(() -> userDAO.findAll());
        result.setSuccess(true);
        result.setData(new Paging<>(page.getPageNum(), page.getPageSize(), page.getPages(), page.getTotal(),page.getResult()));
        return  result;
    }

    @ResponseBody
    @GetMapping("/user/findByUserName")
    public UserDO findByUserName(@RequestParam String userName) {
        return userDAO.findByUserName(userName);
    }

    @ResponseBody
    @GetMapping("/user/query")
    public List<UserDO> query(@RequestParam String keyWord) {
        return userDAO.query(keyWord);
    }


    @ResponseBody
    @GetMapping("/user/search")
    public List<UserDO> search(@RequestParam String keyWord,
                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                               @RequestParam LocalDateTime startTime,
                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                               @RequestParam LocalDateTime endTime) {
        return userDAO.search(keyWord, startTime, endTime);
    }

    @ResponseBody
    @PostMapping("/user/batchAdd")
    public List<UserDO> batchAdd(@RequestBody List<UserDO> userDOS) {
        userDAO.batchAdd(userDOS);
        return userDOS;
    }

    @ResponseBody
    @GetMapping("/user/findByIds")
    public List<UserDO> findByIds(@RequestParam List<Long> ids) {
        return userDAO.findByIds(ids);
    }



}
