package com.example.demo.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.demo.dao.PersonalRecordDAO;
import com.example.demo.dataobject.PersonalRecordDO;
import com.example.demo.model.Paging;
import com.example.demo.model.PersonalRecord;
import com.example.demo.model.Result;
import com.example.demo.service.PersonalRecordService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jakarta.annotation.PostConstruct;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
@RequestMapping("/record")
public class PersonalRecordControl {

    private static final TypeReference<List<PersonalRecordDO>> t_pr = new TypeReference<List<PersonalRecordDO>>() { };

    @Autowired
    private PersonalRecordDAO personalRecordDAO;

    @Autowired
    private ResourceLoader loader;

    @Autowired
    private PersonalRecordService personalRecordService;

    @Autowired
    private RedisTemplate redisTemplate;

//    @PostConstruct
    public void init() {
        String content = getContent("classpath:datas/personal_record.json");
        List<PersonalRecordDO> prs = JSON.parseObject(content, t_pr);

        prs.forEach(prData -> {
            personalRecordService.save(prData.toModel());
            personalRecordDAO.insert(prData);
        });
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


    @ResponseBody
    @GetMapping("/personal/findAll")
    public Result<Paging<PersonalRecordDO>> getAll(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "15") Integer pageSize) {

        Result<Paging<PersonalRecordDO>> result = new Result<>();

        Page<PersonalRecordDO> page = PageHelper.startPage(pageNum, pageSize).doSelectPage(() -> personalRecordDAO.findAll());
        result.setData(new Paging<>(page.getPageNum(),page.getPageSize(),page.getPages(),page.getTotal(),page.getResult()));
        result.setSuccess(true);
        return result;
    }

    @ResponseBody
    @GetMapping("/personal/findByUserId")
    public PersonalRecordDO findByUserId(@RequestParam Long userId) {
        return personalRecordDAO.findByUserId(userId);
    }

    @ResponseBody
    @PostMapping("/personal/insert")
    public PersonalRecordDO insert(@RequestBody PersonalRecordDO personalRecordDO) {
        personalRecordDAO.insert(personalRecordDO);
        return personalRecordDO;
    }

    @ResponseBody
    @PostMapping("/personal/update")
    public PersonalRecordDO update(@RequestBody PersonalRecordDO personalRecordDO) {
        personalRecordDAO.update(personalRecordDO);
        return personalRecordDO;
    }

    @ResponseBody
    @GetMapping("/personal/del")
    public boolean delete(@RequestParam Long id) {
       int result = personalRecordDAO.delete(id);
       return result > 0;
    }

}
