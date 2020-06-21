package com.example.bkfoodcourt;

public class Row {
    private String orderID;
    private String userID;

    public Row(String orderID, String userID) {
        this.orderID = orderID;
        this.userID = userID;
    }

    public String getOrderID() {
        return orderID;
    }

    public String getUserID() {
        return userID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
