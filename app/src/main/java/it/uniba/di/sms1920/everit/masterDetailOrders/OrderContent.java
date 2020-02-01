package it.uniba.di.sms1920.everit.masterDetailOrders;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.uniba.di.sms1920.everit.models.Product;
import it.uniba.di.sms1920.everit.models.ProductCategory;

public class OrderContent {

    /**
     * An array of order items.
     */
    public static final List<OrderContent.OrderItem> ITEMS = new ArrayList<OrderContent.OrderItem>();

    /**
     * A map of order items, by ID.
     */
    public static final Map<Long, OrderContent.OrderItem> ITEM_MAP = new HashMap<Long, OrderContent.OrderItem>();

    public static void addItem(OrderContent.OrderItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    static{

        OrderContent.OrderItem orderItem;
        Map<Product, Integer> product = new HashMap<Product, Integer>();
        product.put(new Product("Product 1", 50, "Nice", new ProductCategory("Gjwan"), null), 1);
        product.put(new Product("Product 2", 40, "Nice", new ProductCategory("Gjwaan"), null), 2);
        product.put(new Product("Product 3", 30, "Nice", new ProductCategory("Gjwaen"), null), 3);

        for(int i=1; i<25; i++) {
            orderItem = new OrderItem(i, "activiy name", 20, "delivery address, n°", new Date(), product, "Ciao", "Ciaone");
            addItem(orderItem);
        }
    }
    /**
     * A Order item representing a piece of content.
     */
    public static class OrderItem {
        private final long id;
        private final String activityName;
        private final double orderPrice;
        private final String deliveryAddress;
        private final Date actualDeliveryTime;
        private final Map<Product, Integer> products;
        private final String orderNotes;
        private final String deliveryNotes;


        public OrderItem(long id, String activityName, double orderPrice, String deliveryAddress, Date actualDeliveryTime, Map<Product, Integer> products, String orderNotes, String deliveryNotes) {
            this.id = id;
            this.activityName = activityName;
            this.orderPrice = orderPrice;
            this.deliveryAddress = deliveryAddress;
            this.actualDeliveryTime = actualDeliveryTime;
            this.products = products;
            this.orderNotes = orderNotes;
            this.deliveryNotes = deliveryNotes;
        }

        public long getId() {
            return id;
        }

        public String getActivityName() {
            return activityName;
        }

        public double getOrderPrice() {
            return orderPrice;
        }

        public String getDeliveryAddress() {
            return deliveryAddress;
        }

        public Date getActualDeliveryTime() {
            return actualDeliveryTime;
        }

        public Map<Product, Integer> getProducts() {
            return products;
        }

        public String getOrderNotes() {
            return orderNotes;
        }

        public String getDeliveryNotes() {
            return deliveryNotes;
        }
    }

}
