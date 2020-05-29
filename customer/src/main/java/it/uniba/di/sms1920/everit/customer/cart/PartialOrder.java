package it.uniba.di.sms1920.everit.customer.cart;

import java.util.HashMap;
import java.util.Map;

import it.uniba.di.sms1920.everit.utils.Address;
import it.uniba.di.sms1920.everit.utils.models.Product;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;

public class PartialOrder {
    private Address deliveryAddress;
    private String orderNotes;
    private String deliveryNotes;
    private Restaurateur restaurateur;
    private Map<Product, Integer> products = new HashMap<>();

    public PartialOrder(Restaurateur restaurateur, Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
        this.restaurateur = restaurateur;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
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

    public Restaurateur getRestaurateur() {
        return restaurateur;
    }

    public Map<Product, Integer> getProducts() {
        return products;
    }

    public void setProducts(Map<Product, Integer> products) {
        this.products = products;
    }

    public void addProduct(Product product) {
        this.addProduct(product, 1);
    }

    public void addProduct(Product product, int quantity) {
        this.products.put(product, quantity);
    }

    public void removeProduct(Product product) {
        this.products.remove(product);
    }

    public void setProductQuantity(Product product, int quantity) {
        this.addProduct(product, quantity);
    }

    SerializableItem getSerializableItem() {
        return new SerializableItem(this);
    }

    static class SerializableItem {
        private Address deliveryAddress;
        private String orderNotes;
        private String deliveryNotes;
        private long restaurateurId;
        private Map<Long, Integer> products = new HashMap<>();

        SerializableItem(PartialOrder partialOrder) {
            this.deliveryAddress = partialOrder.deliveryAddress;
            this.orderNotes = partialOrder.orderNotes;
            this.deliveryNotes = partialOrder.deliveryNotes;
            this.restaurateurId = partialOrder.restaurateur.getId();

            for (Map.Entry<Product, Integer> entry : partialOrder.products.entrySet()) {
                this.products.put(entry.getKey().getId(), entry.getValue());
            }
        }

        Address getDeliveryAddress() {
            return deliveryAddress;
        }

        String getOrderNotes() {
            return orderNotes;
        }

        String getDeliveryNotes() {
            return deliveryNotes;
        }

        long getRestaurateurId() {
            return restaurateurId;
        }

        Map<Long, Integer> getProducts() {
            return products;
        }
    }
}
