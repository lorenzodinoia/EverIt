package it.uniba.di.sms1920.everit.utils.request;

import java.util.Collection;

import it.uniba.di.sms1920.everit.utils.models.Product;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.core.CRUD;
import it.uniba.di.sms1920.everit.utils.request.core.CRUDRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public final class ProductRequest extends CRUDRequest<Product> implements CRUD<Product> {

    private final String RESTAURATEUR = "restaurateur";
    private final String CATEGORY = "/productCategory";
    private final String PRODUCT = "/product";

    @Override
    public void create(Product model, RequestListener<Product> RequestListener) {
        super.create(model, RESTAURATEUR + CATEGORY + "/" + Long.toString(model.getCategory().getId()) + PRODUCT, RequestListener,  Product.class, true);
    }

    @Override
    public void read(long id, RequestListener<Product> RequestListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void readAll(RequestListener<Collection<Product>> RequestListener) {
        super.readAll( RESTAURATEUR + "/" + Long.toString(Providers.getAuthProvider().getUser().getId()) + CATEGORY + PRODUCT, RequestListener, Product.class, true);
    }

    @Override
    public void update(Product model, RequestListener<Product> RequestListener) {
       super.update(model, RESTAURATEUR + CATEGORY + PRODUCT + "/" + Long.toString(model.getId()), RequestListener, Product.class, true);
    }

    @Override
    public void delete(long id, RequestListener<Boolean> RequestListener) {
        super.delete(id, RESTAURATEUR + CATEGORY + PRODUCT + "/" + Long.toString(id), RequestListener, true);
    }

}
