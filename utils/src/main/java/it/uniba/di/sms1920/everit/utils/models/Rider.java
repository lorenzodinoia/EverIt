package it.uniba.di.sms1920.everit.utils.models;

import android.os.Parcel;

import java.util.InvalidPropertiesFormatException;

public class Rider extends User {

    private Rider(Rider.RiderBuilder customerBuilder){
        super(customerBuilder);
    }

    public Rider(Parcel in) {
        super(in);
    }

    public static final Creator<Rider> CREATOR = new Creator<Rider>() {
        @Override
        public Rider createFromParcel(Parcel in) {
            return new Rider(in);
        }

        @Override
        public Rider[] newArray(int size) {
            return new Rider[size];
        }
    };

    public static final class RiderBuilder extends User.UserBuilder<Rider> {

        public RiderBuilder(String name, String surname, String phoneNumber, String email) {
            super(name, surname, phoneNumber, email);
        }

        @Override
        public Rider build() throws InvalidPropertiesFormatException {
            super.checkFields();
            return new Rider(this);
        }
    }

    public String getFullName(){
        return this.getName() + " " + this.getSurname();
    }
}
