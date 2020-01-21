package it.uniba.di.sms1920.everit.models;

public class Rider extends User {
    public Rider(String name, String surname, String phoneNumber, String email) {
        super(name, surname, phoneNumber, email);
    }

    public Rider(long id, String name, String surname, String phoneNumber, String email) {
        super(id, name, surname, phoneNumber, email);
    }
}
