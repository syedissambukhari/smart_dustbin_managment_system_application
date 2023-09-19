package com.app.cattlemanagement.models;

public class User extends Super {
    String id, firstName, lastName, desc,
            phone, type, userType, status;

    public User() {
    }

    public User(String id, String firstName, String lastName, String desc, String phone, String type, String status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.desc = desc;
        this.phone = phone;
        this.type = type;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStrEtFirstName() {
        return firstName;
    }

    public void setStrEtFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getStrEtLastName() {
        return lastName;
    }

    public void setStrEtLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStrEtClassroom() {
        return desc;
    }

    public void setStrEtClassroom(String desc) {
        this.desc = desc;
    }

    public String getStrEtPhone() {
        return phone;
    }

    public void setStrEtPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVerStatus() {
        return status;
    }

    public void setVerStatus(String status) {
        this.status = status;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
