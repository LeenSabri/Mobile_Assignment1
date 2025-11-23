package com.example.assignment1;

public class Trip {
    private String location;
    private int imageID;
    private String starting_point;
    private String coordinator;
    private String date;
    private int allowed_num;
    private int registered_num;
    private int remaining_num;
    private double price;
    private String overview;


    public Trip(String location, String starting_point, String coordinator, String date, int allowed_num, int registered_num, int remaining_num, double price) {
        this.location = location;
        this.starting_point = starting_point;
        this.coordinator = coordinator;
        this.date = date;
        this.allowed_num = allowed_num;
        this.registered_num = registered_num;
        this.remaining_num = remaining_num;
        this.price = price;
    }

    public Trip(String location, int imageID) {
        this.location = location;
        this.imageID = imageID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getRemaining_num() {
        return remaining_num;
    }

    public void setRemaining_num(int remaining_num) {
        this.remaining_num = remaining_num;
    }

    public int getRegistered_num() {
        return registered_num;
    }

    public void setRegistered_num(int registered_num) {
        this.registered_num = registered_num;
    }

    public int getAllowed_num() {
        return allowed_num;
    }

    public void setAllowed_num(int allowed_num) {
        this.allowed_num = allowed_num;
    }

    public String getCoordinator() {
        return coordinator;
    }

    public void setCoordinator(String coordinator) {
        this.coordinator = coordinator;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStarting_point() {
        return starting_point;
    }

    public void setStarting_point(String starting_point) {
        this.starting_point = starting_point;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }
}
