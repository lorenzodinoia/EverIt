package it.uniba.di.sms1920.everit.utils.models;

import java.util.InvalidPropertiesFormatException;

public class Rider extends User {

    private Rider(Rider.RiderBuilder customerBuilder){
        super(customerBuilder);
    }

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
}
