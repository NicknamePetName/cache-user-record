package com.example.demo.dao;

import com.example.demo.dataobject.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;


@Mapper
public interface UserDAO {

    int insert(UserDO userDO);

    int update(UserDO userDO);

    int delete(@Param("id") long id);

    List<UserDO> findAll();

    UserDO findByUserName(@Param("userName") String userName);

    List<UserDO> query(@Param("keyWord") String keyWord);

    List<UserDO> search(@Param("keyWord") String keyWord,
                       @Param("startTime") LocalDateTime startTime,
                       @Param("endTime") LocalDateTime endTime);

    int batchAdd(@Param("userDOS") List<UserDO> userDOS);

    List<UserDO> findByIds(@Param("ids") List<Long> ids);

    UserDO findById(@Param("id") Long id);
}
