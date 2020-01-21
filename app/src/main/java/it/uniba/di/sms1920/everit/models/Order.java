package it.uniba.di.sms1920.everit.models;

import java.time.LocalDateTime;
import java.util.Map;

public class Order extends Model {
    private String deliveryAddress;
    private LocalDateTime estimatedDeliveryTime;
    private String orderNotes;
    private String validationCode;
    private String deliveryNotes;
    private LocalDateTime actualDeliveryTime;
    private boolean delivered;
    private Map<Product, Integer> products;

    public Order(String deliveryAddress, LocalDateTime estimatedDeliveryTime, String orderNotes, String validationCode, String deliveryNotes, LocalDateTime actualDeliveryTime, boolean delivered) {
        this.deliveryAddress = deliveryAddress;
        this.estimatedDeliveryTime = estimatedDeliveryTime;
        this.orderNotes = orderNotes;
        this.validationCode = validationCode;
        this.deliveryNotes = deliveryNotes;
        this.actualDeliveryTime = actualDeliveryTime;
        this.delivered = delivered;
    }

    public Order(long id, String deliveryAddress, LocalDateTime estimatedDeliveryTime, String orderNotes, String validationCode, String deliveryNotes, LocalDateTime actualDeliveryTime, boolean delivered) {
        super(id);
        this.deliveryAddress = deliveryAddress;
        this.estimatedDeliveryTime = estimatedDeliveryTime;
        this.orderNotes = orderNotes;
        this.validationCode = validationCode;
        this.deliveryNotes = deliveryNotes;
        this.actualDeliveryTime = actualDeliveryTime;
        this.delivered = delivered;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public LocalDateTime getEstimatedDeliveryTime() {
        return estimatedDeliveryTime;
    }

    public void setEstimatedDeliveryTime(LocalDateTime estimatedDeliveryTime) {
        this.estimatedDeliveryTime = estimatedDeliveryTime;
    }

    public String getOrderNotes() {
        return orderNotes;
    }

    public void setOrderNotes(String orderNotes) {
        this.orderNotes = orderNotes;
    }

    public String getValidationCode() {
        return validationCode;
    }

    public void setValidationCode(String validationCode) {
        this.validationCode = validationCode;
    }

    public String getDeliveryNotes() {
        return deliveryNotes;
    }

    public void setDeliveryNotes(String deliveryNotes) {
        this.deliveryNotes = deliveryNotes;
    }

    public LocalDateTime getActualDeliveryTime() {
        return actualDeliveryTime;
    }

    public void setActualDeliveryTime(LocalDateTime actualDeliveryTime) {
        this.actualDeliveryTime = actualDeliveryTime;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }
}
