package dto;

import java.io.Serializable;

public class Cart implements Serializable {

    private int cartId;
    private int profileId;
    private int orderId;
    private boolean active;

    // Constructors, getters, and setters
    public Cart() {
    }

    public Cart(int cartId, int profileId, int orderId, boolean active) {
        this.cartId = cartId;
        this.profileId = profileId;
        this.orderId = orderId;
        this.active = active;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
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
