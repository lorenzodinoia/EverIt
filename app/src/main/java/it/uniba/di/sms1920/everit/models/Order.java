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

    private Order(Builder builder) {
        super(builder.id);
        this.deliveryAddress = builder.deliveryAddress;
        this.estimatedDeliveryTime = builder.estimatedDeliveryTime;
        this.orderNotes = builder.orderNotes;
        this.validationCode = builder.validationCode;
        this.deliveryNotes = builder.deliveryNotes;
        this.actualDeliveryTime = builder.actualDeliveryTime;
        this.delivered = builder.delivered;
        this.products = builder.products;
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

    public Map<Product, Integer> getProducts() {
        return products;
    }

    public void setProducts(Map<Product, Integer> products) {
        this.products = products;
    }

    private static final class Builder {
        private long id;
        private String deliveryAddress;
        private LocalDateTime estimatedDeliveryTime;
        private String orderNotes;
        private String validationCode;
        private String deliveryNotes;
        private LocalDateTime actualDeliveryTime;
        private boolean delivered;
        private Map<Product, Integer> products;

        public Builder(String deliveryAddress, LocalDateTime estimatedDeliveryTime, Map<Product, Integer> products) {
            this.deliveryAddress = deliveryAddress;
            this.estimatedDeliveryTime = estimatedDeliveryTime;
            this.products = products;
        }

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setOrderNotes(String orderNotes) {
            this.orderNotes = orderNotes;
            return this;
        }

        public Builder setValidationCode(String validationCode) {
            this.validationCode = validationCode;
            return this;
        }

        public Builder setDeliveryNotes(String deliveryNotes) {
            this.deliveryNotes = deliveryNotes;
            return this;
        }

        public Builder setActualDeliveryTime(LocalDateTime actualDeliveryTime) {
            this.actualDeliveryTime = actualDeliveryTime;
            return this;
        }

        public Builder setDelivered(boolean delivered) {
            this.delivered = delivered;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
