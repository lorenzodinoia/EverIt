package it.uniba.di.sms1920.everit.utils.request;

import java.util.Collection;

import it.uniba.di.sms1920.everit.utils.models.Order;

public final class OrderRequest extends CRUDRequest<Order> implements CRUD<Order> {

    private final String URL = "customer/order";

    @Override
    public void create(Order model, RequestListener<Order> RequestListener) {
        super.create(model, URL, RequestListener, Order.class, true);
    }

    @Override
    public void read(long id, RequestListener<Order> RequestListener) {
        super.read(id, URL, RequestListener, Order.class, true);
    }

    @Override
    public void readAll(RequestListener<Collection<Order>> RequestListener) {
        super.readAll(URL, RequestListener, Order.class, true);
    }

    @Override
    public void update(Order model, RequestListener<Order> RequestListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(long id, RequestListener<Boolean> RequestListener) {
        throw new UnsupportedOperationException();
    }
}
