package dto;

import java.io.Serializable;

public class OrderDetail implements Serializable {

    private int orderDetailId;
    private String name;
    private String description;
    private double price;
    private String orderDate;
    private String orderEndDate;
    private String orderStartDate;
    private boolean active;
    private int orderId;
    private int itemId;
    private String itemType;
    private String image;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Constructors, getters, and setters
    public OrderDetail() {
    }

    public OrderDetail(int orderDetailId, String name, String description, double price, String orderDate, boolean active, int orderId, int itemId, String itemType,String orderStartDate, String orderEndDate) {
        this.orderDetailId = orderDetailId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.orderDate = orderDate;
        this.active = active;
        this.orderId = orderId;
        this.itemId = itemId;
        this.itemType = itemType;
        this.orderStartDate = orderStartDate;
        this.orderEndDate = orderEndDate;
    }
    
    public OrderDetail(int orderDetailId, String name, String description, double price, String orderDate,String orderStartDate, String orderEndDate) {
        this.orderDetailId = orderDetailId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.orderDate = orderDate;
        this.orderStartDate = orderStartDate;
        this.orderEndDate = orderEndDate;
    }

    public OrderDetail(int orderDetailId, String name, String description, double price, String orderDate, int itemId, String itemType) {
        this.orderDetailId = orderDetailId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.orderDate = orderDate;
        this.itemId = itemId;
        this.itemType = itemType;
    }

    public OrderDetail(int orderDetailId, String name, String description, double price, String orderDate, int orderId, int itemId, String itemType,String orderStartDate, String orderEndDate) {
        this.orderDetailId = orderDetailId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.orderDate = orderDate;
        this.orderId = orderId;
        this.itemId = itemId;
        this.itemType = itemType;
        this.orderStartDate = orderStartDate;
        this.orderEndDate = orderEndDate;
    }

    public OrderDetail(int orderDetailId, String name, String description, double price, String orderDate) {
        this.orderDetailId = orderDetailId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.orderDate = orderDate;
    }

    public OrderDetail(int orderDetailId, String name, String description, double price, String orderDate, boolean active, int orderId, int itemId, String itemType, String image) {
        this.orderDetailId = orderDetailId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.orderDate = orderDate;
        this.active = active;
        this.orderId = orderId;
        this.itemId = itemId;
        this.itemType = itemType;
        this.image = image;
    }

    public OrderDetail(String orderDate, int orderId, int itemId, String itemType) {
        this.orderDate = orderDate;
        this.orderId = orderId;
        this.itemId = itemId;
        this.itemType = itemType;
    }

    public String getOrderEndDate() {
        return orderEndDate;
    }

    public void setOrderEndDate(String orderEndDate) {
        this.orderEndDate = orderEndDate;
    }

    public String getOrderStartDate() {
        return orderStartDate;
    }

    public void setOrderStartDate(String orderStartDate) {
        this.orderStartDate = orderStartDate;
    }
    
    
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
