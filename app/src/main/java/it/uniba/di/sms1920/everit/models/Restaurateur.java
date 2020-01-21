package it.uniba.di.sms1920.everit.models;

import java.util.Map;

public class Restaurateur extends Model {
    private String shopName;
    private String address;
    private String cap;
    private String phoneNumber;
    private String email;
    private String piva;
    private String description;
    private float deliveryCost;
    private int minmumQuantity;
    private int orderRangeTime;
    private String imagePath;
    private ShopType shopType;
    private City city;
    private Map<Customer, Integer> feedbacks;

    //TODO Sostituire sto sorto di costruttore con il builder
    public Restaurateur(long id, String shopName, String address, String cap, String phoneNumber, String email, String piva, String description, float deliveryCost, int minmumQuantity, int orderRangeTime, String imagePath, ShopType shopType, City city) {
        super(id);
        this.shopName = shopName;
        this.address = address;
        this.cap = cap;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.piva = piva;
        this.description = description;
        this.deliveryCost = deliveryCost;
        this.minmumQuantity = minmumQuantity;
        this.orderRangeTime = orderRangeTime;
        this.imagePath = imagePath;
        this.shopType = shopType;
        this.city = city;
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

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
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

    public String getPiva() {
        return piva;
    }

    public void setPiva(String piva) {
        this.piva = piva;
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

    public int getMinmumQuantity() {
        return minmumQuantity;
    }

    public void setMinmumQuantity(int minmumQuantity) {
        this.minmumQuantity = minmumQuantity;
    }

    public int getOrderRangeTime() {
        return orderRangeTime;
    }

    public void setOrderRangeTime(int orderRangeTime) {
        this.orderRangeTime = orderRangeTime;
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

    public Map<Customer, Integer> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(Map<Customer, Integer> feedbacks) {
        this.feedbacks = feedbacks;
    }
}
