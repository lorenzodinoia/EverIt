package it.uniba.di.sms1920.everit.utils;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public final class Address implements Parcelable {
    private static final String CITY_SEPARATOR = " - ";

    private String address;
    private String city;
    private double latitude;
    private double longitude;

    public Address(Location location) {
        this(location.getLatitude(), location.getLongitude());
    }

    public Address(double latitude, double longitude) {
        this(latitude, longitude, null, null);
    }

    public Address(double latitude, double longitude, String fullAddress) {
        if (fullAddress.contains(" - ")) {
            String[] parts = fullAddress.split(CITY_SEPARATOR, 2);
            this.address = parts[0];
            this.city = parts[1];
        }
        else {
            this.address = fullAddress;
            this.city = "";
        }
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Address (double latitude, double longitude, String address, String city) {
        this.address = address;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private Address(Parcel in) {
        this(in.readDouble(), in.readDouble(), in.readString(), in.readString());
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getFullAddress() {
        StringBuilder stringBuilder = new StringBuilder();

        if (this.address != null) {
            stringBuilder.append(this.address);
        }
        if (this.city != null) {
            stringBuilder.append(CITY_SEPARATOR);
            stringBuilder.append(this.city);
        }

        return stringBuilder.toString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(address);
        dest.writeString(city);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };
}
