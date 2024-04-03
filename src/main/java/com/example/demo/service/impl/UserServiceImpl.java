package com.example.demo.service.impl;

import com.example.demo.dao.UserDAO;
import com.example.demo.dataobject.UserDO;
import com.example.demo.model.Result;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Result<User> register(String userName, String pwd) {

        Result<User> result = new Result<>();

        if (StringUtils.isEmpty(userName)) {
            result.setCode("600");
            result.setMessage("用户名不能为空");
            return result;
        }
        if (StringUtils.isEmpty(pwd)) {
            result.setCode("601");
            result.setMessage("密码不能为空");
            return result;
        }


        // 使用 redis 优化查询
        UserDO userDO = (UserDO) redisTemplate.opsForValue().get(userName);
        if (userDO == null) {
            userDO = userDAO.findByUserName(userName);
        }

        if (userDO != null) {
            result.setCode("602");
            result.setMessage("用户名已存在");
            return result;
        }

        // 密码加自定义盐值，确保密码安全
        String saltPwd = pwd + "_ykd2050";
        // 生成md5值，并转为大写字母
        String md5Pwd = DigestUtils.md5Hex(saltPwd).toUpperCase();

        UserDO userDO1 = new UserDO();

        userDO1.setUserName(userName);
        userDO1.setNickName(userName);
        userDO1.setPwd(md5Pwd);
        userDAO.insert(userDO1);


        User user = userDO1.toModel();
        result.setData(user);

        result.setSuccess(true);

        // 新用户注册成功后，存入缓存
        redisTemplate.opsForValue().set(userName, userDO1);

        return result;
    }

    @Override
    public Result<User> login(String userName, String pwd) {

        Result<User> result = new Result<>();

        if (StringUtils.isEmpty(userName)) {
            result.setCode("600");
            result.setMessage("用户名不能为空");
            return result;
        }
        if (StringUtils.isEmpty(pwd)) {
            result.setCode("601");
            result.setMessage("密码不能为空");
        }


        // 使用 redis 优化查询
        UserDO userDO = (UserDO) redisTemplate.opsForValue().get(userName);
        if (userDO == null) {
            userDO = userDAO.findByUserName(userName);
        }

        if (userDO == null) {
            result.setCode("602");
            result.setMessage("用户名不存在");
            return result;
        }

        // 密码加自定义盐值，确保密码安全
        String saltPwd = pwd + "_ykd2050";
        // 生成md5值，并转为大写字母
        String md5Pwd = DigestUtils.md5Hex(saltPwd).toUpperCase();

        if (!userDO.getPwd().equals(md5Pwd)) {
            result.setCode("603");
            result.setMessage("密码不正确");
            return result;
        }

        User user = userDO.toModel();

        result.setData(user);
        result.setSuccess(true);

        return result;
    }
}
