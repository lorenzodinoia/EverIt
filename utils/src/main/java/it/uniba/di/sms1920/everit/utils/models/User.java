package it.uniba.di.sms1920.everit.utils.models;

import java.util.InvalidPropertiesFormatException;

import it.uniba.di.sms1920.everit.utils.Utility;

public abstract class User extends Model implements Authenticable {

    private String name;
    private String surname;
    private String phoneNumber;
    private String email;
    private String password;

    protected User(UserBuilder userBuilder){
        super(userBuilder.id);
        this.name = userBuilder.name;
        this.surname = userBuilder.surname;
        this.phoneNumber = userBuilder.phoneNumber;
        this.email = userBuilder.email;
        this.password = userBuilder.password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static abstract class UserBuilder<T extends User> {
        protected long id;
        protected String name;
        protected String surname;
        protected String phoneNumber;
        protected String email;
        protected String password;

        public UserBuilder(String name, String surname, String phoneNumber, String email) {
            this.name = name;
            this.surname = surname;
            this.phoneNumber = phoneNumber;
            this.email = email;
        }

        public UserBuilder<T> setId(long id){
            this.id = id;
            return this;
        }

        public UserBuilder<T> setPassword(String password){
            this.password = password;
            return this;
        }

        public abstract T build() throws InvalidPropertiesFormatException;

        protected void checkFields()throws InvalidPropertiesFormatException {
            //Name check
            if (name == null) {
                throw new InvalidPropertiesFormatException("Name couldn't be empty");
            }
            else if (name.length() <= 1) {
                throw new InvalidPropertiesFormatException("Name too short");
            }
            else if (name.length() > 50) {
                throw new InvalidPropertiesFormatException("Name too long");
            }

            //Surname check
            if (surname == null){
                throw new InvalidPropertiesFormatException("Surname couldn't be empty");
            }
            else if (surname.length() <= 1){
                throw new InvalidPropertiesFormatException("Surname too short");
            }
            else if (surname.length() > 50){
                throw new InvalidPropertiesFormatException("Surname too long");
            }

            //Phone number check
            if (phoneNumber == null){
                throw new InvalidPropertiesFormatException("Phone Number couldn't be empty");
            }
            else if (phoneNumber.length() < 10){
                throw new InvalidPropertiesFormatException("Invalid phone number format");
            }

            //Email check
            if (email == null){
                throw new InvalidPropertiesFormatException("Email couldn't be empty");
            }
            else if (!Utility.isEmailValid(email)){
                throw new InvalidPropertiesFormatException("Invalid email format");
            }

            //Password check
            if (password == null){
                throw new InvalidPropertiesFormatException("Password couldn't be empty");
            }
            else if (!Utility.isPasswordValid(password)){
                throw new InvalidPropertiesFormatException("Invalid password format");
            }
        }
    }

}
