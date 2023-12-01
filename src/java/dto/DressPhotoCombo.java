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
public class DressPhotoCombo implements Serializable{
    
    private int id;
    private String comboName;
    private String comboDescription;
    private int dressId;
    private int photoStudioId;
    private double price;
    private String image;
    private int stock;
    private boolean active;

    public DressPhotoCombo() {
    }

    public DressPhotoCombo(int id, String comboName, String comboDescription, int dressId, int photoStudioId, double price, String image, boolean active) {
        this.id = id;
        this.comboName = comboName;
        this.comboDescription = comboDescription;
        this.dressId = dressId;
        this.photoStudioId = photoStudioId;
        this.price = price;
        this.image = image;
        this.active = active;
    }

    public DressPhotoCombo(int id, String comboName, String comboDescription, int dressId, int photoStudioId, double price, String image, int stock, boolean active) {
        this.id = id;
        this.comboName = comboName;
        this.comboDescription = comboDescription;
        this.dressId = dressId;
        this.photoStudioId = photoStudioId;
        this.price = price;
        this.image = image;
        this.stock = stock;
        this.active = active;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
        

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

   

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComboName() {
        return comboName;
    }

    public void setComboName(String comboName) {
        this.comboName = comboName;
    }

    public String getComboDescription() {
        return comboDescription;
    }

    public void setComboDescription(String comboDescription) {
        this.comboDescription = comboDescription;
    }

    public int getDressId() {
        return dressId;
    }

    public void setDressId(int dressId) {
        this.dressId = dressId;
    }

    public int getPhotoStudioId() {
        return photoStudioId;
    }

    public void setPhotoStudioId(int photoStudioId) {
        this.photoStudioId = photoStudioId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    
    
    
}
