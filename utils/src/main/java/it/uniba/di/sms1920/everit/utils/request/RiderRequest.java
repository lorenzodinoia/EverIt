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
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(long id, RequestListener<Boolean> RequestListener) {
        throw new UnsupportedOperationException();
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
                        requestListener.errorResponse(new RequestException(e.getMessage()));
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
            jsonObject.put("latitude", 16.305676);
            jsonObject.put("longitude", 41.13449);

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
                        requestListener.errorResponse(new RequestException(e.getMessage()));
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
}
