package com.example.demo.service;


import com.example.demo.model.PersonalRecord;

import java.util.List;

public interface PersonalRecordService {
    /**
     * 添加或者修改个人战绩
     *
     * @param personalRecord 个人战绩
     * @return PersonalRecord
     */
    int save(PersonalRecord personalRecord);

    /**
     * 初始化用户的默认的个人战绩
     *
     * @param userId 用户主键 id
     */
    void initDefaultData(Long userId);

    /**
     * 查询前 x 名个人战绩数据。<br>按积分倒序排序
     * @param limitNum 指定名次，查询所有的则输入 0 或 null
     * @return
     */
    List<PersonalRecord> queryLimit(Long limitNum);
}
