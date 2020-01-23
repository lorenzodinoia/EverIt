package it.uniba.di.sms1920.everit.request;

public interface RequestListener<T> {
    void successResponse(T response);
    void errorResponse(String error);
}
