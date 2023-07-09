package com.example.projectandroid;

public class Reservation {
    private int id;
    private int roomid;
    private String imageUrl;
    private String username;
    private double price;
    private String state;

    public Reservation(int id,String imageUrl, String username, double price, String state ,int roomid) {
        this.imageUrl = imageUrl;
        this.username = username;
        this.price = price;
        this.state = state;
        this.id = id;
        this.roomid = roomid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUsername() {
        return username;
    }

    public double getPrice() {
        return price;
    }

    public String getState() {
        return state;
    }

    public int getId() {
        return id;
    }

    public int getRoomid() {
        return roomid;
    }

    public void setState(String state) {
        this.state = state;
    }


}
