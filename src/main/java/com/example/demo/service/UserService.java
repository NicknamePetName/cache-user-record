package com.example.demo.service;

import com.example.demo.model.Result;
import com.example.demo.model.User;

public interface UserService {

    String KEY = "integralRankUserLocal";
    String KEY_USER = "integralRankUserModelLocal";
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

    /**
     * 根据 id 查询用户模型实例
     *
     * @param id
     * @return
     */
    public User findById(Long id);
}
