package it.uniba.di.sms1920.everit.utils.models;

import java.util.Collection;

public class ProductCategory extends Model {

    private String name;
    private Collection<Product> products;

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

    public Collection<Product> getProducts() {
        return products;
    }

    public void setProducts(Collection<Product> products) {
        this.products = products;
    }
}
