package it.uniba.di.sms1920.everit.utils.request;

import java.util.Collection;

import it.uniba.di.sms1920.everit.utils.models.Customer;
import it.uniba.di.sms1920.everit.utils.request.core.CRUD;
import it.uniba.di.sms1920.everit.utils.request.core.CRUDRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public final class CustomerRequest extends CRUDRequest<Customer> implements CRUD<Customer> {

    private final String URL = "customer";

    @Override
    public void create(Customer model, RequestListener<Customer> RequestListener) {
        super.create(model, URL, RequestListener, Customer.class, false);
    }

    @Override
    public void read(long id, RequestListener<Customer> RequestListener) {
        super.read(id, URL, RequestListener, Customer.class, true);
    }

    @Override
    public void readAll(RequestListener<Collection<Customer>> RequestListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(Customer model, RequestListener<Customer> RequestListener) {
        super.update(model, URL, RequestListener, Customer.class, true);
    }

    @Override
    public void delete(long id, RequestListener<Boolean> RequestListener) {
        super.delete(id, URL, RequestListener, true);
    }

}

