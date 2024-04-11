package com.example.demo.dao;

import com.example.demo.dataobject.PersonalRecordDO;
import com.example.demo.model.PersonalRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PersonalRecordDAO {
    int insert(PersonalRecordDO personalRecordDO);

    int update(PersonalRecordDO personalRecordDO);

    int delete(@Param("id") Long id);

    List<PersonalRecordDO> findAll();

    PersonalRecord findByUserId(@Param("userId") Long userId);

}
