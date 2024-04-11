package com.example.demo.dao;

import com.example.demo.dataobject.PersonalRecordDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PersonalRecordDAO {
    int insert(PersonalRecordDO personalRecordDO);

    int update(PersonalRecordDO personalRecordDO);

    int delete(@Param("id") Long id);

    List<PersonalRecordDO> findAll();

    PersonalRecordDO findByUserId(@Param("userId") Long userId);

}
