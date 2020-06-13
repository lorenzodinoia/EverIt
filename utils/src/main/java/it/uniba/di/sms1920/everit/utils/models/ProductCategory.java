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

    public Product getProductByIndex(long prodId) {
        Product found = null;

        for(Product it: this.products){
            if(it.getId() == prodId){
                found = it;
                break;
            }
        }

        return found;
    }

    public void setProducts(Collection<Product> products) {
        this.products = products;
    }

}
