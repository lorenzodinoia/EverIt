package it.uniba.di.sms1920.everit.utils.models;

import android.os.Parcel;

import androidx.annotation.Nullable;

public class Product extends Model{

    private String name;
    private float price;
    private String details;
    private ProductCategory category;
    private Restaurateur restaurateur;

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

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

    public Product(Parcel in) {
        super(in);
        this.name = in.readString();
        this.price = in.readFloat();
        this.details = in.readString();
        this.category = in.readParcelable(ProductCategory.class.getClassLoader());
        this.restaurateur = in.readParcelable(Restaurateur.class.getClassLoader());
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

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.name);
        dest.writeFloat(this.price);
        dest.writeString(this.details);
        dest.writeParcelable(this.category, flags);
        dest.writeParcelable(this.restaurateur, flags);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Product product = (Product) obj;
        return this.getId() == product.getId();
    }

    @Override
    public int hashCode() {
        long l = this.getId();
        return (int) l;
    }
}


