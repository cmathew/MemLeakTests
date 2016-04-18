package com.christophermathew.memleaktests;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Driver implements Serializable, Parcelable {

    private static final long serialVersionUID = 2L;
    public final static String DRIVER_UPDATED = "co.deliv.blackdog.DRIVER_UPDATED";

    private boolean available = false;
    private boolean admin = false;
    private boolean hasSubmittedHours = false;

    private int id;
    private int activeDeliveryId;
    private int activeTaskId;
    private int rating;
    private int metroId;

    private String preferredConveyanceMetatype;
    private String phoneNumber;
    private String lastName;
    private String firstName;
    private String email;
    private String photoUrl;
    private String authenticationToken;
    private String badgeNumber = null;

    private String delivingSince;
    private String metroName;

    // TODO: replace with float?
    private String balance;


    /**
     * Constructor
     */
    public Driver() { }

    /**
     * Returns the full name (first + last) of the driver
     */
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    /**
     * @return the available
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * @return
     */
    public boolean isAdmin() {
        return this.admin;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the activeDeliveryId
     */
    public int getActiveDeliveryId() {
        return activeDeliveryId;
    }

    public int getDriverRating() {
        return this.rating;
    }

    public int getMetroId() {
        return this.metroId;
    }

    public Boolean hasSubmittedHours() {
        return this.hasSubmittedHours;
    }

    public void setHasSubmittedHours(boolean hasSubmittedHours) {
        this.hasSubmittedHours = hasSubmittedHours;
    }

    /**
     * Method that returns unicode star chars for the driver rating.
     * TODO: this will have to be updated to a special control later.
     * @return
     */
    public String getDriverRatingAsStars() {
        if (this.rating == 0) {
            return "\u2605\u2605\u2605\u2605\u2605";
        }

        // TODO: replace with logic to mask/show images
        //
        return "\u2605\u2605\u2605\u2605\u2605";
    }

    /**
     * @return the activeTaskId
     */
    public int getActiveTaskId() {
        return activeTaskId;
    }

    /**
     * @return the preferredConveyanceMetatype
     */
    public String getPreferredConveyanceMetatype() {
        if (preferredConveyanceMetatype == null) {
            return "automobile";
        }

        return preferredConveyanceMetatype;
    }

    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return the photoUrl
     */
    public String getPhotoUrl(Context ctx) {
        if (this.photoUrl.startsWith("http")) {
            return photoUrl;
        }

        if (this.photoUrl.startsWith("//")) {
            // Some Android versions have trouble with SSL and Cloudfront
            return String.format("http:%s", this.photoUrl);
        }

        String prodHost = ctx.getString(R.string.production_host);
        return String.format("https://%s/%s/", prodHost, this.photoUrl);
    }

    /**
     * @return the authenticationToken
     */
    public String getAuthenticationToken() {
        return authenticationToken;
    }

    /**
     * @return the authenticationToken
     */
    public String getBadgeNumber() {
        return badgeNumber;
    }

    /**
     * @return the balance of the driver
     */
    public String getBalance() {
        return this.balance;
    }

    /**
     * @return the start month of the driver
     */
    public String getDelivingSince() {
        return delivingSince;
    }

    /**
     * @return the metro name of the driver
     */
    public String getMetroName() {
        return metroName;
    }

    /**
     * @param admin
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    /**
     * @param available the available to set
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the rating for the driver.
     * TODO: wire up callbacks to PUT @ drivers/me
     * @param r
     */
    public void setDriverRating(int r) {
        this.rating = r;
    }

    public void setMetroId(int metro) {
        this.metroId = metro;
    }

    /**
     * @param activeDeliveryId the activeDeliveryId to set
     */
    public void setActiveDeliveryId(int activeDeliveryId) {
        this.activeDeliveryId = activeDeliveryId;
    }

    /**
     * @param activeTaskId the activeTaskId to set
     */
    public void setActiveTaskId(int activeTaskId) {
        this.activeTaskId = activeTaskId;
    }

    /**
     * @param metatype the preferredConveyanceMetatype to set
     */
    public void setPreferredConveyanceMetatype(String metatype) {
        this.preferredConveyanceMetatype = metatype;
    }

    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @param photoUrl the photoUrl to set
     */
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    /**
     * @param authenticationToken the authenticationToken to set
     */
    public void setAuthenticationToken(String authenticationToken) {
        this.authenticationToken = authenticationToken;
    }

    /**
     * @param badgeNumber the badge identifier
     */
    public void setBadgeNumber(String badgeNumber) {
        this.badgeNumber = badgeNumber;
    }

    /**
     * @param b the balance of the driver.
     */
    public void setBalance(String b) {
        this.balance = b;
    }

    /**
     * @param delivingSince the driver's start month
     */
    public void setDelivingSince(String delivingSince) {
        this.delivingSince = delivingSince;
    }

    /**
     * @param metroName the driver's current metro
     */
    public void setMetroName(String metroName) { this.metroName = metroName; }

    //================================================================================
    // Parcelable
    //================================================================================
    protected Driver(Parcel in) {
        this.available = in.readInt() != 0;
        this.admin = in.readInt() != 0;
        this.hasSubmittedHours = in.readInt() != 0;

        this.id = in.readInt();
        this.activeDeliveryId = in.readInt();
        this.activeTaskId = in.readInt();
        this.rating = in.readInt();
        this.metroId = in.readInt();

        this.phoneNumber = in.readString();
        this.lastName = in.readString();
        this.firstName = in.readString();
        this.email = in.readString();
        this.photoUrl = in.readString();
        this.authenticationToken = in.readString();
        this.badgeNumber = in.readString();

        this.delivingSince = in.readString();
        this.metroName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(available ? 1 : 0);
        dest.writeInt(admin ? 1 : 0);
        dest.writeInt(hasSubmittedHours ? 1 : 0);

        dest.writeInt(id);
        dest.writeInt(activeDeliveryId);
        dest.writeInt(activeTaskId);
        dest.writeInt(rating);
        dest.writeInt(metroId);

        dest.writeString(phoneNumber);
        dest.writeString(lastName);
        dest.writeString(firstName);
        dest.writeString(email);
        dest.writeString(photoUrl);
        dest.writeString(authenticationToken);
        dest.writeString(badgeNumber);

        dest.writeString(delivingSince);
        dest.writeString(metroName);
    }

    public static final Parcelable.Creator<Driver> CREATOR = new Parcelable.Creator<Driver>() {
        @Override
        public Driver createFromParcel(Parcel in) {
            return new Driver(in);
        }

        @Override
        public Driver[] newArray(int size) {
            return new Driver[size];
        }
    };

    public boolean hasAutomobile() {
        return getPreferredConveyanceMetatype().equals("automobile");
    }
}
