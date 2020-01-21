package it.uniba.di.sms1920.everit.models;

public class Customer extends User {
    public Customer(String name, String surname, String phoneNumber, String email) {
        super(name, surname, phoneNumber, email);
    }

    public Customer(long id, String name, String surname, String phoneNumber, String email) {
        super(id, name, surname, phoneNumber, email);
    }
}
