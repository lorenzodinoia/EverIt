package it.uniba.di.sms1920.everit.utils.request;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.adapter.Adapter;
import it.uniba.di.sms1920.everit.utils.models.OpeningTime;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.models.User;
import it.uniba.di.sms1920.everit.utils.provider.AdapterProvider;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.core.ArrayRequest;
import it.uniba.di.sms1920.everit.utils.request.core.CRUDRequest;
import it.uniba.di.sms1920.everit.utils.request.core.CRUD;
import it.uniba.di.sms1920.everit.utils.request.core.MultipartRequest;
import it.uniba.di.sms1920.everit.utils.request.core.ObjectRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestExceptionFactory;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public final class RestaurateurRequest extends CRUDRequest<Restaurateur> implements CRUD<Restaurateur> {

    private final String URL = "restaurateur";
    private final String IMAGE = "image";

    private static class Keys{
        private static final String KEY_OLD_PASSWORD = "old_password";
        private static final String KEY_NEW_PASSWORD = "new_password";
    }

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
        super.update(model, URL+"/update", RequestListener, Restaurateur.class, true);
    }

    @Override
    public void delete(long id, RequestListener<Boolean> RequestListener) {
        super.delete(id, URL+"/delete", RequestListener, true);
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
                        requestListener.errorResponse(new RequestException(null, e.getMessage()));
                    }
                },
                error ->
                    requestListener.errorResponse(RequestExceptionFactory.createExceptionFromError(error))
                );

        Providers.getRequestProvider().addToQueue(request);
    }

    public void saveImage(Uri uri, Context context, RequestListener<String> requestListener) {

        Map<String, String> headers = new HashMap<>();
        headers.put("Expect", "application/json");
        headers.put("Accept", "application/json");
        headers.put("Authorization", Providers.getAuthProvider().getAuthToken());

        //Setting listener
        //TODO gestire onResponse e onErrorResponse
        MultipartRequest request = new MultipartRequest(String.format("%s/api/%s/%s", Constants.SERVER_HOST, URL, IMAGE), headers,
                response -> {
                    Log.d("test", new String(response.data));
                    requestListener.successResponse(response.toString());
                },
                error -> Log.d("test", new String(error.networkResponse.data)));

        //Creating image bitmap and convert it into bytes array
        Bitmap bmp = null;
        try {
            bmp = getBitmapFromUri(uri, context);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bmp.recycle();

        //Creating request and adding to queue
        request.addPart(new MultipartRequest.FilePart("image", "image/jpg", "image.jpg", byteArray));
        Providers.getRequestProvider().addToQueue(request);
    }

    private Bitmap getBitmapFromUri(Uri uri, Context context) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                context.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    public void getCurrentUser(RequestListener requestListener){
        Adapter<Restaurateur> adapter = AdapterProvider.getAdapterFor(Restaurateur.class);
        try {
            ObjectRequest request = new ObjectRequest(Request.Method.GET, String.format("%s/api/%s/read/current", Constants.SERVER_HOST, URL), null,
                    response -> {
                        Restaurateur data = adapter.fromJSON(response, Restaurateur.class);
                        requestListener.successResponse(data);
                    },
                    error -> {
                        requestListener.errorResponse(RequestExceptionFactory.createExceptionFromError(error));
                    }, Providers.getAuthProvider().getAuthToken());

            Providers.getRequestProvider().addToQueue(request);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setShopName(Restaurateur model, RequestListener requestListener){
        Adapter<Restaurateur> adapter = AdapterProvider.getAdapterFor(Restaurateur.class);

        try {
            JSONObject jsonObject = adapter.toJSON(model);

            ObjectRequest request = new ObjectRequest(Request.Method.PUT, String.format("%s/api/%s/update/shopName", Constants.SERVER_HOST, URL), jsonObject,
                    response -> {
                        Restaurateur data = adapter.fromJSON(response, Restaurateur.class);
                        requestListener.successResponse(data);
                    },
                    error ->requestListener.errorResponse(RequestExceptionFactory.createExceptionFromError(error)),
                    Providers.getAuthProvider().getAuthToken());

            Providers.getRequestProvider().addToQueue(request);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setEmail(Restaurateur model, RequestListener requestListener){
        Adapter<Restaurateur> adapter = AdapterProvider.getAdapterFor(Restaurateur.class);

        try {
            JSONObject jsonObject = adapter.toJSON(model);

            ObjectRequest request = new ObjectRequest(Request.Method.PUT, String.format("%s/api/%s/update/email", Constants.SERVER_HOST, URL), jsonObject,
                    response -> {
                        Restaurateur data = adapter.fromJSON(response, Restaurateur.class);
                        requestListener.successResponse(data);
                    },
                    error ->requestListener.errorResponse(RequestExceptionFactory.createExceptionFromError(error)),
                    Providers.getAuthProvider().getAuthToken());

            Providers.getRequestProvider().addToQueue(request);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void changePassword(String oldPassword, String newPassword, RequestListener<Boolean> requestListener) {
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put(RestaurateurRequest.Keys.KEY_OLD_PASSWORD, oldPassword);
            jsonObject.put(RestaurateurRequest.Keys.KEY_NEW_PASSWORD, newPassword);

            ObjectRequest request = new ObjectRequest(Request.Method. PUT, String.format("%s/api/%s/update/changePassword", Constants.SERVER_HOST, URL), jsonObject,
                    response -> {
                        requestListener.successResponse(true);
                    },
                    error -> {
                        requestListener.errorResponse(RequestExceptionFactory.createExceptionFromError(error));
                    }, Providers.getAuthProvider().getAuthToken());

            Providers.getRequestProvider().addToQueue(request);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
