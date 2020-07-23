package it.uniba.di.sms1920.everit.utils.provider;

import java.util.HashMap;
import java.util.Map;

import it.uniba.di.sms1920.everit.utils.adapter.Adapter;
import it.uniba.di.sms1920.everit.utils.models.Customer;
import it.uniba.di.sms1920.everit.utils.models.Model;
import it.uniba.di.sms1920.everit.utils.models.OpeningTime;
import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.models.Product;
import it.uniba.di.sms1920.everit.utils.models.ProductCategory;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.models.Review;
import it.uniba.di.sms1920.everit.utils.models.Rider;
import it.uniba.di.sms1920.everit.utils.models.ShopType;

public final class AdapterProvider {
    private static final Map<Class<? extends Model>, Adapter<? extends Model>> adapters = new HashMap<>();

    static {
        adapters.put(Customer.class, new Adapter<Customer>());
        adapters.put(Order.class, new Adapter<Order>());
        adapters.put(Restaurateur.class, new Adapter<Restaurateur>());
        adapters.put(Rider.class, new Adapter<Rider>());
        adapters.put(Review.class, new Adapter<Review>());
        adapters.put(Product.class, new Adapter<Product>());
        adapters.put(ProductCategory.class, new Adapter<ProductCategory>());
        adapters.put(ShopType.class, new Adapter<ShopType>());
        adapters.put(OpeningTime.class, new Adapter<OpeningTime>());
    }

    public static <T extends Model> Adapter<T> getAdapterFor(Class<T> modelClass) {
        return (Adapter<T>) adapters.get(modelClass);
    }
}
