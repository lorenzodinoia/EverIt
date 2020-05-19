package it.uniba.di.sms1920.everit.utils;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public final class Address implements Parcelable {
    private String address;
    private String city;
    private double latitude;
    private double longitude;

    public Address(Location location) {
        this(location.getLatitude(), location.getLongitude());
    }

    public Address(double latitude, double longitude) {
        this( latitude, longitude, null, null);
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

        if (this.city != null) {
            stringBuilder.append(this.city);
            stringBuilder.append(", ");
        }
        if (this.address != null) {
            stringBuilder.append(this.address);
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
