package it.uniba.di.sms1920.everit.utils.models;

import android.os.Parcel;

import java.util.List;

public class ProductCategory extends Model {

    private String name;
    private List<Product> products;

    public static final Creator<ProductCategory> CREATOR = new Creator<ProductCategory>() {
        @Override
        public ProductCategory createFromParcel(Parcel in) {
            return new ProductCategory(in);
        }

        @Override
        public ProductCategory[] newArray(int size) {
            return new ProductCategory[size];
        }
    };

    public ProductCategory(String name) {
        this.name = name;
    }

    public ProductCategory(long id, String name) {
        super(id);
        this.name = name;
    }

    public ProductCategory(Parcel in) {
        super(in);
        this.name = in.readString();
        this.products = in.createTypedArrayList(Product.CREATOR);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
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

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.name);
        dest.writeTypedArray(this.products.toArray(new Product[0]), flags);
    }
}
