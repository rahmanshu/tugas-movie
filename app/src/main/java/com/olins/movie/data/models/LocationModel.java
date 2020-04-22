package com.olins.movie.data.models;

import java.io.Serializable;



public class LocationModel implements Serializable {

    private static final long serialVersionUID = 5837758271563603904L;

    String id;
    String latitude;
    String longitude;
    String locationTitle;
    String locationAddress;
    String locationCompany;
    String username;
    String email;
    String phone;

    public LocationModel(){

    }
    public LocationModel(
            String id,
            String latitude,
            String longitude,
            String locationTitle,
            String locationAddress,
            String locationCompany,
            String username,
            String email,
            String phone){
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationTitle = locationTitle;
        this.locationAddress = locationAddress;
        this.locationCompany = locationCompany;
        this.username = username;
        this.email = email;
        this.phone = phone;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLocationTitle() {
        return locationTitle;
    }

    public void setLocationTitle(String locationTitle) {
        this.locationTitle = locationTitle;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public String getLocationCompany() {
        return locationCompany;
    }

    public void setLocationCompany(String locationCompany) {
        this.locationCompany = locationCompany;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
