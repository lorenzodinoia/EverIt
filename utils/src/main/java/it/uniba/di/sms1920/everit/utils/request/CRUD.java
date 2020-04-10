package it.uniba.di.sms1920.everit.utils.request;

import java.util.Collection;

import it.uniba.di.sms1920.everit.utils.models.Model;

public interface CRUD<T extends Model> {

    void create(T model, RequestListener<T> RequestListener);

    void read(long id, RequestListener<T> RequestListener);

    void readAll(RequestListener<Collection<T>> RequestListener);

    void update(T model, RequestListener<T> RequestListener);

    void delete(long id, RequestListener<Boolean> RequestListener);
}
