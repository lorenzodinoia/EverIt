package it.uniba.di.sms1920.everit.utils.models;

import com.google.gson.annotations.SerializedName;

import java.util.Collection;
import java.util.List;

import it.uniba.di.sms1920.everit.utils.Address;

public class Restaurateur extends Model implements Authenticable{
    private String shopName;
    private Address address;
    private String phoneNumber;
    private String email;
    private String password;
    private String vatNumber;
    private String description;
    @SerializedName("max_delivery_time_slot")
    private int maxDeliveryPerTimeSlot;
    private float deliveryCost;
    private float minPrice;
    private String imagePath;
    private ShopType shopType;
    private boolean isOpen;
    private Collection<ProductCategory> productCategories;
    @SerializedName("opening_times")
    private List<OpeningDay> openingDays;

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
        this.productCategories = builder.productCategories;
        this.openingDays = builder.openingDays;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
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

    public int getMaxDeliveryPerTimeSlot(){
        return maxDeliveryPerTimeSlot;
    }

    public void setMaxDeliveryPerTimeSlot(int maxDeliveryPerTimeSlot){
        this.maxDeliveryPerTimeSlot = maxDeliveryPerTimeSlot;
    }

    public float getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(float deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public float getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(float minPrice) {
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

    public List<OpeningDay> getOpeningDays() {
        return openingDays;
    }

    public void setOpeningDays(List<OpeningDay> openingDays) {
        this.openingDays = openingDays;
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

    public static final class Builder {
        private long id;
        private String shopName;
        private Address address;
        private String phoneNumber;
        private String email;
        private String password;
        private String vatNumber;
        private String description;
        private int maxDeliveryPerTimeSlot;
        private float deliveryCost;
        private float minPrice;
        private String imagePath;
        private ShopType shopType;
        private boolean isOpen;
        private Collection<ProductCategory> productCategories;
        private List<OpeningDay> openingDays;

        public Builder() {

        }

        public String getShopName() {
            return shopName;
        }

        public Builder setShopName(String shopName) {
            this.shopName = shopName;
            return this;
        }

        public Address getAddress() {
            return address;
        }

        public Builder setAddress(Address address) {
            this.address = address;
            return this;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public Builder setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public String getEmail() {
            return email;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public String getVatNumber() {
            return vatNumber;
        }

        public Builder setVatNumber(String vatNumber) {
            this.vatNumber = vatNumber;
            return this;
        }

        public String getDescription() {
            return description;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public int getMaxDeliveryPerTimeSlot() {
            return maxDeliveryPerTimeSlot;
        }

        public Builder setMaxDeliveryPerTimeSlot(int maxDeliveryPerTimeSlot) {
            this.maxDeliveryPerTimeSlot = maxDeliveryPerTimeSlot;
            return this;
        }

        public float getDeliveryCost() {
            return deliveryCost;
        }

        public Builder setDeliveryCost(float deliveryCost) {
            this.deliveryCost = deliveryCost;
            return this;
        }

        public float getMinPrice() {
            return minPrice;
        }

        public Builder setMinPrice(float minPrice) {
            this.minPrice = minPrice;
            return this;
        }

        public String getImagePath() {
            return imagePath;
        }

        public Builder setImagePath(String imagePath) {
            this.imagePath = imagePath;
            return this;
        }

        public ShopType getShopType() {
            return shopType;
        }

        public Builder setShopType(ShopType shopType) {
            this.shopType = shopType;
            return this;
        }

        public boolean isOpen() {
            return isOpen;
        }

        public Builder setOpen(boolean open) {
            isOpen = open;
            return this;
        }

        public Collection<ProductCategory> getProductCategories() {
            return productCategories;
        }

        public Builder setProductCategories(Collection<ProductCategory> productCategories) {
            this.productCategories = productCategories;
            return this;
        }

        public List<OpeningDay> getOpeningDays() {
            return openingDays;
        }

        public Builder setOpeningDays(List<OpeningDay> openingDays) {
            this.openingDays = openingDays;
            return this;
        }

        public Restaurateur build() {
            return new Restaurateur(this);
        }
    }
}
