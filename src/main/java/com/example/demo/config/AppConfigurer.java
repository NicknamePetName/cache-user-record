package com.example.demo.config;

import com.example.demo.interceptor.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器 和 白名单
 *
 * @author 亦-Nickname
 */
@Configuration
public class AppConfigurer implements WebMvcConfigurer {

    @Autowired
    private UserInterceptor userInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // 仅演示，设置所有 url 都拦截
        registry.addInterceptor(userInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/user/reg")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/api/user/reg")
                .excludePathPatterns("/api/user/login")
                .excludePathPatterns("/css/**")
                .excludePathPatterns("/js/**")
                .excludePathPatterns("/error");
    }
}
