package it.uniba.di.sms1920.everit.utils.request;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Collection;

import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.adapter.Adapter;
import it.uniba.di.sms1920.everit.utils.adapter.AdapterProvider;
import it.uniba.di.sms1920.everit.utils.models.Model;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.models.User;

//TODO Controllare che AuthProvider sia diverso da NULL

abstract class CRUDRequest<T extends Model> {

    void create(T model, String url, RequestListener<T> RequestListener, Class<T> modelType, boolean needToken) {
        Adapter<T> adapter = AdapterProvider.getAdapterFor(modelType);
        try {
            JSONObject jsonObject = adapter.toJSON(model);

            ObjectRequest request = new ObjectRequest(Request.Method.POST, String.format("%s/api/%s", Constants.SERVER_HOST, url), jsonObject,
                response -> {
                    T data = adapter.fromJSON(response, modelType);
                    RequestListener.successResponse(data);
                    if (model instanceof User) {
                        ((User) model).setPassword(null);
                    }
                    else if (model instanceof Restaurateur) {
                        ((Restaurateur) model).setPassword(null);
                    }
                },
                error -> {
                    String string = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    RequestListener.errorResponse(string);
                }, ((needToken) ? AuthProvider.getInstance().getAuthToken() : null));

            RequestManager.getInstance().addToQueue(request);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void read(long id, String url, RequestListener<T> RequestListener, Class<T> modelType, boolean needToken) {
        Adapter<T> adapter = AdapterProvider.getAdapterFor(modelType);

        ObjectRequest request = new ObjectRequest(Request.Method.GET, String.format("%s/api/%s/%d", Constants.SERVER_HOST, url, id), null,
            response -> {
                T data = adapter.fromJSON(response, modelType);
                RequestListener.successResponse(data);
            },
            error -> {
                String string = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                RequestListener.errorResponse(string);
            }, ((needToken) ? AuthProvider.getInstance().getAuthToken() : null));

        RequestManager.getInstance().addToQueue(request);
    }

    void readAll(String url, RequestListener<Collection<T>> RequestListener, Class<T> modelType, boolean needToken) {
        Adapter<T> adapter = AdapterProvider.getAdapterFor(modelType);

        ArrayRequest request = new ArrayRequest(Request.Method.GET, String.format("%s/api/%s", Constants.SERVER_HOST, url), null,
            response -> {
                try {
                    Collection<T> collection = adapter.fromJSONArray(response, modelType);
                    RequestListener.successResponse(collection);
                } catch (JSONException e) {
                    RequestListener.errorResponse(e.getMessage());
                }
            },
            error -> {
                String string = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                RequestListener.errorResponse(string);
            }, ((needToken) ? AuthProvider.getInstance().getAuthToken() : null));

        RequestManager.getInstance().addToQueue(request);
    }

    void update(T model, String url, RequestListener<T> RequestListener, Class<T> modelType, boolean needToken) {
        Adapter<T> adapter = AdapterProvider.getAdapterFor(modelType);

        try {
            JSONObject jsonObject = adapter.toJSON(model);

            ObjectRequest request = new ObjectRequest(Request.Method.PUT, String.format("%s/api/%s", Constants.SERVER_HOST, url), jsonObject,
                response -> {
                    T data = adapter.fromJSON(response, modelType);
                    RequestListener.successResponse(data);
                    if (model instanceof User) {
                        ((User) model).setPassword(null);
                    }
                    else if (model instanceof Restaurateur) {
                        ((Restaurateur) model).setPassword(null);
                    }
                },
                error -> {
                    String string = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    RequestListener.errorResponse(string);
                }, ((needToken) ? AuthProvider.getInstance().getAuthToken() : null));

            RequestManager.getInstance().addToQueue(request);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void delete(long id, String url, RequestListener<Boolean> RequestListener, boolean needToken) {
        ObjectRequest request = new ObjectRequest(Request.Method.DELETE, String.format("%s/api/$s", Constants.SERVER_HOST, url), null,
            response -> RequestListener.successResponse(true), error -> RequestListener.errorResponse("Can't delete account"),
                ((needToken) ? AuthProvider.getInstance().getAuthToken() : null));
        RequestManager.getInstance().addToQueue(request);
    }
}
