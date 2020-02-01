package it.uniba.di.sms1920.everit.masterDetailOrders.productList;

import java.util.ArrayList;
import java.util.List;

public class ProductContent {

    public static final List<ProductItem> ITEMS = new ArrayList<ProductItem>();

    public static void addItem(ProductItem productItem){
        ITEMS.add(productItem);
    }

    static{
        for(int i=0; i<25; i++){
            ProductContent.ProductItem productItem = new ProductContent.ProductItem("cibo", i);
            addItem(productItem);
        }
    }

    public static class ProductItem {

        private String name;
        private double price;

        public ProductItem(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }

    }
}
