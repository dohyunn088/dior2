package com.kbu.busstopjava.firebase;

public class user {
    public String userName;
    public String userNumber;
    public String userPassword;

    public user() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public user(String userName, String userNumber, String userPassword) {
        this.userName = userName;
        this.userNumber = userNumber;
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }
    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public String toString() {
        return "user{" +
                "userPassword ='" + userPassword + '\'' +
                ", userNumber ='" + userNumber + '\'' +
                ", userName ='" + userName + '\'' +
                '}';
    }
}

