package com.example.projectandroid;

public class Resfood {
    private int id;
    private String username;
    private String name;
    private String state;
    private String des;
    private double price;

    public Resfood(int id, String username, String name, String state, double price,String des) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.state = state;
        this.price = price;
        this.des=des;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
