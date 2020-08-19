package it.uniba.di.sms1920.everit.utils.request;

import com.android.volley.Request;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.security.Provider;
import java.util.Collection;
import java.util.Locale;

import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.adapter.Adapter;
import it.uniba.di.sms1920.everit.utils.models.Proposal;
import it.uniba.di.sms1920.everit.utils.provider.AdapterProvider;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.core.CRUD;
import it.uniba.di.sms1920.everit.utils.request.core.CRUDRequest;
import it.uniba.di.sms1920.everit.utils.request.core.ObjectRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestExceptionFactory;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class ProposalRequest extends CRUDRequest<Proposal> implements CRUD<Proposal> {
    private final String URL = "rider/proposal";
    private final String RESTAURATEUR = "restaurateur";
    private final String ORDER = "order";
    private final String PROPOSAL = "proposal";

    @Override
    public void create(Proposal model, RequestListener<Proposal> RequestListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void read(long id, RequestListener<Proposal> RequestListener) {
        super.read(id, URL, RequestListener, Proposal.class, true);
    }

    @Override
    public void readAll(RequestListener<Collection<Proposal>> RequestListener) {
        super.readAll(URL.concat("/get/all"), RequestListener, Proposal.class, true);
    }

    @Override
    public void update(Proposal model, RequestListener<Proposal> RequestListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(long id, RequestListener<Boolean> RequestListener) {
        throw new UnsupportedOperationException();
    }

    public void accept(long id, RequestListener<Boolean> requestListener) {
        ObjectRequest acceptRequest = new ObjectRequest(Request.Method.POST, String.format(Locale.getDefault(), "%s/api/%s/%d/accept", Constants.SERVER_HOST, URL, id), null,
                response -> {
                    requestListener.successResponse(true);
                },
                error -> {
                     requestListener.errorResponse(RequestExceptionFactory.createExceptionFromError(error));
                }, Providers.getAuthProvider().getAuthToken());

        Providers.getRequestProvider().addToQueue(acceptRequest);
    }

    public void refuse(long id, RequestListener<Boolean> requestListener) {
        ObjectRequest acceptRequest = new ObjectRequest(Request.Method.POST, String.format(Locale.getDefault(), "%s/api/%s/%d/refuse", Constants.SERVER_HOST, URL, id), null,
                response -> {
                    requestListener.successResponse(true);
                },
                error -> {
                    requestListener.errorResponse(RequestExceptionFactory.createExceptionFromError(error));
                }, Providers.getAuthProvider().getAuthToken());

        Providers.getRequestProvider().addToQueue(acceptRequest);
    }

    public void checkProposalsState(long idOrder, RequestListener<Boolean> requestListener){

        ObjectRequest checkRequest = new ObjectRequest(Request.Method.GET, String.format(Locale.getDefault(), "%s/api/%s/%s/%d/%s", Constants.SERVER_HOST, RESTAURATEUR, ORDER, idOrder, PROPOSAL), null,
                response -> {
                    Boolean value = false;
                    try {
                        value = response.getBoolean("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    requestListener.successResponse(value);
                },
                error -> {
                    requestListener.errorResponse(RequestExceptionFactory.createExceptionFromError(error));
                }, Providers.getAuthProvider().getAuthToken());

        Providers.getRequestProvider().addToQueue(checkRequest);
    }
}
