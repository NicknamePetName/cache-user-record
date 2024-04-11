package com.example.demo.service.impl;

import com.example.demo.model.PersonalRecord;
import com.example.demo.service.PersonalRecordService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PersonalRecordServiceImpl implements PersonalRecordService {
    private static final String KEY = "integralRankLocal";

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Override
    public int save(PersonalRecord personalRecord) {
        Boolean result = redisTemplate.opsForZSet().add(KEY, personalRecord, personalRecord.getPoints());
        return result ? 1 : 0;
    }

    @Override
    public void initDefaultData(Long userId) {
        //创建用户时 默认会创建个人战绩
        PersonalRecord personalRecord = new PersonalRecord();
        personalRecord.setWinTimes(0);
        personalRecord.setTopTenTimes(0);
        personalRecord.setEliminateNum(0);
        personalRecord.setPlayNum(0);
        personalRecord.setPoints(1200);
        personalRecord.setKd(0d);
        personalRecord.setUserId(userId);

        save(personalRecord);
    }

    @Override
    public List<PersonalRecord> queryLimit(Long limitNum) {

        Set<TypedTuple<PersonalRecord>> datas = redisTemplate.opsForZSet().reverseRangeWithScores(KEY, 0, limitNum);

        if (CollectionUtils.isEmpty(datas)) {
            return Collections.EMPTY_LIST;
        }

        List<PersonalRecord> prs = datas.stream().map(data -> {
            // 存入的对象
            PersonalRecord pr = data.getValue();
            pr.setUser(userService.findById(pr.getUserId()));
            // 对应的分数
            Double score = data.getScore();
            return pr;
        }).collect(Collectors.toList());

        return prs;
    }

}
