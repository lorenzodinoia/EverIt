package it.uniba.di.sms1920.everit.request;

import java.util.Collection;

import it.uniba.di.sms1920.everit.models.Order;

public final class OrderRequest extends CRUDRequest<Order> implements CRUD<Order> {

    private final String URL = "order";

    @Override
    public void create(Order model, RequestListener<Order> requestListener) {
        //TODO implement create request for order
    }

    @Override
    public void read(long id, RequestListener<Order> requestListener) {
        super.read(id, URL,requestListener, Order.class, true);
    }

    @Override
    public void readAll(RequestListener<Collection<Order>> requestListener) {
        super.readAll(URL, requestListener, Order.class, true);
    }

    @Override
    public void update(Order model, RequestListener<Order> requestListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(long id, RequestListener<Boolean> requestListener) {
        throw new UnsupportedOperationException();
    }
}
