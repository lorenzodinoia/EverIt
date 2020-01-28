package it.uniba.di.sms1920.everit.request;

import it.uniba.di.sms1920.everit.models.Customer;

public class CustomerRequest extends CRUDRequest<Customer> implements CRUD<Customer> {

    private final String URL = "customer";

    @Override
    public void create(Customer model, RequestListener<Customer> requestListener) {
        super.create(model, URL, requestListener, Customer.class, false);
    }

    @Override
    public void read(long id, RequestListener<Customer> requestListener) {
        super.read(id, URL, requestListener, Customer.class, true);
    }

    @Override
    public void readAll(RequestListener<Customer> requestListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(Customer model, RequestListener<Customer> requestListener) {
        super.update(model, URL, requestListener, Customer.class, true);
    }

    @Override
    public void delete(long id, RequestListener<Boolean> requestListener) {
        super.delete(id, URL, requestListener, true);
    }

}
