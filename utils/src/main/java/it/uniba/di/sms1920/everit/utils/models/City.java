package it.uniba.di.sms1920.everit.utils.models;

public class City extends Model {

    private String name;

    public City(long id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
