package it.uniba.di.sms1920.everit.models;

public class ShopType extends Model {

    private String name;

    public ShopType(long id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
