package it.uniba.di.sms1920.everit.utils.request;

import java.util.Collection;

import it.uniba.di.sms1920.everit.utils.models.Model;
import it.uniba.di.sms1920.everit.utils.models.Product;
import it.uniba.di.sms1920.everit.utils.request.core.CRUDRequest;
import it.uniba.di.sms1920.everit.utils.request.core.CRUD;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public final class ProductRequest extends CRUDRequest<Product> implements CRUD<Product> {

    private final String URL = "restaurateur/productCategory";
    private final String PRODUCT = "/product";

    @Override
    public void create(Product model, RequestListener<Product> RequestListener) {
        super.create(model, URL + Long.toString(model.getCategory().getId()) + PRODUCT, RequestListener,  Product.class, true);
    }

    @Override
    public void read(long id, RequestListener<Product> RequestListener) {
    }

    @Override
    public void readAll(RequestListener<Collection<Product>> RequestListener) {
    }

    @Override
    public void update(Product model, RequestListener<Product> RequestListener) {
    }

    @Override
    public void delete(long id, RequestListener<Boolean> RequestListener) {
    }




}
