package com.prototype.microservice.commons.json;

public class UserAuthenticationRequestJson extends RequestJson {

    private static final long serialVersionUID = 3440824579529096549L;
    private String userName;
    private String password;

    public UserAuthenticationRequestJson() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
