package com.example.kashif.abesgram.UsersListActivities;

/**
 * Created by kashif on 29/10/17.
 */

public class AllUserListModel {

    private String UserName;
    private String UserImageUrl;
    private String UserUniqueId;

    public AllUserListModel(String user_name, String user_image_Url, String user_uniqueId){
        this.UserName = user_name;
        this.UserImageUrl = user_image_Url;
        this.UserUniqueId = user_uniqueId;
    }

    public String getUserName(){
        return UserName;
    }
    public String getUserImageUrl(){
        return UserImageUrl;
    }
    public String getUserUniqueId(){
        return UserUniqueId;
    }
}
