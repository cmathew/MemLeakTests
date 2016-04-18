package com.christophermathew.memleaktests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public interface ParserInterface<T> {
    T parse(JSONObject json) throws JSONException;

    ArrayList<T> parse(JSONArray json) throws JSONException;
}
