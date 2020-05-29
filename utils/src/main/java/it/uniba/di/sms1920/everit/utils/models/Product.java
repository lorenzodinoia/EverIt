package it.uniba.di.sms1920.everit.utils.models;

public class Product extends Model {

    private String name;
    private float price;
    private String details;
    private ProductCategory category;
    private Restaurateur restaurateur;

    public Product(String name, float price, String details, ProductCategory category, Restaurateur restaurateur) {
        this.name = name;
        this.price = price;
        this.details = details;
        this.category = category;
        this.restaurateur = restaurateur;
    }

    public Product(long id, String name, float price, String details, ProductCategory category, Restaurateur restaurateur) {
        super(id);
        this.name = name;
        this.price = price;
        this.details = details;
        this.category = category;
        this.restaurateur = restaurateur;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public Restaurateur getRestaurateur() {
        return restaurateur;
    }

    public void setRestaurateur(Restaurateur restaurateur) {
        this.restaurateur = restaurateur;
    }
}


