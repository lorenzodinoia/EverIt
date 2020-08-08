package it.uniba.di.sms1920.everit.utils.models;

import org.threeten.bp.LocalDateTime;

import java.util.Map;

import it.uniba.di.sms1920.everit.utils.Address;

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
    private Restaurateur restaurateur;
    private LocalDateTime createdAt;

    private Order() {}

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
}
