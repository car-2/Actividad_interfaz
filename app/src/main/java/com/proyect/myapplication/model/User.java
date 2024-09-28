package com.proyect.myapplication.model;

public class User {

    //Declaración de Variables.
    String NameUser, AgeUser, CellUser, Location, favoriteStation, ActivityFavorite;

    //Medoto Contructor Vacio.
    public User(){}


    //Metodo Contructor LLeno.
    public User(String nameUser, String ageUser, String cellUser, String location, String favoritestation, String activityfavorite) {
        NameUser = nameUser;
        AgeUser = ageUser;
        CellUser = cellUser;
        Location = location;
        favoriteStation = favoritestation;
        ActivityFavorite = activityfavorite;
    }

    //Getters and Setters
    public String getNameUser() {
        return NameUser;
    }

    public void setNameUser(String nameUser) {
        NameUser = nameUser;
    }

    public String getAgeUser() {
        return AgeUser;
    }

    public void setAge(String age) {
        AgeUser = age;
    }

    public String getCellUser() {
        return CellUser;
    }

    public void setCellUser(String cellUser) {
        CellUser = cellUser;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getFavoriteStation() {
        return favoriteStation;
    }

    public void setFavoriteStation(String favoriteStation) {
        this.favoriteStation = favoriteStation;
    }

    public String getActivityFavorite() {
        return ActivityFavorite;
    }

    public void setActivityFavorite(String activityFavorite) {
        this.ActivityFavorite = activityFavorite;
    }
}

