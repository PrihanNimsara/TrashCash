package com.groupite.trashcash.models;

import java.io.Serializable;

public class Metal implements Serializable {
    private String id;

    private String userId;
    private String userType;
    private String priceForKg;


    public Metal() {
    }

    public Metal(String id, String userId, String userType, String priceForKg) {
        this.id = id;
        this.userId = userId;
        this.userType = userType;
        this.priceForKg = priceForKg;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPriceForKg() {
        return priceForKg;
    }

    public void setPriceForKg(String priceForKg) {
        this.priceForKg = priceForKg;
    }


}
