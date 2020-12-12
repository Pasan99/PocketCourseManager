package com.triangle.pocketcoursemanager.models;

import java.io.Serializable;
import java.sql.Date;

public class Course implements Serializable {
    private long id;
    private String name;
    private String code;
    private String description;
    private String duration;
    private float fee;
    private String startDate;
    private String endDate;
    private String days;
    private String dayStartTime;
    private String dayEndTime;

    private boolean isAttended = false;

    public Course(long id, String name, String code, String description, String duration, float fee, String startDate, String endDate, String days, String dayStartTime, String dayEndTime) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.description = description;
        this.duration = duration;
        this.fee = fee;
        this.startDate = startDate;
        this.endDate = endDate;
        this.days = days;
        this.dayStartTime = dayStartTime;
        this.dayEndTime = dayEndTime;
    }

    public boolean isAttended() {
        return isAttended;
    }

    public void setAttended(boolean attended) {
        isAttended = attended;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public float getFee() {
        return fee;
    }

    public void setFee(float fee) {
        this.fee = fee;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getDayStartTime() {
        return dayStartTime;
    }

    public void setDayStartTime(String dayStartTime) {
        this.dayStartTime = dayStartTime;
    }

    public String getDayEndTime() {
        return dayEndTime;
    }

    public void setDayEndTime(String dayEndTime) {
        this.dayEndTime = dayEndTime;
    }
}
