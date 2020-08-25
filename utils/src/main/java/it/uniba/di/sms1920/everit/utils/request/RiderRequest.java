package it.uniba.di.sms1920.everit.utils.request;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.adapter.Adapter;
import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.models.Rider;
import it.uniba.di.sms1920.everit.utils.provider.AdapterProvider;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.core.ArrayRequest;
import it.uniba.di.sms1920.everit.utils.request.core.CRUD;
import it.uniba.di.sms1920.everit.utils.request.core.CRUDRequest;
import it.uniba.di.sms1920.everit.utils.request.core.ObjectRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestExceptionFactory;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class RiderRequest extends CRUDRequest<Rider> implements CRUD<Rider> {

    private static final class JSONKeys {
        private static final String KEY_OLD_PASSWORD = "old_password";
        private static final String KEY_NEW_PASSWORD = "new_password";
    }

    private final String URL = "rider";

    @Override
    public void create(Rider model, RequestListener<Rider> RequestListener) {
        super.create(model, URL, RequestListener, Rider.class, false);
    }

    @Override
    public void read(long id, RequestListener<Rider> RequestListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void readAll(RequestListener<Collection<Rider>> RequestListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(Rider model, RequestListener<Rider> RequestListener) {
        super.update(model, URL+"/update", RequestListener, Rider.class, true);
    }

    @Override
    public void delete(long id, RequestListener<Boolean> RequestListener) {
        super.delete(id, URL+"/delete", RequestListener, true);
    }

    public void readCurrent(RequestListener<Rider> requestListener){
        super.read(Providers.getAuthProvider().getUser().getId(), URL, requestListener, Rider.class, true);
    }

    public void readAssignedOrders(RequestListener<Collection<Order>> requestListener) {
        Adapter<Order> orderAdapter = AdapterProvider.getAdapterFor(Order.class);
        ArrayRequest assignedOrdersRequest = new ArrayRequest(Request.Method.GET, String.format(Locale.getDefault(), "%s/api/%s/order/assigned", Constants.SERVER_HOST, URL), null,
                response -> {
                    try {
                        Collection<Order> resultList = orderAdapter.fromJSONArray(response, Order.class);
                        requestListener.successResponse(resultList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        requestListener.errorResponse(new RequestException(null, e.getMessage()));
                    }
                },
                error -> {
                    requestListener.errorResponse(RequestExceptionFactory.createExceptionFromError(error));
                }, Providers.getAuthProvider().getAuthToken());

        Providers.getRequestProvider().addToQueue(assignedOrdersRequest);
    }

    public void readAssignedOrder(long id, RequestListener<Order> requestListener) {
        Adapter<Order> orderAdapter = AdapterProvider.getAdapterFor(Order.class);
        ObjectRequest assignedOrdersRequest = new ObjectRequest(Request.Method.GET, String.format(Locale.getDefault(), "%s/api/%s/order/assigned/%d", Constants.SERVER_HOST, URL, id), null,
                response -> {
                    Order result = orderAdapter.fromJSON(response, Order.class);
                    requestListener.successResponse(result);
                },
                error -> {
                    requestListener.errorResponse(RequestExceptionFactory.createExceptionFromError(error));
                }, Providers.getAuthProvider().getAuthToken());

        Providers.getRequestProvider().addToQueue(assignedOrdersRequest);
    }

    public void pickupOrder(long id, double riderLatitude, double riderLongitude, RequestListener<Boolean> requestListener) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("latitude", riderLatitude);
            jsonObject.put("longitude", riderLongitude);

            ObjectRequest request = new ObjectRequest(Request.Method.POST, String.format(Locale.getDefault(), "%s/api/%s/order/%d/confirmLocation", Constants.SERVER_HOST, URL, id), jsonObject,
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

    public void readDeliveries(RequestListener<Collection<Order>> requestListener) {
        Adapter<Order> orderAdapter = AdapterProvider.getAdapterFor(Order.class);
        ArrayRequest assignedOrdersRequest = new ArrayRequest(Request.Method.GET, String.format(Locale.getDefault(), "%s/api/%s/order/deliveries", Constants.SERVER_HOST, URL), null,
                response -> {
                    try {
                        Collection<Order> resultList = orderAdapter.fromJSONArray(response, Order.class);
                        requestListener.successResponse(resultList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        requestListener.errorResponse(new RequestException(null, e.getMessage()));
                    }
                },
                error -> {
                    requestListener.errorResponse(RequestExceptionFactory.createExceptionFromError(error));
                }, Providers.getAuthProvider().getAuthToken());

        Providers.getRequestProvider().addToQueue(assignedOrdersRequest);
    }

    public void readDelivery(long id, RequestListener<Order> requestListener) {
        Adapter<Order> orderAdapter = AdapterProvider.getAdapterFor(Order.class);
        ObjectRequest assignedOrdersRequest = new ObjectRequest(Request.Method.GET, String.format(Locale.getDefault(), "%s/api/%s/order/deliveries/%d", Constants.SERVER_HOST, URL, id), null,
                response -> {
                    Order result = orderAdapter.fromJSON(response, Order.class);
                    requestListener.successResponse(result);
                },
                error -> {
                    requestListener.errorResponse(RequestExceptionFactory.createExceptionFromError(error));
                }, Providers.getAuthProvider().getAuthToken());

        Providers.getRequestProvider().addToQueue(assignedOrdersRequest);
    }

    public void changePassword(String oldPassword, String newPassword, RequestListener<Boolean> requestListener){
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put(RiderRequest.JSONKeys.KEY_OLD_PASSWORD, oldPassword);
            jsonObject.put(RiderRequest.JSONKeys.KEY_NEW_PASSWORD, newPassword);

            ObjectRequest request = new ObjectRequest(Request.Method.POST, String.format("%s/api/%s/changePassword", Constants.SERVER_HOST, URL), jsonObject,
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
