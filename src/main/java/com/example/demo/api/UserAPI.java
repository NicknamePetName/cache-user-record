package com.example.demo.api;

import com.example.demo.model.Result;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserAPI {

    @Autowired
    private UserService userService;
    @ResponseBody
    @PostMapping("/api/user/reg")
    public Result<User> register(@RequestBody User user) {
        return  userService.register(user.getUserName(),user.getPwd());
    }

}
