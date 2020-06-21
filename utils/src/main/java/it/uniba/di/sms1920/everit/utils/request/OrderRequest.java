package it.uniba.di.sms1920.everit.utils.request;

import com.android.volley.Request;

import org.json.JSONException;

import java.util.Collection;

import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.adapter.Adapter;
import it.uniba.di.sms1920.everit.utils.models.Order;
import it.uniba.di.sms1920.everit.utils.provider.AdapterProvider;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.core.ArrayRequest;
import it.uniba.di.sms1920.everit.utils.request.core.CRUD;
import it.uniba.di.sms1920.everit.utils.request.core.CRUDRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestExceptionFactory;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public final class OrderRequest extends CRUDRequest<Order> implements CRUD<Order> {

    private final String URL = "customer/order";

    @Override
    public void create(Order model, RequestListener<Order> RequestListener) {
        super.create(model, URL, RequestListener, Order.class, true);
    }

    @Override
    public void read(long id, RequestListener<Order> RequestListener) {
        super.read(id, URL, RequestListener, Order.class, true);
    }

    @Override
    public void readAll(RequestListener<Collection<Order>> RequestListener) {
        super.readAll(URL+"/getAll", RequestListener, Order.class, true);
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

        ArrayRequest request = new ArrayRequest(Request.Method.GET, String.format("%s/api/%s/pending", Constants.SERVER_HOST, URL), null,
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
}
