package com.christophermathew.memleaktests;

public class AppUserState {
    public final static String DRIVER_ID_KEY  = "driver_id";
    public final static String AUTH_TOKEN_KEY = "apikey";
    public final static String CONVEYANCE_TYPE_KEY = "conveyance_type";

    public int userId;
    public String authToken;
    public String conveyanceType;

    public AppUserState(int id, String token, String conveyance) {
        this.userId = id;
        this.authToken = token;
        this.conveyanceType = conveyance;
    }

    public AppUserState(int id, String token) {
        this(id, token, "automobile");
    }
}
