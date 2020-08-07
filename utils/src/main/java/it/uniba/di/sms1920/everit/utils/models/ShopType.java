package it.uniba.di.sms1920.everit.utils.models;

import android.os.Parcel;

import androidx.annotation.NonNull;

public class ShopType extends Model {

    private String name;

    public static final Creator<ShopType> CREATOR = new Creator<ShopType>() {
        @Override
        public ShopType createFromParcel(Parcel in) {
            return new ShopType(in);
        }

        @Override
        public ShopType[] newArray(int size) {
            return new ShopType[size];
        }
    };

    public ShopType(long id, String name) {
        super(id);
        this.name = name;
    }

    public ShopType(Parcel in) {
        super(in);
        this.name = in.readString();
    }

    public String getName() {
        return name;
    }

    @NonNull
    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.name);
    }
}
