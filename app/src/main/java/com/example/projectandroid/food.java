package com.example.projectandroid;

import java.util.ArrayList;

public class food {
    private int id;
    private String name;
    private String description;
    private double price;
    ArrayList<food> foods;
    public food(){
        foods=new ArrayList<>();
    }

    public food(int id, String name, String description, double price, ArrayList<food> foods) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.foods = foods;
    }

    public food(int id, String name, String description, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public ArrayList<food> getFoods() {
        return foods;
    }

    public void setFoods(ArrayList<food> foods) {
        this.foods = foods;
    }
}