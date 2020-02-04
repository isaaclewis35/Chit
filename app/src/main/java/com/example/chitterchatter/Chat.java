package com.example.chitterchatter;


//class representing a chat in code form
//mostly just a value store with getters and setters
public class Chat {

    private String mContent;
    private int mLikes;
    private int mDislikes;
    private String _id;
    private String Longitude;
    private String Latitude;
    private String date;
    private String user;

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public int getmLikes() {
        return mLikes;
    }

    public void setmLikes(int mLikes) {
        this.mLikes = mLikes;
    }

    public int getmDislikes() {
        return mDislikes;
    }

    public void setmDislikes(int mDislikes) {
        this.mDislikes = mDislikes;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
