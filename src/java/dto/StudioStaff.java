/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.io.Serializable;

/**
 *
 * @author ptd
 */
public class StudioStaff implements Serializable{
    private int studioStaffId;
    private int profileId;
    private boolean active;

    public StudioStaff() {
    }

    public StudioStaff(int studioStaffId, int profileId, boolean active) {
        this.studioStaffId = studioStaffId;
        this.profileId = profileId;
        this.active = active;
    }

    public int getStudioStaffId() {
        return studioStaffId;
    }

    public void setStudioStaffId(int studioStaffId) {
        this.studioStaffId = studioStaffId;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    
    
}
