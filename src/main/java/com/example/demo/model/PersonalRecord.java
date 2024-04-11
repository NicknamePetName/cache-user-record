package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.io.Serializable;
import java.time.LocalDateTime;

public class PersonalRecord implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户
     */
    private Long userId;

    /**
     * 积分
     */
    private Integer points;

    /**
     * 场数
     */
    private Integer playNum;


    /**
     * 获胜数
     */
    private Integer winTimes;

    /**
     * 前十数
     */
    private Integer topTenTimes;

    /**
     * 淘汰/被淘汰
     */
    private Double kd;

    /**
     * 淘汰数
     */
    private Integer eliminateNum;

    /**
     * 用户完整信息
     */
    private User user;

    //向redis中存入对象，需要将对象序列化，如果某个字段为LocalDateTime类型，就会出现报错,对应字段加入注解:
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    // STRING：表示序列化和反序列化时都使用字符串格式。
    //  NUMBER（默认值）：表示序列化和反序列化时都使用数字格式（通常是时间戳）。
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime gmtCreated;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime gmtModified;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getPlayNum() {
        return playNum;
    }

    public void setPlayNum(Integer playNum) {
        this.playNum = playNum;
    }

    public Integer getWinTimes() {
        return winTimes;
    }

    public void setWinTimes(Integer winTimes) {
        this.winTimes = winTimes;
    }

    public Integer getTopTenTimes() {
        return topTenTimes;
    }

    public void setTopTenTimes(Integer topTenTimes) {
        this.topTenTimes = topTenTimes;
    }

    public Double getKd() {
        return kd;
    }

    public void setKd(Double kd) {
        this.kd = kd;
    }

    public Integer getEliminateNum() {
        return eliminateNum;
    }

    public void setEliminateNum(Integer eliminateNum) {
        this.eliminateNum = eliminateNum;
    }

    public LocalDateTime getGmtCreated() {
        return gmtCreated;
    }

    public void setGmtCreated(LocalDateTime gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    public LocalDateTime getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }
}
