package it.uniba.di.sms1920.everit.models;

import java.util.InvalidPropertiesFormatException;

public class Customer extends User {

    private Customer(CustomerBuilder customerBuilder){
        super(customerBuilder);
    }

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
