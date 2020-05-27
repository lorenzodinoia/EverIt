package it.uniba.di.sms1920.everit.utils.request;

import java.util.Collection;
import java.util.Locale;

import it.uniba.di.sms1920.everit.utils.models.ProductCategory;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.core.CRUDRequest;
import it.uniba.di.sms1920.everit.utils.request.core.CRUD;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public final class ProductCategoryRequest extends CRUDRequest<ProductCategory> implements CRUD<ProductCategory> {

    //TODO modificare url del readAll se si utilizza Read
    private final String RESTAURATEUR = "restaurateur";
    private final String PRODCUTCATEGORY = "/productCategory";

    @Override
    public void create(ProductCategory model, RequestListener<ProductCategory> RequestListener) {
        super.create(model, RESTAURATEUR + PRODCUTCATEGORY, RequestListener, ProductCategory.class, true);
    }

    @Override
    public void read(long id, RequestListener<ProductCategory> RequestListener) {
        throw  new UnsupportedOperationException();
    }

    @Override
    public void readAll(RequestListener<Collection<ProductCategory>> RequestListener) {
        super.readAll(RESTAURATEUR + "/" + Long.toString(Providers.getAuthProvider().getUser().getId()) + PRODCUTCATEGORY, RequestListener, ProductCategory.class,false);
    }

    @Override
    public void update(ProductCategory model, RequestListener<ProductCategory> RequestListener) {
        super.update(model, RESTAURATEUR + PRODCUTCATEGORY + "/" + Long.toString(model.getId()), RequestListener, ProductCategory.class, true);
    }

    @Override
    public void delete(long id, RequestListener<Boolean> RequestListener) {
        super.delete(id, RESTAURATEUR + PRODCUTCATEGORY + "/" + Long.toString(id), RequestListener, true);
    }
}
