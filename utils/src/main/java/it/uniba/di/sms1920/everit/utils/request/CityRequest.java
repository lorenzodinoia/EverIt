package it.uniba.di.sms1920.everit.utils.request;

import java.util.Collection;

import it.uniba.di.sms1920.everit.utils.models.City;
import it.uniba.di.sms1920.everit.utils.request.core.CRUD;
import it.uniba.di.sms1920.everit.utils.request.core.CRUDRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public final class CityRequest extends CRUDRequest<City> implements CRUD<City> {

    private final String URL = "city";

    @Override
    public void create(City model, RequestListener<City> RequestListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void read(long id, RequestListener<City> RequestListener) {
        super.read(id, URL, RequestListener, City.class,false);
    }

    @Override
    public void readAll(RequestListener<Collection<City>> RequestListener) {
        super.readAll(URL, RequestListener, City.class, false);
    }

    @Override
    public void update(City model, RequestListener<City> RequestListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(long id, RequestListener<Boolean> RequestListener) {
        throw new UnsupportedOperationException();
    }
}
