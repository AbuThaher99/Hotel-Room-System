package com.example.projectandroid;

import java.util.ArrayList;

public class Room {
    private int id;

    private String iamge;
    private int numfloor;
    private double price;
    private int type;
    private String state;
  ArrayList<Room> rooms;


    public Room() {
        rooms = new ArrayList<>();
    }



    public Room(int id, String iamge, int numfloor, double price, int type, String state) {
        this.id = id;
        this.iamge = iamge;
        this.numfloor = numfloor;
        this.price = price;
        this.type = type;
        this.state = state;
    }


    public Room(String iamge, int numfloor, double price, int type) {
        this.iamge = iamge;
        this.numfloor = numfloor;
        this.price = price;
        this.type = type;
    }



// getter and setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getIamge() {
        return iamge;
    }

    public void setIamge(String iamge) {
        this.iamge = iamge;
    }


    public int getNumfloor() {
        return numfloor;
    }

    public void setNumfloor(int numfloor) {
        this.numfloor = numfloor;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
