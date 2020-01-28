package it.uniba.di.sms1920.everit.request;

import android.util.Log;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import it.uniba.di.sms1920.everit.Constants;
import it.uniba.di.sms1920.everit.adapter.Adapter;
import it.uniba.di.sms1920.everit.adapter.AdapterProvider;
import it.uniba.di.sms1920.everit.models.Model;
import it.uniba.di.sms1920.everit.models.Restaurateur;
import it.uniba.di.sms1920.everit.models.User;

abstract class CRUDRequest<T extends Model> {

    void create(T model, String url, RequestListener<T> requestListener, Class<T> modelType, boolean needToken) {
        Adapter<T> adapter = AdapterProvider.getAdapterFor(modelType);
        try {
            JSONObject jsonObject = adapter.toJSON(model);

            CustomRequest request = new CustomRequest(Request.Method.POST, String.format("%s/api/%s", Constants.SERVER_HOST, url), jsonObject,
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
                    String string = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    requestListener.errorResponse(string);
            }, ((needToken) ? AuthProvider.getInstance().getAuthToken() : null));

            RequestManager.getInstance().addToQueue(request);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void read(long id, String url, RequestListener<T> requestListener, Class<T> modelType, boolean needToken) {
        Adapter<T> adapter = AdapterProvider.getAdapterFor(modelType);

        CustomRequest request = new CustomRequest(Request.Method.GET, String.format("%s/api/%s/%d", Constants.SERVER_HOST, url, id), null,
            response -> {
                T data = adapter.fromJSON(response, modelType);
                requestListener.successResponse(data);
            },
            error -> {
                String string = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                requestListener.errorResponse(string);
            }, ((needToken) ? AuthProvider.getInstance().getAuthToken() : null));

        RequestManager.getInstance().addToQueue(request);
    }

    void readAll(String url, RequestListener<Collection<T>> requestListener, Class<T> modelType, boolean needToken) {
        Adapter<T> adapter = AdapterProvider.getAdapterFor(modelType);

        CustomRequest request = new CustomRequest(Request.Method.GET, String.format("%s/api/%s", Constants.SERVER_HOST, url), null,
            response -> {
                Class arrayClass = Array.newInstance(modelType, 0).getClass();
                Log.d("ZIO", adapter.fromJSON(response, arrayClass).getClass().getCanonicalName());
                //TODO Lo scopriremo solo vivendo
            },
            error -> {
                String string = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                requestListener.errorResponse(string);
            }, ((needToken) ? AuthProvider.getInstance().getAuthToken() : null));

        RequestManager.getInstance().addToQueue(request);
    }

    void update(T model, String url, RequestListener<T> requestListener, Class<T> modelType, boolean needToken) {
        Adapter<T> adapter = AdapterProvider.getAdapterFor(modelType);
        try {
            JSONObject jsonObject = adapter.toJSON(model);

            CustomRequest request = new CustomRequest(Request.Method.PUT, String.format("%s/api/%s", Constants.SERVER_HOST, url), jsonObject,
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
                    String string = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    requestListener.errorResponse(string);
                }, ((needToken) ? AuthProvider.getInstance().getAuthToken() : null));

            RequestManager.getInstance().addToQueue(request);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void delete(long id, String url, RequestListener<Boolean> requestListener, boolean needToken) {
        CustomRequest request = new CustomRequest(Request.Method.DELETE, String.format("%s/api/$s", Constants.SERVER_HOST, url), null,
                response -> {
                    requestListener.successResponse(true);
                },
                error -> {
                    requestListener.errorResponse("Can't delete account");
                }, ((needToken) ? AuthProvider.getInstance().getAuthToken() : null));
        RequestManager.getInstance().addToQueue(request);
    }
}
