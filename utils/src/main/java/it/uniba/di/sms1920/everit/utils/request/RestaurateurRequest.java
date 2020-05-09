package it.uniba.di.sms1920.everit.utils.request;

import java.util.Collection;

import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.request.core.CRUDRequest;
import it.uniba.di.sms1920.everit.utils.request.core.CRUD;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public final class RestaurateurRequest extends CRUDRequest<Restaurateur> implements CRUD<Restaurateur> {

    private final String URL = "restaurateur";

    @Override
    public void create(Restaurateur model, RequestListener<Restaurateur> RequestListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void read(long id, RequestListener<Restaurateur> RequestListener) {
        super.read(id, URL, RequestListener, Restaurateur.class, false);
    }

    @Override
    public void readAll(RequestListener<Collection<Restaurateur>> RequestListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(Restaurateur model, RequestListener<Restaurateur> RequestListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(long id, RequestListener<Boolean> RequestListener) {
        throw new UnsupportedOperationException();
    }
}
