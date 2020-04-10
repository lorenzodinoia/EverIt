package it.uniba.di.sms1920.everit.utils.adapter;

import java.util.HashMap;
import java.util.Map;

import it.uniba.di.sms1920.everit.utils.models.City;
import it.uniba.di.sms1920.everit.utils.models.Customer;
import it.uniba.di.sms1920.everit.utils.models.Model;
import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.models.Rider;

public final class AdapterProvider {
    private static final Map<Class<? extends Model>, Adapter<? extends Model>> adapters = new HashMap<>();

    static {
        adapters.put(Customer.class, new Adapter<Customer>());
        adapters.put(City.class, new Adapter<City>());
        adapters.put(Order.class, new Adapter<Order>());
        adapters.put(Restaurateur.class, new Adapter<Restaurateur>());
        adapters.put(Rider.class, new Adapter<Rider>());
    }

    public static <T extends Model> Adapter<T> getAdapterFor(Class<T> modelClass) {
        return (Adapter<T>) adapters.get(modelClass);
    }
}
