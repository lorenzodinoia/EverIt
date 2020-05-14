package it.uniba.di.sms1920.everit.utils.request;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Locale;

import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.adapter.Adapter;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.provider.AdapterProvider;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.core.ArrayRequest;
import it.uniba.di.sms1920.everit.utils.request.core.CRUDRequest;
import it.uniba.di.sms1920.everit.utils.request.core.CRUD;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestExceptionFactory;
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

    public void search(double latitude, double longitude, RequestListener<Collection<Restaurateur>> requestListener) {
        Adapter<Restaurateur> adapter = AdapterProvider.getAdapterFor(Restaurateur.class);

        String serverUrl = String.format(Locale.getDefault(), "%s/api/%s/searchNearby/%f/%f", Constants.SERVER_HOST, URL, latitude, longitude);
        serverUrl = serverUrl.replace(',', '.');

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("latitude", latitude);
            jsonObject.put("longitude", longitude);
        }
        catch (JSONException e) {
            e.printStackTrace();
            return;
            //TODO Gestire eccezione
        }

        ArrayRequest request = new ArrayRequest(Request.Method.GET, serverUrl, jsonObject,
                response -> {
                    try {
                        Collection<Restaurateur> results = adapter.fromJSONArray(response, Restaurateur.class);
                        requestListener.successResponse(results);
                    }
                    catch (JSONException e) {
                        requestListener.errorResponse(new RequestException(e.getMessage()));
                    }
                },
                error ->
                    requestListener.errorResponse(RequestExceptionFactory.createExceptionFromError(error))
                );

        Providers.getRequestProvider().addToQueue(request);
    }
}
