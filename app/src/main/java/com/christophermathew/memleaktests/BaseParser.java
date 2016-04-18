package com.christophermathew.memleaktests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public abstract class BaseParser<T> implements ParserInterface<T> {
    @Override
    public ArrayList<T> parse(JSONArray json) throws JSONException {
        ArrayList<T> items = new ArrayList<>();

        for (int j = 0; j < json.length(); j++) {
            JSONObject itemJson = json.getJSONObject(j);
            T item = parse(itemJson);
            items.add(item);
        }
        return items;
    }
}
