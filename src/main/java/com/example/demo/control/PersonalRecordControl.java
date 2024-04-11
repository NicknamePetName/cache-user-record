package com.example.demo.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.demo.config.RedisTemplateConfig;
import com.example.demo.model.PersonalRecord;
import com.example.demo.model.Result;
import com.example.demo.service.PersonalRecordService;
import jakarta.annotation.PostConstruct;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
@RequestMapping("/record")
public class PersonalRecordControl {

    private static final TypeReference<List<PersonalRecord>> t_pr = new TypeReference<List<PersonalRecord>>() {
    };

    @Autowired
    private ResourceLoader loader;

    @Autowired
    private PersonalRecordService personalRecordService;

    @Autowired
    private RedisTemplateConfig redisTemplateConfig;

//    @PostConstruct
    public void init() {
        String content = getContent("classpath:datas/personal_record.json");
        List<PersonalRecord> prs = JSON.parseObject(content, t_pr);

        for (PersonalRecord prData : prs) {
            personalRecordService.save(prData);
        }
    }

    @GetMapping("/rank/all")
    @ResponseBody
    public Result<List<PersonalRecord>> queryAllRank() {

        Result<List<PersonalRecord>> result = new Result<>();
        result.setData(personalRecordService.queryLimit(-1L));
        result.setSuccess(true);
        return result;
    }

    @GetMapping("/rank/limit")
    @ResponseBody
    public Result<List<PersonalRecord>> queryLimitAllRank(@RequestParam(value = "limit") Long limit) {

        Result<List<PersonalRecord>> result = new Result<>();
        result.setData(personalRecordService.queryLimit(limit-1));
        result.setSuccess(true);
        return result;
    }

    private String getContent(String name) {
        try {
            InputStream in = loader.getResource(name).getInputStream();
            return IOUtils.toString(in, "utf-8");
        } catch (IOException e) {
            return null;
        }
    }
}
