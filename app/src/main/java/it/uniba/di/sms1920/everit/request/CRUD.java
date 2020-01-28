package it.uniba.di.sms1920.everit.request;

import it.uniba.di.sms1920.everit.models.Model;

public interface CRUD<T extends Model> {
    void create(T model, RequestListener<T> requestListener);

    void read(long id, RequestListener<T> requestListener);

    void readAll(RequestListener<T> requestListener);

    void update(T model, RequestListener<T> requestListener);

    void delete(long id, RequestListener<Boolean> requestListener);
}
