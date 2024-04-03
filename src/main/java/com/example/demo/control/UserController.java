package com.example.demo.control;

import com.example.demo.dao.UserDAO;
import com.example.demo.model.Paging;
import com.example.demo.dataobject.UserDO;
import com.example.demo.model.Result;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private RedisTemplate redisTemplate;


    @GetMapping("/user/login")
    public String loginPage(Model model) {
        return "login";
    }
    @GetMapping("/user/reg")
    public String regPage(Model model) {
        return "reg";
    }
    @GetMapping("/user/info")
    public String info(Model model){
        return "info";
    }

    @ResponseBody
    @PostMapping("/user")
    public UserDO insert(@RequestBody UserDO userDO) {
        if (userDO == null) {
            return null;
        }
        userDAO.insert(userDO);
        redisTemplate.opsForValue().set(userDO.getUserName(), userDO);
        return userDO;
    }

    @ResponseBody
    @PostMapping("/user/update")
    public UserDO update(@RequestBody UserDO userDO) {
        if (userDO == null) {
            return null;
        }

        userDAO.update(userDO);
        redisTemplate.opsForValue().set(userDO.getUserName(), userDO);
        return userDO;
    }


    @ResponseBody
    @GetMapping("/user/del")
    public boolean delete(@RequestParam Long id) {
        if (id == null || id < 0) {
            return false;
        }

        List<Long> ids = new ArrayList<>();
        ids.add(id);
        List<UserDO> users = findByIds(ids);
        if (!CollectionUtils.isEmpty(users)) {
            UserDO userDO = users.get(0);
            redisTemplate.delete(userDO.getUserName());
        }

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

        if (StringUtils.isBlank(userName)) {
            return null;
        }

        UserDO userDO =(UserDO) redisTemplate.opsForValue().get(userName);
        if (userDO == null) {
            userDO = userDAO.findByUserName(userName);
        }
        return userDO;
    }

    @ResponseBody
    @GetMapping("/user/query")
    public List<UserDO> query(@RequestParam String keyWord) {

        if (StringUtils.isBlank(keyWord)) {
            return null;
        }

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
        if (CollectionUtils.isEmpty(userDOS)) {
            return null;
        }
        userDAO.batchAdd(userDOS);
        userDOS.forEach(userDO -> redisTemplate.opsForValue().set(userDO.getUserName(), userDO));
        return userDOS;
    }

    @ResponseBody
    @GetMapping("/user/findByIds")
    public List<UserDO> findByIds(@RequestParam List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }
        return userDAO.findByIds(ids);
    }



}
