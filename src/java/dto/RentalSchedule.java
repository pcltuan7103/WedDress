package dto;

import javax.management.relation.Role;
import java.io.Serializable;
import java.util.Date;

public class RentalSchedule implements Serializable {

    private int scheduleId;
    private Role user;
    private RentalProduct product;
    private Date scheduleDate;
    private boolean active;

    // Constructors, getters, and setters
    public RentalSchedule() {
    }

    public RentalSchedule(int scheduleId, Role user, RentalProduct product, Date scheduleDate, boolean active) {
        this.scheduleId = scheduleId;
        this.user = user;
        this.product = product;
        this.scheduleDate = scheduleDate;
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Role getUser() {
        return user;
    }

    public void setUser(Role user) {
        this.user = user;
    }

    public RentalProduct getProduct() {
        return product;
    }

    public void setProduct(RentalProduct product) {
        this.product = product;
    }

    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }
}
