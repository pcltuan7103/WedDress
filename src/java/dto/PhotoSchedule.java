package dto;

import javax.management.relation.Role;
import java.io.Serializable;
import java.util.Date;

public class PhotoSchedule implements Serializable {
    private int scheduleId;
    private int userId;
    private int locationId;
    private int studioId;
    private String scheduleDate;
    private String status;
    private String timeRange;
    private String timeRangeReturn;

    // Constructors, getters, and setters

    public PhotoSchedule(){}

    public PhotoSchedule(int scheduleId, int userId, int locationId, int studioId, String scheduleDate, String status, String timeRange, String timeRangeReturn) {
        this.scheduleId = scheduleId;
        this.userId = userId;
        this.locationId = locationId;
        this.studioId = studioId;
        this.scheduleDate = scheduleDate;
        this.status = status;
        this.timeRange = timeRange;
        this.timeRangeReturn = timeRangeReturn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }

    public String getTimeRangeReturn() {
        return timeRangeReturn;
    }

    public void setTimeRangeReturn(String timeRangeReturn) {
        this.timeRangeReturn = timeRangeReturn;
    }

    
    

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getStudioId() {
        return studioId;
    }

    public void setStudioId(int studioId) {
        this.studioId = studioId;
    }

   

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

   
}
