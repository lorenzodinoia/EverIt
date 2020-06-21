package it.uniba.di.sms1920.everit.utils.request.core;

import android.util.Log;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;

import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.adapter.Adapter;
import it.uniba.di.sms1920.everit.utils.provider.AdapterProvider;
import it.uniba.di.sms1920.everit.utils.models.Model;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.models.User;
import it.uniba.di.sms1920.everit.utils.provider.Providers;

public abstract class CRUDRequest<T extends Model> {

    protected void create(T model, String url, RequestListener<T> requestListener, Class<T> modelType, boolean needToken) {
        Adapter<T> adapter = AdapterProvider.getAdapterFor(modelType);
        try {
            JSONObject jsonObject = adapter.toJSON(model);

            ObjectRequest request = new ObjectRequest(Request.Method.POST, String.format("%s/api/%s", Constants.SERVER_HOST, url), jsonObject,
                response -> {
                    T data = adapter.fromJSON(response, modelType);
                    requestListener.successResponse(data);
                    if (model instanceof User) {
                        ((User) model).setPassword(null);
                    }
                    else if (model instanceof Restaurateur) {
                        ((Restaurateur) model).setPassword(null);
                    }
                },
                error -> {
                    requestListener.errorResponse(RequestExceptionFactory.createExceptionFromError(error));
                }, ((needToken) ? Providers.getAuthProvider().getAuthToken() : null));

            Providers.getRequestProvider().addToQueue(request);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void read(long id, String url, RequestListener<T> requestListener, Class<T> modelType, boolean needToken) {
        Adapter<T> adapter = AdapterProvider.getAdapterFor(modelType);

        ObjectRequest request = new ObjectRequest(Request.Method.GET, String.format("%s/api/%s/%d", Constants.SERVER_HOST, url, id), null,
            response -> {
                T data = adapter.fromJSON(response, modelType);
                requestListener.successResponse(data);
            },
            error -> {
                requestListener.errorResponse(RequestExceptionFactory.createExceptionFromError(error));
            }, ((needToken) ? Providers.getAuthProvider().getAuthToken() : null));

        Providers.getRequestProvider().addToQueue(request);
    }

    protected void readAll(String url, RequestListener<Collection<T>> requestListener, Class<T> modelType, boolean needToken) {
        Adapter<T> adapter = AdapterProvider.getAdapterFor(modelType);

        ArrayRequest request = new ArrayRequest(Request.Method.GET, String.format("%s/api/%s", Constants.SERVER_HOST, url), null,
            response -> {
                try {
                    Collection<T> collection = adapter.fromJSONArray(response, modelType);
                    requestListener.successResponse(collection);
                }
                catch (JSONException e) {
                    requestListener.errorResponse(new RequestException(e.getMessage()));
                }
            },
            error -> requestListener.errorResponse(RequestExceptionFactory.createExceptionFromError(error)),
                ((needToken) ? Providers.getAuthProvider().getAuthToken() : null));

        Providers.getRequestProvider().addToQueue(request);
    }

    protected void update(T model, String url, RequestListener<T> requestListener, Class<T> modelType, boolean needToken) {
        Adapter<T> adapter = AdapterProvider.getAdapterFor(modelType);

        try {
            JSONObject jsonObject = adapter.toJSON(model);

            ObjectRequest request = new ObjectRequest(Request.Method.PUT, String.format("%s/api/%s", Constants.SERVER_HOST, url), jsonObject,
                response -> {
                    T data = adapter.fromJSON(response, modelType);
                    requestListener.successResponse(data);
                    if (model instanceof User) {
                        ((User) model).setPassword(null);
                    }
                    else if (model instanceof Restaurateur) {
                        ((Restaurateur) model).setPassword(null);
                    }
                },
                error ->requestListener.errorResponse(RequestExceptionFactory.createExceptionFromError(error)),
                           ((needToken) ? Providers.getAuthProvider().getAuthToken() : null));

            Providers.getRequestProvider().addToQueue(request);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void delete(long id, String url, RequestListener<Boolean> RequestListener, boolean needToken) {
        ObjectRequest request = new ObjectRequest(Request.Method.DELETE, String.format("%s/api/%s", Constants.SERVER_HOST, url), null,
            response -> RequestListener.successResponse(true),
            error -> RequestListener.errorResponse(RequestExceptionFactory.createExceptionFromError(error)),
                ((needToken) ? Providers.getAuthProvider().getAuthToken() : null));

        Providers.getRequestProvider().addToQueue(request);
    }
}
