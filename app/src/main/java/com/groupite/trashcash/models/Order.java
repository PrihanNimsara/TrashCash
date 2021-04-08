package com.groupite.trashcash.models;

public class Order {
    String id;

    String buyerId;
    String buyerName;
    String buyerEmail;
    String buyerPhone;
    String buyerAddress;

    String sellerId;
    String sellerName;
    String sellerEmail;
    String sellerPhone;
    String sellerAddress;

    String price;
    String weight;
    String type;
    String status;

    String buyerId_sellerId_type_status;

    public Order() {
    }

    public Order(String id, String buyerId, String buyerName, String buyerEmail, String buyerPhone, String buyerAddress, String sellerId, String sellerName, String sellerEmail, String sellerPhone, String sellerAddress, String price, String weight, String type, String status, String buyerId_sellerId_type_status) {
        this.id = id;
        this.buyerId = buyerId;
        this.buyerName = buyerName;
        this.buyerEmail = buyerEmail;
        this.buyerPhone = buyerPhone;
        this.buyerAddress = buyerAddress;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.sellerEmail = sellerEmail;
        this.sellerPhone = sellerPhone;
        this.sellerAddress = sellerAddress;
        this.price = price;
        this.weight = weight;
        this.type = type;
        this.status = status;
        this.buyerId_sellerId_type_status = buyerId_sellerId_type_status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBuyerId_sellerId_type_status() {
        return buyerId_sellerId_type_status;
    }

    public void setBuyerId_sellerId_type_status(String buyerId_sellerId_type_status) {
        this.buyerId_sellerId_type_status = buyerId_sellerId_type_status;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public String getBuyerPhone() {
        return buyerPhone;
    }

    public void setBuyerPhone(String buyerPhone) {
        this.buyerPhone = buyerPhone;
    }

    public String getBuyerAddress() {
        return buyerAddress;
    }

    public void setBuyerAddress(String buyerAddress) {
        this.buyerAddress = buyerAddress;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }

    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }

    public String getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = sellerAddress;
    }
}
