package com.example.demo.service.impl;

import com.example.demo.dao.UserDAO;
import com.example.demo.dataobject.UserDO;
import com.example.demo.model.Result;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;
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

        UserDO userDO = userDAO.findByUserName(userName);

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

        result.setSuccess(true);

        User user = new User();
        user.setId(userDO1.getId());
        user.setUserName(userDO1.getUserName());
        user.setNickName(userDO1.getNickName());

        result.setData(user);

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

        UserDO userDO = userDAO.findByUserName(userName);

        if (userDO == null) {
            result.setCode("602");
            result.setMessage("用户名不存在");
            return result;
        }

        String saltPwd = pwd + "_ykd2050";
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
