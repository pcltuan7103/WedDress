/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;

/**
 *
 * @author ptd
 */
public class RejectOrder implements Serializable{
    
    private int rejectId;
    private String status;
    private String name;
    private String description;
    private double price;
    private String orderDate;
    private String orderStartDate;
    private String orderEndDate;
    private int profileId;
    private String itemType;
    private int itemId;

    public RejectOrder() {
    }

    public RejectOrder(int rejectId, String status, String name, String description, double price, String orderDate, String orderStartDate, String orderEndDate, int profileId, String itemType, int itemId) {
        this.rejectId = rejectId;
        this.status = status;
        this.name = name;
        this.description = description;
        this.price = price;
        this.orderDate = orderDate;
        this.orderStartDate = orderStartDate;
        this.orderEndDate = orderEndDate;
        this.profileId = profileId;
        this.itemType = itemType;
        this.itemId = itemId;
    }

     public RejectOrder(String status, String name, String description, double price, String orderDate, String orderStartDate, String orderEndDate, int profileId, String itemType, int itemId) {
        this.status = status;
        this.name = name;
        this.description = description;
        this.price = price;
        this.orderDate = orderDate;
        this.orderStartDate = orderStartDate;
        this.orderEndDate = orderEndDate;
        this.profileId = profileId;
        this.itemType = itemType;
        this.itemId = itemId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
    

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
   
   


    public int getRejectId() {
        return rejectId;
    }

    public void setRejectId(int rejectId) {
        this.rejectId = rejectId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStartDate() {
        return orderStartDate;
    }

    public void setOrderStartDate(String orderStartDate) {
        this.orderStartDate = orderStartDate;
    }

    public String getOrderEndDate() {
        return orderEndDate;
    }

    public void setOrderEndDate(String orderEndDate) {
        this.orderEndDate = orderEndDate;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    
    
    
}
