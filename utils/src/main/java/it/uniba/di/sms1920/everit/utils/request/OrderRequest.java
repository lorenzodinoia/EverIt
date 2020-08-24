package it.uniba.di.sms1920.everit.utils.request;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;

import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.adapter.Adapter;
import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.provider.AdapterProvider;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.core.ArrayRequest;
import it.uniba.di.sms1920.everit.utils.request.core.CRUD;
import it.uniba.di.sms1920.everit.utils.request.core.CRUDRequest;
import it.uniba.di.sms1920.everit.utils.request.core.ObjectRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestExceptionFactory;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public final class OrderRequest extends CRUDRequest<Order> implements CRUD<Order> {

    private final String CUSTOMER = "customer";
    private final String RESTAURATEUR = "restaurateur";
    private final String ORDER = "/order";

    @Override
    public void create(Order model, RequestListener<Order> RequestListener) {
        super.create(model, RESTAURATEUR+"/"+model.getRestaurateur().getId()+ORDER, RequestListener, Order.class, true);
    }

    @Override
    public void read(long id, RequestListener<Order> RequestListener) {
        super.read(id, CUSTOMER+ORDER, RequestListener, Order.class, true);
    }

    @Override
    public void readAll(RequestListener<Collection<Order>> RequestListener) {
        super.readAll(CUSTOMER+ORDER+"/getAll", RequestListener, Order.class, true);
    }

    @Override
    public void update(Order model, RequestListener<Order> RequestListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(long id, RequestListener<Boolean> RequestListener) {
        throw new UnsupportedOperationException();
    }

    public void readPendingOrders(RequestListener<Collection<Order>> requestListener){
        Adapter<Order> adapter = AdapterProvider.getAdapterFor(Order.class);

        ArrayRequest request = new ArrayRequest(Request.Method.GET, String.format("%s/api/%s%s/pending", Constants.SERVER_HOST, RESTAURATEUR, ORDER), null,
                response -> {
                    try {
                        Collection<Order> collection = adapter.fromJSONArray(response, Order.class);
                        requestListener.successResponse(collection);
                    }
                    catch (JSONException e) {
                        requestListener.errorResponse(new RequestException(e.getMessage()));
                    }
                },
                error -> requestListener.errorResponse(RequestExceptionFactory.createExceptionFromError(error)),
                Providers.getAuthProvider().getAuthToken());

        Providers.getRequestProvider().addToQueue(request);
    }

    public void readDoneOrders(RequestListener<Collection<Order>> requestListener){
        Adapter<Order> adapter = AdapterProvider.getAdapterFor(Order.class);

        ArrayRequest request = new ArrayRequest(Request.Method.GET, String.format("%s/api/%s%s/done", Constants.SERVER_HOST, RESTAURATEUR, ORDER), null,
                response -> {
                    try {
                        Collection<Order> collection = adapter.fromJSONArray(response, Order.class);
                        requestListener.successResponse(collection);
                    }
                    catch (JSONException e) {
                        requestListener.errorResponse(new RequestException(e.getMessage()));
                    }
                },
                error -> requestListener.errorResponse(RequestExceptionFactory.createExceptionFromError(error)),
                Providers.getAuthProvider().getAuthToken());

        Providers.getRequestProvider().addToQueue(request);
    }

    public void readToDoOrders(RequestListener<Collection<Order>> requestListener){
        Adapter<Order> adapter = AdapterProvider.getAdapterFor(Order.class);

        ArrayRequest request = new ArrayRequest(Request.Method.GET, String.format("%s/api/%s%s/toDo", Constants.SERVER_HOST, RESTAURATEUR, ORDER), null,
                response -> {
                    try {
                        Collection<Order> collection = adapter.fromJSONArray(response, Order.class);
                        requestListener.successResponse(collection);
                    }
                    catch (JSONException e) {
                        requestListener.errorResponse(new RequestException(e.getMessage()));
                    }
                },
                error -> requestListener.errorResponse(RequestExceptionFactory.createExceptionFromError(error)),
                Providers.getAuthProvider().getAuthToken());

        Providers.getRequestProvider().addToQueue(request);
    }

    public void markAsConfirmed(long orderId, RequestListener<Order> requestListener) {
        Adapter<Order> adapter = AdapterProvider.getAdapterFor(Order.class);
        try {
            ObjectRequest request = new ObjectRequest(Request.Method.POST, String.format("%s/api/%s%s/%d/markAsConfirmed", Constants.SERVER_HOST, RESTAURATEUR, ORDER, orderId), null,
                    response -> {
                        Order data = adapter.fromJSON(response, Order.class);
                        requestListener.successResponse(data);
                    },
                    error -> {
                        requestListener.errorResponse(RequestExceptionFactory.createExceptionFromError(error));
                    }, Providers.getAuthProvider().getAuthToken());

            Providers.getRequestProvider().addToQueue(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void markAsLate(long orderId, RequestListener<Order> requestListener){
        Adapter<Order> adapter = AdapterProvider.getAdapterFor(Order.class);
        try {
            ObjectRequest request = new ObjectRequest(Request.Method.POST, String.format("%s/api/%s%s/%d/markAsLate", Constants.SERVER_HOST, RESTAURATEUR, ORDER, orderId), null,
                    response -> {
                        Order data = adapter.fromJSON(response, Order.class);
                        requestListener.successResponse(data);
                    },
                    error -> {
                        requestListener.errorResponse(RequestExceptionFactory.createExceptionFromError(error));
                    }, Providers.getAuthProvider().getAuthToken());

            Providers.getRequestProvider().addToQueue(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void markAsInProgress(long orderId, RequestListener<Order> requestListener) {
        Adapter<Order> adapter = AdapterProvider.getAdapterFor(Order.class);
        try {
            ObjectRequest request = new ObjectRequest(Request.Method.POST, String.format("%s/api/%s%s/%d/markAsInProgress", Constants.SERVER_HOST, RESTAURATEUR, ORDER, orderId), null,
                    response -> {
                        Order data = adapter.fromJSON(response, Order.class);
                        requestListener.successResponse(data);
                    },
                    error -> {
                        requestListener.errorResponse(RequestExceptionFactory.createExceptionFromError(error));
                    }, Providers.getAuthProvider().getAuthToken());

            Providers.getRequestProvider().addToQueue(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void markAsReady(long orderId, RequestListener<Order> requestListener) {
        Adapter<Order> adapter = AdapterProvider.getAdapterFor(Order.class);
        try {
            ObjectRequest request = new ObjectRequest(Request.Method.POST, String.format("%s/api/%s%s/%d/markAsReady", Constants.SERVER_HOST, RESTAURATEUR, ORDER, orderId), null,
                    response -> {
                        Order data = adapter.fromJSON(response, Order.class);
                        requestListener.successResponse(data);
                    },
                    error -> {
                        requestListener.errorResponse(RequestExceptionFactory.createExceptionFromError(error));
                    }, Providers.getAuthProvider().getAuthToken());

            Providers.getRequestProvider().addToQueue(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readAsRestaurauter(long id, RequestListener<Order> RequestListener){
        super.read(id, RESTAURATEUR+ORDER, RequestListener, Order.class, true);
    }

    public void searchRider(long orderId, String pickupTime, RequestListener<String> requestListener){

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("pickup_time", pickupTime);
            ObjectRequest request = new ObjectRequest(Request.Method.POST, String.format("%s/api/%s%s/%d/searchRider", Constants.SERVER_HOST, RESTAURATEUR, ORDER, orderId), jsonObject,
                    response -> {
                        requestListener.successResponse(response.toString());
                    },
                    error -> {
                        requestListener.errorResponse(RequestExceptionFactory.createExceptionFromError(error));
                    }, Providers.getAuthProvider().getAuthToken());

            Providers.getRequestProvider().addToQueue(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deliverOrderAsRestaurateur(long idOrder, int validationCode, RequestListener<Boolean> requestListener){

        try {
            ObjectRequest request = new ObjectRequest(Request.Method.GET, String.format("%s/api/%s%s/%d/validateCode/%d", Constants.SERVER_HOST, RESTAURATEUR, ORDER, idOrder, validationCode), null,
                    response -> {
                        try {
                            requestListener.successResponse(response.getBoolean("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        requestListener.errorResponse(RequestExceptionFactory.createExceptionFromError(error));
                    }, Providers.getAuthProvider().getAuthToken());

            Providers.getRequestProvider().addToQueue(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
