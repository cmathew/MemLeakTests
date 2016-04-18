package com.christophermathew.memleaktests;

import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import rx.functions.Func1;

public class UserHandler implements Func1<JSONObject, Driver> {
    @Nullable
    @Override
    public Driver call(JSONObject driverJson){
        try {
            DriverParser driverParser = new DriverParser();
            return driverParser.parse(driverJson);
        } catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
