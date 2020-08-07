package it.uniba.di.sms1920.everit.utils.models;

import android.os.Parcel;

import java.util.InvalidPropertiesFormatException;

public class Customer extends User {

    private Customer(CustomerBuilder customerBuilder){
        super(customerBuilder);
    }

    protected Customer(Parcel in) {
        super(in);
    }

    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    public static final class CustomerBuilder extends User.UserBuilder<Customer> {

        public CustomerBuilder(String name, String surname, String phoneNumber, String email) {
            super(name, surname, phoneNumber, email);
        }

        @Override
        public Customer build() throws InvalidPropertiesFormatException {
            super.checkFields();
            return new Customer(this);
        }
    }
}
