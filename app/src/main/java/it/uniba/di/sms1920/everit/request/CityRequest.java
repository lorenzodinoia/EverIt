package it.uniba.di.sms1920.everit.request;

import java.util.Collection;

import it.uniba.di.sms1920.everit.models.City;

public final class CityRequest extends CRUDRequest<City> implements  CRUD<City>{

    private final String URL = "city";

    @Override
    public void create(City model, RequestListener<City> requestListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void read(long id, RequestListener<City> requestListener) {
        super.read(id, URL, requestListener, City.class,false);
    }

    @Override
    public void readAll(RequestListener<Collection<City>> requestListener) {
        super.readAll(URL, requestListener, City.class, false);
    }

    @Override
    public void update(City model, RequestListener<City> requestListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(long id, RequestListener<Boolean> requestListener) {
        throw new UnsupportedOperationException();
    }
}
