package com.android.anton.testapp.classes;

/**
 * Created by Administrator on 8/22/2016.
 */
public class UserInfo {

    private String name;
    private String email;
    private String address1;
    private String address2;
    private String town;
    private String postcode;
    private String telephone;


    public UserInfo (String name, String email, String telephone) {
        this.name = name;
        this.email = email;
        this.telephone = telephone;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getTown() {
        return town;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

}
