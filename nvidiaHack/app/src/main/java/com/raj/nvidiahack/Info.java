package com.raj.nvidiahack;

/**
 * Created by RAJ on 29-01-2018.
 */


public class Info {

    public String onDate, lateDate, onTime, lateTime;
    public Integer lateAttendanceCount, onTimeAttendanceCount;
    public Integer imageCount;

    Info() {

    }

    public Info(Integer lateAttendanceCount, String lateDate, String lateTime, Integer onTimeAttendanceCount, String onDate, String onTime) {
        this.lateAttendanceCount = lateAttendanceCount;
        this.lateDate = lateDate;
        this.lateTime = lateTime;

        this.onTimeAttendanceCount = onTimeAttendanceCount;
        this.onDate = onDate;
        this.onTime = onTime;

    }


}
