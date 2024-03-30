package com.example.demo.dao;

import com.example.demo.dataobject.CommentDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentDAO {

    int insert(CommentDO commentDO);

    int update(CommentDO commentDO);

    int delete(@Param("id") long id);

    List<CommentDO> findAll();

    List<CommentDO> findByRefId(@Param("refId") String refId);

    int batchAdd(@Param("commentDOS") List<CommentDO> commentDOS);

    List<CommentDO> findByIds(@Param("ids") List<Long> ids);
}
