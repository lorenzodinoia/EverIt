package it.uniba.di.sms1920.everit.customer.cart;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.HashMap;
import java.util.Map;

import it.uniba.di.sms1920.everit.utils.Address;
import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.models.Product;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;

public class PartialOrder {
    private Address deliveryAddress;
    private String orderNotes;
    private String deliveryNotes;
    private Restaurateur restaurateur;
    private Map<Product, Integer> products = new HashMap<>();
    private String deliveryTime;
    private int orderType;

    public PartialOrder(Restaurateur restaurateur, Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
        this.restaurateur = restaurateur;
    }

    public String getDeliveryTime(){return deliveryTime;}

    public void setDeliveryTime(String devTime){ this.deliveryTime = devTime; }

    public int getOrderType() { return orderType; }

    public void setOrderType(int orderType) { this.orderType = orderType; }

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

    public int getProductQuantity(Product product) {
        int quantity = 0;

        if (this.products.containsKey(product)) {
            quantity = this.products.get(product);
        }

        return quantity;
    }


    public void addProduct(Product product) {
        int savedQuantity = this.getProductQuantity(product);

        this.addProduct(product, ++savedQuantity);
    }

    public void addProduct(Product product, int quantity) {
        this.products.put(product, quantity);
    }

    public void removeProduct(Product product) {
        int quantity = this.getProductQuantity(product);
        quantity--;

        if (quantity == 0) {
            this.deleteProduct(product);
        }
        else {
            this.addProduct(product, quantity);
        }
    }

    public void deleteProduct(Product product) {
        this.products.remove(product);
    }


    public Order partialOrderToOrder(){
        Order order = new Order();

        LocalDate localDate = LocalDate.now();
        String formattedDate = localDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String completeDate = formattedDate + " " + this.deliveryTime;

        DateTimeFormatter formatterDt = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        LocalDateTime estimatedTime = LocalDateTime.parse(completeDate, formatterDt);

        order.setEstimatedDeliveryTime(estimatedTime);
        order.setDeliveryAddress(this.deliveryAddress);
        order.setDeliveryNotes(this.deliveryNotes);
        order.setOrderNotes(this.orderNotes);
        order.setProducts(this.products);
        order.setRestaurateur(this.restaurateur);
        order.setOrderType(this.orderType);

        return order;
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
        private String deliveryTime;
        private int orderType;

        SerializableItem(PartialOrder partialOrder) {
            this.deliveryAddress = partialOrder.deliveryAddress;
            this.orderNotes = partialOrder.orderNotes;
            this.deliveryNotes = partialOrder.deliveryNotes;
            this.restaurateurId = partialOrder.restaurateur.getId();
            this.deliveryTime = partialOrder.deliveryTime;
            this.orderType = partialOrder.orderType;

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

        String getDeliveryTime(){return  deliveryTime;}

        int getOrderType(){return orderType;}

    }
}
