package it.uniba.di.sms1920.everit.request;

import it.uniba.di.sms1920.everit.models.Customer;

public class CustomerRequest extends CRUDRequest<Customer> implements CRUD<Customer> {
    @Override
    public void create(Customer model, RequestListener<Customer> requestListener) {
        super.create(model, "customer", requestListener, Customer.class, false);
    }

    @Override
    public void read(long id, RequestListener<Customer> requestListener) {

    }

    @Override
    public void readAll(RequestListener<Customer> requestListener) {

    }

    @Override
    public void update(Customer model, RequestListener<Customer> requestListener) {

    }

    @Override
    public void delete(long id, RequestListener<Customer> requestListener) {

    }

    public void login(String email, String password, RequestListener<Customer> requestListener) {

    }
}
