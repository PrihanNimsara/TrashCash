package com.groupite.trashcash.models;

import java.io.Serializable;

public class BuyerModel implements Serializable {
    User user;


    public BuyerModel() {
    }

    public BuyerModel(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
