package com.android.anton.testapp.classes;

/**
 * Created by Administrator on 8/22/2016.
 */
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 8/22/2016.
 */
public class UserInfo {

    private Context mContext;

    public UserInfo (Context context) {
        this.mContext = context;
    }

    public String getName() {
        String name = mContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
                .getString("Name", "");
        return name;
    }

    public String getEmail() {
        String email = mContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
                .getString("Email", "");
        return email;
    }

    public String getAddress1() {
        String address1 = mContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
                .getString("Address1", "");
        return address1;
    }

    public String getAddress2() {
        String address2 = mContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
                .getString("Address2", "");
        return address2;
    }

    public String getTown() {
        String town = mContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
                .getString("Town", "");
        return town;
    }

    public String getPostcode() {
        String postcode = mContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
                .getString("Postcode", "");
        return postcode;
    }

    public String getTelephone() {
        String telephone = mContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE).getString("Telephone", "");
        return telephone;
    }

    public void setName(String name) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
                .edit();
        editor.putString("Name", name);
        editor.commit();
    }

    public void setEmail(String email) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
                .edit();
        editor.putString("Email", email);
        editor.commit();
    }

    public void setAddress1(String address1) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
                .edit();
        editor.putString("Address1", address1);
        editor.commit();
    }

    public void setAddress2(String address2) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
                .edit();
        editor.putString("Address2", address2);
        editor.commit();
    }

    public void setTown(String town) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
                .edit();
        editor.putString("Town", town);
        editor.commit();
    }

    public void setPostcode(String postcode) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
                .edit();
        editor.putString("Postcode", postcode);
        editor.commit();
    }

    public void setTelephone(String telephone) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
                .edit();
        editor.putString("Telephone", telephone);
        editor.commit();
    }

}
