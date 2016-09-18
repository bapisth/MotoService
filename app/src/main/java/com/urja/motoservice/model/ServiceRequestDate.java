package com.urja.motoservice.model;

/**
 * Created by BAPI1 on 9/18/2016.
 */
public class ServiceRequestDate {
    private Long date;
    private Long day;
    private Long hours;
    private Long minutes;
    private Long month;
    private Long seconds;
    private Long time;
    private Long timezoneOffset;
    private Long year;

    public ServiceRequestDate() {
    }

    public ServiceRequestDate(Long date, Long day, Long hours, Long minutes, Long month, Long seconds, Long time, Long timezoneOffset, Long year) {
        this.date = date;
        this.day = day;
        this.hours = hours;
        this.minutes = minutes;
        this.month = month;
        this.seconds = seconds;
        this.time = time;
        this.timezoneOffset = timezoneOffset;
        this.year = year;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Long getDay() {
        return day;
    }

    public void setDay(Long day) {
        this.day = day;
    }

    public Long getHours() {
        return hours;
    }

    public void setHours(Long hours) {
        this.hours = hours;
    }

    public Long getMinutes() {
        return minutes;
    }

    public void setMinutes(Long minutes) {
        this.minutes = minutes;
    }

    public Long getMonth() {
        return month;
    }

    public void setMonth(Long month) {
        this.month = month;
    }

    public Long getSeconds() {
        return seconds;
    }

    public void setSeconds(Long seconds) {
        this.seconds = seconds;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getTimezoneOffset() {
        return timezoneOffset;
    }

    public void setTimezoneOffset(Long timezoneOffset) {
        this.timezoneOffset = timezoneOffset;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }
}
