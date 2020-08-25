package it.uniba.di.sms1920.everit.utils.models;

import android.os.Parcel;
import android.os.Parcelable;

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
    @SerializedName("max_delivery_time_slot")
    private int maxDeliveryPerTimeSlot;
    private float deliveryCost;
    private float minPrice;
    private int avg;
    private String imagePath;
    private ShopType shopType;
    private boolean isOpen;
    private Collection<ProductCategory> productCategories;
    @SerializedName("opening_times")
    private List<OpeningDay> openingDays;

    public static final Creator<Restaurateur> CREATOR = new Creator<Restaurateur>() {
        @Override
        public Restaurateur createFromParcel(Parcel in) {
            return new Restaurateur(in);
        }

        @Override
        public Restaurateur[] newArray(int size) {
            return new Restaurateur[size];
        }
    };

    private Restaurateur(Builder builder) {
        super(builder.id);
        this.shopName = builder.shopName;
        this.address = builder.address;
        this.phoneNumber = builder.phoneNumber;
        this.email = builder.email;
        this.vatNumber = builder.vatNumber;
        this.maxDeliveryPerTimeSlot = builder.maxDeliveryPerTimeSlot;
        this.deliveryCost = builder.deliveryCost;
        this.minPrice = builder.minPrice;
        this.imagePath = builder.imagePath;
        this.shopType = builder.shopType;
        this.productCategories = builder.productCategories;
        this.openingDays = builder.openingDays;
    }

    public Restaurateur(Parcel in) {
        super(in);
        this.shopName = in.readString();
        this.address = in.readParcelable(Address.class.getClassLoader());
        this.phoneNumber = in.readString();
        this.email = in.readString();
        this.password = in.readString();
        this.vatNumber = in.readString();
        this.maxDeliveryPerTimeSlot = in.readInt();
        this.deliveryCost = in.readFloat();
        this.minPrice = in.readFloat();
        this.avg = in.readInt();
        this.imagePath = in.readString();
        this.shopType = in.readParcelable(ShopType.class.getClassLoader());
        this.isOpen = (in.readInt() == 1);
        this.productCategories = in.createTypedArrayList(ProductCategory.CREATOR);
        this.openingDays = in.createTypedArrayList(OpeningDay.CREATOR);
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

    public int getAvg(){
        return avg;
    }

    public void setAvg(int avg){
        this.avg = avg;
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

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.shopName);
        dest.writeParcelable(this.address, flags);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.email);
        dest.writeString(this.password);
        dest.writeString(this.vatNumber);
        dest.writeInt(this.maxDeliveryPerTimeSlot);
        dest.writeFloat(this.deliveryCost);
        dest.writeFloat(this.minPrice);
        dest.writeInt(this.avg);
        dest.writeString(this.imagePath);
        dest.writeParcelable(this.shopType, flags);
        dest.writeInt((this.isOpen) ? 1 : 0);
        if (this.productCategories != null) {
            dest.writeTypedArray(this.productCategories.toArray(new ProductCategory[0]), flags);
        }
        else {
            dest.writeTypedArray(null, flags);
        }
        if (this.openingDays != null) {
            dest.writeTypedArray(this.openingDays.toArray(new OpeningDay[0]), flags);
        }
        else {
            dest.writeTypedArray(null, flags);
        }
    }

    public static final class Builder implements Parcelable {
        private long id;
        private String shopName;
        private Address address;
        private String phoneNumber;
        private String email;
        private String password;
        private String vatNumber;
        private int maxDeliveryPerTimeSlot;
        private float deliveryCost;
        private float minPrice;
        private int avg;
        private String imagePath;
        private ShopType shopType;
        private boolean isOpen;
        private Collection<ProductCategory> productCategories;
        private List<OpeningDay> openingDays;

        public Builder() {

        }

        protected Builder(Parcel in) {
            id = in.readLong();
            shopName = in.readString();
            address = in.readParcelable(Address.class.getClassLoader());
            phoneNumber = in.readString();
            email = in.readString();
            password = in.readString();
            vatNumber = in.readString();
            maxDeliveryPerTimeSlot = in.readInt();
            deliveryCost = in.readFloat();
            minPrice = in.readFloat();
            avg = in.readInt();
            imagePath = in.readString();
            shopType = in.readParcelable(ShopType.class.getClassLoader());
            isOpen = in.readByte() != 0;
            openingDays = in.createTypedArrayList(OpeningDay.CREATOR);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(id);
            dest.writeString(shopName);
            dest.writeParcelable(address, flags);
            dest.writeString(phoneNumber);
            dest.writeString(email);
            dest.writeString(password);
            dest.writeString(vatNumber);
            dest.writeInt(maxDeliveryPerTimeSlot);
            dest.writeFloat(deliveryCost);
            dest.writeFloat(minPrice);
            dest.writeInt(avg);
            dest.writeString(imagePath);
            dest.writeParcelable(shopType, flags);
            dest.writeByte((byte) (isOpen ? 1 : 0));
            dest.writeTypedList(openingDays);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Builder> CREATOR = new Creator<Builder>() {
            @Override
            public Builder createFromParcel(Parcel in) {
                return new Builder(in);
            }

            @Override
            public Builder[] newArray(int size) {
                return new Builder[size];
            }
        };

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

        public int getAvg(){
            return avg;
        }

        public Builder setAvg(int avg){
            this.avg = avg;
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
