package it.uniba.di.sms1920.everit.utils.request;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.adapter.Adapter;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.provider.AdapterProvider;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.core.ArrayRequest;
import it.uniba.di.sms1920.everit.utils.request.core.CRUDRequest;
import it.uniba.di.sms1920.everit.utils.request.core.CRUD;
import it.uniba.di.sms1920.everit.utils.request.core.MultipartRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestExceptionFactory;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public final class RestaurateurRequest extends CRUDRequest<Restaurateur> implements CRUD<Restaurateur> {

    private final String URL = "restaurateur";
    private final String IMAGE = "image";

    //TODO implementare altre funzioni

    @Override
    public void create(Restaurateur model, RequestListener<Restaurateur> RequestListener) {
        super.create(model, URL, RequestListener, Restaurateur.class, false);
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

    public void saveImage(File file, RequestListener<String> requestListener) throws IOException {

        /*ImageRequest imageRequest = new ImageRequest(URL+"/"+IMAGE,
                response -> {
                String resultResponse = new String(response.data);
                    try{
                        JSONObject result = new JSONObject(resultResponse);
                        String status = result.getString("status");
                        String message = result.getString("message");
                        requestListener.successResponse(message);
                    } catch(JSONException e){
                        requestListener.successResponse(e.getMessage());
                    }
                },
                error -> {
                    requestListener.errorResponse(RequestExceptionFactory.createExceptionFromError(error));
                }, Providers.getAuthProvider().getAuthToken(), file);

        Providers.getRequestProvider().addToQueue(imageRequest);
    }*/
        Map<String, String> headers = new HashMap<>();
        headers.put("Expect", "application/json");
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");

        MultipartRequest request = new MultipartRequest(String.format("%s/api/%s/%s", Constants.SERVER_HOST, URL, IMAGE), headers,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        //TODO gestire risposta
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //TODO gestire risposta
                    }
                });
        byte[] arrayFile = new byte[(int) file.length()];
        FileInputStream inputStream = new FileInputStream(file);
        inputStream.read(arrayFile);
        String extension = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("."));
        request.addPart(new MultipartRequest.FilePart("image", extension, file.getName(), arrayFile));

        Providers.getRequestProvider().addToQueue(request);
    }

}
