package com.example.demo.api;

import com.example.demo.model.Result;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserAPI {

    @Autowired
    private UserService userService;
    @PostMapping("/api/user/reg")
    public Result<User> register(@RequestParam String userName, @RequestParam String pwd) {
        return  userService.register(userName,pwd);
    }

    @PostMapping("/api/user/login")
    public Result<User> login(@RequestParam String userName, @RequestParam String pwd, HttpServletRequest request) {
        Result<User> result = userService.login(userName, pwd);
        if (result.isSuccess()) {
            request.getSession().setAttribute("userId",result.getData().getId());
        }
        return result;
    }

    @GetMapping("/api/user/logout")
    public Result logout(HttpServletRequest request){

        Result result = new Result();

        request.getSession().removeAttribute("userId");

        result.setSuccess(true);

        return result;
    };

}
