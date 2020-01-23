package it.uniba.di.sms1920.everit.adapter;

import java.util.HashMap;
import java.util.Map;

import it.uniba.di.sms1920.everit.models.Customer;
import it.uniba.di.sms1920.everit.models.Model;

public final class AdapterProvider {
    private static final Map<Class<? extends Model>, Adapter<? extends Model>> adapters = new HashMap<>();

    static {
        adapters.put(Customer.class, new Adapter<Customer>());
    }

    public static <T extends Model> Adapter<T> getAdapterFor(Class<T> modelType) {
        return (Adapter<T>) adapters.get(modelType);
    }
}
