package com.christophermathew.memleaktests;

import org.json.JSONException;
import org.json.JSONObject;

public class DriverParser extends BaseParser<Driver> {
    @Override
    public Driver parse(JSONObject json) throws JSONException {
        Driver d = new Driver();

        d.setId(json.getInt("id"));
        d.setAdmin(json.getBoolean("is_admin"));
        d.setAvailable(json.getBoolean("available_now"));
        d.setMetroId(json.getInt("metro_id"));

        d.setPreferredConveyanceMetatype(json.getString("preferred_conveyance_metatype"));
        d.setPhoneNumber(json.getString("cellphone"));
        d.setFirstName(json.getString("first_name"));
        d.setLastName(json.getString("last_name"));
        d.setEmail(json.getString("email"));
        d.setPhotoUrl(json.getString("photo_url"));
        d.setAuthenticationToken(json.getString("authentication_token"));

        d.setDelivingSince(json.getString("deliving_since"));
        d.setMetroName(json.getString("metro_name"));

        Object badgeNum = json.getString("badge_number");
        if (badgeNum != "null") {
            d.setBadgeNumber(badgeNum.toString());
        }

        d.setBalance("TBD");

        // these may be NULL, so let's check them first.
        //
        if (!json.isNull("active_delivery_id")) {
            d.setActiveDeliveryId(json.getInt("active_delivery_id"));
        }

        if (!json.isNull("active_task_id")) {
            d.setActiveTaskId(json.getInt("active_task_id"));
        }

        if (json.has("rating_total")) {
            d.setDriverRating(json.getInt("rating_total"));
        }

        if (json.has("has_submitted_hours")) {
            d.setHasSubmittedHours(json.getBoolean("has_submitted_hours"));
        }

        return d;
    }
}
