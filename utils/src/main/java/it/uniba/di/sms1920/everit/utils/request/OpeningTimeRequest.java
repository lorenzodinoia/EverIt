package it.uniba.di.sms1920.everit.utils.request;

import com.android.volley.Request;

import org.json.JSONObject;

import java.util.Collection;

import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.adapter.Adapter;
import it.uniba.di.sms1920.everit.utils.models.OpeningTime;
import it.uniba.di.sms1920.everit.utils.models.Restaurateur;
import it.uniba.di.sms1920.everit.utils.models.User;
import it.uniba.di.sms1920.everit.utils.provider.AdapterProvider;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.core.CRUD;
import it.uniba.di.sms1920.everit.utils.request.core.CRUDRequest;
import it.uniba.di.sms1920.everit.utils.request.core.ObjectRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestExceptionFactory;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class OpeningTimeRequest extends CRUDRequest<OpeningTime> implements CRUD<OpeningTime> {

    private String RESTAURATEUR = "restaurateur";
    private String OPENING_DAYS = "/openingDay";
    private String OPENING_TIMES = "/openingTimes";

    @Override
    public void create(OpeningTime model, RequestListener<OpeningTime> RequestListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void read(long id, RequestListener<OpeningTime> RequestListener) {
        throw  new UnsupportedOperationException();
    }

    @Override
    public void readAll(RequestListener<Collection<OpeningTime>> RequestListener) {
        throw  new UnsupportedOperationException();
    }

    @Override
    public void update(OpeningTime model, RequestListener<OpeningTime> RequestListener) {
        throw  new UnsupportedOperationException();
    }

    @Override
    public void delete(long id, RequestListener<Boolean> RequestListener) {
        super.delete(Providers.getAuthProvider().getUser().getId(), RESTAURATEUR+OPENING_TIMES+"/"+id, RequestListener, true);
    }

    public void createOpeningTime(long idDay, OpeningTime model, RequestListener<OpeningTime> requestListener){
        Adapter<OpeningTime> adapter = AdapterProvider.getAdapterFor(OpeningTime.class);
        try {
            JSONObject jsonObject = adapter.toJSON(model);
            String url = Constants.SERVER_HOST+"/api/"+RESTAURATEUR+OPENING_DAYS+"/"+idDay+OPENING_TIMES;
            ObjectRequest request = new ObjectRequest(Request.Method.POST, url, jsonObject,
                    response -> {
                        OpeningTime data = adapter.fromJSON(response, OpeningTime.class);
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
}
