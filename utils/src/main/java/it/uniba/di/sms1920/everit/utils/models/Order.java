package it.uniba.di.sms1920.everit.utils.models;

import java.util.Date;
import java.util.Map;

public class Order extends Model {

    private String deliveryAddress;
    private Date estimatedDeliveryTime;
    private String orderNotes;
    private String validationCode;
    private String deliveryNotes;
    private Date actualDeliveryTime;
    private boolean delivered;
    private Map<Product, Integer> products;
    private Restaurateur restaurateur;
    private Date createdAt;
    private int status;

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
        this.restaurateur = builder.restaurateur;
        this.status = builder.status;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Date getEstimatedDeliveryTime() {
        return estimatedDeliveryTime;
    }

    public void setEstimatedDeliveryTime(Date estimatedDeliveryTime) {
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

    public Date getActualDeliveryTime() {
        return actualDeliveryTime;
    }

    public void setActualDeliveryTime(Date actualDeliveryTime) {
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

    public Restaurateur getRestaurateur() {
        return restaurateur;
    }

    public void setRestaurateur(Restaurateur restaurateur) {
        this.restaurateur = restaurateur;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getTotalCost() {
        float cost = 0f;

        for (Map.Entry<Product, Integer> entry: this.products.entrySet()) {
            final int quantity = entry.getValue();
            cost += entry.getKey().getPrice() * quantity;

        }

        return cost;
    }

    public static final class Builder {
        private long id;
        private String deliveryAddress;
        private Date estimatedDeliveryTime;
        private String orderNotes;
        private String validationCode;
        private String deliveryNotes;
        private Date actualDeliveryTime;
        private boolean delivered;
        private Map<Product, Integer> products;
        private Restaurateur restaurateur;
        private int status;

        public Builder(String deliveryAddress, Date estimatedDeliveryTime, Map<Product, Integer> products, Restaurateur restaurateur, int status) {
            this.deliveryAddress = deliveryAddress;
            this.estimatedDeliveryTime = estimatedDeliveryTime;
            this.products = products;
            this.restaurateur = restaurateur;
            this.status = status;
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

        public Builder setActualDeliveryTime(Date actualDeliveryTime) {
            this.actualDeliveryTime = actualDeliveryTime;
            return this;
        }

        public Builder setDelivered(boolean delivered) {
            this.delivered = delivered;
            return this;
        }

        public Builder setStatus(int status){
            this.status = status;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
