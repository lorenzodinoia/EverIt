package it.uniba.di.sms1920.everit.models;

public class ProductCategory extends Model {

    private String name;

    public ProductCategory(String name) {
        this.name = name;
    }

    public ProductCategory(long id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
