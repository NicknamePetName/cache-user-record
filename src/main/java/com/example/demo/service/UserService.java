package com.example.demo.service;

import com.example.demo.model.Result;
import com.example.demo.model.User;

public interface UserService {
    /**
     * 注册用户
     *
     * @param userName
     * @param pwd
     * @return
     */

    public Result<User> register(String userName, String pwd);


    /**
     * 登录
     *
     * @param userName
     * @param pwd
     * @return
     */

    public Result<User> login(String userName, String pwd);
}
