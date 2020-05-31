package it.uniba.di.sms1920.everit.utils.request;

import java.util.Collection;

import it.uniba.di.sms1920.everit.utils.models.ShopType;
import it.uniba.di.sms1920.everit.utils.request.core.CRUD;
import it.uniba.di.sms1920.everit.utils.request.core.CRUDRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class ShopTypeRequest extends CRUDRequest<ShopType> implements CRUD<ShopType> {

    private final String URL = "shopType";

    @Override
    public void create(ShopType model, RequestListener<ShopType> RequestListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void read(long id, RequestListener<ShopType> RequestListener) {
        super.read(id, URL, RequestListener, ShopType.class, false);
    }

    @Override
    public void readAll(RequestListener<Collection<ShopType>> RequestListener) {
        super.readAll(URL, RequestListener, ShopType.class, false);
    }

    @Override
    public void update(ShopType model, RequestListener<ShopType> RequestListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(long id, RequestListener<Boolean> RequestListener) {
        throw new UnsupportedOperationException();
    }
}
