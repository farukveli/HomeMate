package com.example.homemate;

public class LocationClass {

    String userID, iconURI;
    double latitude, longitude;

    public LocationClass(){
    }

    public LocationClass(String userID, String iconURI, double latitude, double longitude) {
        this.userID = userID;
        this.iconURI = iconURI;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getIconURI() {
        return iconURI;
    }

    public void setIconURI(String iconURI) {
        this.iconURI = iconURI;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
