package it.uniba.di.sms1920.everit.utils.models;

import com.google.gson.annotations.SerializedName;

import java.util.Collection;
import java.util.Map;

public class Restaurateur extends Model implements Authenticable {
    private String shopName;
    private String address;
    private String phoneNumber;
    private String email;
    private String password;
    private String vatNumber;
    private String description;
    private float deliveryCost;
    private int minPrice;
    private String imagePath;
    private ShopType shopType;
    private City city;
    private boolean isOpen;
    private Collection<ProductCategory> productCategories;
    @SerializedName("opening_times")
    private Collection<OpeningDay> openingDays;
    private Map<Customer, Integer> feedbacks;

    private Restaurateur(Builder builder) {
        super(builder.id);
        this.shopName = builder.shopName;
        this.address = builder.address;
        this.phoneNumber = builder.phoneNumber;
        this.email = builder.email;
        this.vatNumber = builder.vatNumber;
        this.description = builder.description;
        this.deliveryCost = builder.deliveryCost;
        this.minPrice = builder.minPrice;
        this.imagePath = builder.imagePath;
        this.shopType = builder.shopType;
        this.city = builder.city;
        this.productCategories = builder.productCategories;
        this.openingDays = builder.openingDays;
        this.feedbacks = builder.feedbacks;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(float deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public ShopType getShopType() {
        return shopType;
    }

    public void setShopType(ShopType shopType) {
        this.shopType = shopType;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public Collection<ProductCategory> getProductCategories() {
        return productCategories;
    }

    public void setProductCategories(Collection<ProductCategory> productCategories) {
        this.productCategories = productCategories;
    }

    public Collection<OpeningDay> getOpeningDays() {
        return openingDays;
    }

    public void setOpeningDays(Collection<OpeningDay> openingDays) {
        this.openingDays = openingDays;
    }

    public Map<Customer, Integer> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(Map<Customer, Integer> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public Product getProductById(long id) {
        for (ProductCategory category : this.productCategories) {
            for (Product product : category.getProducts()) {
                if (product.getId() == id) {
                    return product;
                }
            }
        }

        return null;
    }

    private static final class Builder {
        private long id;
        private String shopName;
        private String address;
        private String phoneNumber;
        private String email;
        private String password;
        private String vatNumber;
        private String description;
        private float deliveryCost;
        private int minPrice;
        private String imagePath;
        private ShopType shopType;
        private City city;
        private Collection<ProductCategory> productCategories;
        private Collection<OpeningDay> openingDays;
        private Map<Customer, Integer> feedbacks;

        public Builder(String shopName, String address, String phoneNumber, String email, String vatNumber, ShopType shopType, City city) {
            this.shopName = shopName;
            this.address = address;
            this.phoneNumber = phoneNumber;
            this.email = email;
            this.vatNumber = vatNumber;
            this.shopType = shopType;
            this.city = city;
        }

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setDeliveryCost(float deliveryCost) {
            this.deliveryCost = deliveryCost;
            return this;
        }

        public Builder setMinPrice(int minPrice) {
            this.minPrice = minPrice;
            return this;
        }

        public Builder setImagePath(String imagePath) {
            this.imagePath = imagePath;
            return this;
        }

        public Builder setFeedbacks(Map<Customer, Integer> feedbacks) {
            this.feedbacks = feedbacks;
            return this;
        }

        public Builder setProductCategories(Collection<ProductCategory> productCategories) {
            this.productCategories = productCategories;
            return this;
        }

        public Builder setOpeningDays(Collection<OpeningDay> openingDays) {
            this.openingDays = openingDays;
            return this;
        }

        public Restaurateur build() {
            return new Restaurateur(this);
        }
    }
}
