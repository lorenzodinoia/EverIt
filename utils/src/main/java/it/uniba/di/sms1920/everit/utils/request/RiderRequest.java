package it.uniba.di.sms1920.everit.utils.request;

import java.util.Collection;

import it.uniba.di.sms1920.everit.utils.models.Rider;
import it.uniba.di.sms1920.everit.utils.request.core.CRUD;
import it.uniba.di.sms1920.everit.utils.request.core.CRUDRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class RiderRequest extends CRUDRequest<Rider> implements CRUD<Rider> {
    private final String URL = "rider";

    @Override
    public void create(Rider model, RequestListener<Rider> RequestListener) {
        super.create(model, URL, RequestListener, Rider.class, false);
    }

    @Override
    public void read(long id, RequestListener<Rider> RequestListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void readAll(RequestListener<Collection<Rider>> RequestListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(Rider model, RequestListener<Rider> RequestListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(long id, RequestListener<Boolean> RequestListener) {
        throw new UnsupportedOperationException();
    }
}
