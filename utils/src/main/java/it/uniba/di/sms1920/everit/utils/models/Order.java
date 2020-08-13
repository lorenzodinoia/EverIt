package it.uniba.di.sms1920.everit.utils.models;

import android.os.Parcel;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.Map;

import it.uniba.di.sms1920.everit.utils.Address;
import it.uniba.di.sms1920.everit.utils.Constants;

public class Order extends Model {
    public enum Status {
        ORDERED, IN_PROGRESS, DELIVERING, DELIVERED
    }

    private Address deliveryAddress;
    private LocalDateTime estimatedDeliveryTime;
    private LocalDateTime actualDeliveryTime;
    private String orderNotes;
    private String deliveryNotes;
    private String validationCode;
    private Status status;
    private Boolean late;
    private Map<Product, Integer> products;
    private LocalTime pickupTime;
    private Restaurateur restaurateur;
    private LocalDateTime createdAt;

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    private Order() {}

    public Order(Parcel in) {
        super(in);
        this.deliveryAddress = in.readParcelable(Address.class.getClassLoader());
        this.estimatedDeliveryTime = (LocalDateTime) in.readSerializable();
        this.actualDeliveryTime = (LocalDateTime) in.readSerializable();
        this.orderNotes = in.readString();
        this.deliveryNotes = in.readString();
        this.validationCode = in.readString();
        this.status = Status.valueOf(in.readString());
        //TODO Leggere mappa dei prodotti
        this.restaurateur = in.readParcelable(Restaurateur.class.getClassLoader());
        this.createdAt = (LocalDateTime) in.readSerializable();
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public LocalDateTime getEstimatedDeliveryTime() {
        return estimatedDeliveryTime;
    }

    public void setEstimatedDeliveryTime(LocalDateTime estimatedDeliveryTime) {
        this.estimatedDeliveryTime = estimatedDeliveryTime;
    }

    public LocalDateTime getActualDeliveryTime() {
        return actualDeliveryTime;
    }

    public void setActualDeliveryTime(LocalDateTime actualDeliveryTime) {
        this.actualDeliveryTime = actualDeliveryTime;
    }

    public String getOrderNotes() {
        return orderNotes;
    }

    public void setOrderNotes(String orderNotes) {
        this.orderNotes = orderNotes;
    }

    public String getDeliveryNotes() {
        return deliveryNotes;
    }

    public void setDeliveryNotes(String deliveryNotes) {
        this.deliveryNotes = deliveryNotes;
    }

    public String getValidationCode() {
        return validationCode;
    }

    public void setValidationCode(String validationCode) {
        this.validationCode = validationCode;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Boolean isLate() {
        return late;
    }

    public void setLate(boolean late){
        this.late = late;
    }

    public Map<Product, Integer> getProducts() {
        return products;
    }

    public void setProducts(Map<Product, Integer> products) {
        this.products = products;
    }

    public LocalTime getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(LocalTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getPickupTimeAsString() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(Constants.TIME_FORMAT);
        return timeFormatter.format(this.pickupTime);
    }

    public Restaurateur getRestaurateur() {
        return restaurateur;
    }

    public void setRestaurateur(Restaurateur restaurateur) {
        this.restaurateur = restaurateur;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isDelivered() {
        return (this.status.equals(Status.DELIVERED));
    }

    public void setDelivered(boolean delivered) {
        this.status = Status.DELIVERED;
    }

    public float getTotalCost() {
        float cost = 0f;

        for (Map.Entry<Product, Integer> entry: this.products.entrySet()) {
            final int quantity = entry.getValue();
            cost += entry.getKey().getPrice() * quantity;
        }

        return cost;
    }

    public int getRemainingTime() {
        LocalTime currentTime = LocalTime.now();
        return (int) currentTime.until(pickupTime, ChronoUnit.MINUTES);
    }


    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.deliveryAddress, flags);
        dest.writeSerializable(this.estimatedDeliveryTime);
        dest.writeSerializable(this.actualDeliveryTime);
        dest.writeString(this.orderNotes);
        dest.writeString(this.deliveryNotes);
        dest.writeString(this.validationCode);
        dest.writeString(this.status.name());
        //TODO Serializzare mappa dei prodotti
        dest.writeParcelable(this.restaurateur, flags);
        dest.writeSerializable(this.createdAt);
    }
}
