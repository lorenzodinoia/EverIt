package it.uniba.di.sms1920.everit.utils;

import android.os.Parcel;
import android.os.Parcelable;

public final class Address implements Parcelable {
    private String address;
    private String city;
    private double latitude;
    private double longitude;

    public Address(String address, String city, double latitude, double longitude) {
        this.address = address;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private Address(Parcel in) {
        this(in.readString(), in.readString(), in.readDouble(), in.readDouble());
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
        return String.format("%s, %s", city, address);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeString(city);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
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
