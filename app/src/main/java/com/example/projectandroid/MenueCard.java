package com.example.projectandroid;

public class MenueCard {
    private String name;
    private int image;


    public MenueCard(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public MenueCard() {
    }

    public static final MenueCard[] menue ={
            new MenueCard("Home",R.drawable.home),
            new MenueCard("Search",R.drawable.search),
            new MenueCard("View Rooms" ,R.drawable.baseline_view_headline_24),
            new MenueCard("View Food",R.drawable.viewfood),
            new MenueCard("Logout",R.drawable.logout)
    };

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(int image) {
        this.image = image;
    }

}
