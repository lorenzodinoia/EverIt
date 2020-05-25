package it.uniba.di.sms1920.everit.utils.request;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;

import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.models.Customer;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.core.CRUD;
import it.uniba.di.sms1920.everit.utils.request.core.CRUDRequest;
import it.uniba.di.sms1920.everit.utils.request.core.ObjectRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestExceptionFactory;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public final class CustomerRequest extends CRUDRequest<Customer> implements CRUD<Customer> {
    private static final class JSONKeys {
        private static final String KEY_OLD_PASSWORD = "old_password";
        private static final String KEY_NEW_PASSWORD = "new_password";
    }

    private final String URL = "customer";

    @Override
    public void create(Customer model, RequestListener<Customer> RequestListener) {
        super.create(model, URL, RequestListener, Customer.class, false);
    }

    @Override
    public void read(long id, RequestListener<Customer> RequestListener) {
        super.read(id, URL, RequestListener, Customer.class, true);
    }

    @Override
    public void readAll(RequestListener<Collection<Customer>> RequestListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(Customer model, RequestListener<Customer> RequestListener) {
        super.update(model, URL, RequestListener, Customer.class, true);
    }

    @Override
    public void delete(long id, RequestListener<Boolean> RequestListener) {
        super.delete(id, URL, RequestListener, true);
    }

    public void changePassword(String oldPassword, String newPassword, RequestListener<Boolean> requestListener) {
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put(JSONKeys.KEY_OLD_PASSWORD, oldPassword);
            jsonObject.put(JSONKeys.KEY_NEW_PASSWORD, newPassword);

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

