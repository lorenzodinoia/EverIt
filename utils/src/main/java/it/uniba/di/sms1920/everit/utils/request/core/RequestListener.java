package it.uniba.di.sms1920.everit.utils.request.core;

public interface RequestListener<T> {
    void successResponse(T response);
    void errorResponse(RequestException error);
}
