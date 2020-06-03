package it.uniba.di.sms1920.everit.utils.request;

import java.util.Collection;

import it.uniba.di.sms1920.everit.utils.models.OpeningTime;
import it.uniba.di.sms1920.everit.utils.provider.Providers;
import it.uniba.di.sms1920.everit.utils.request.core.CRUD;
import it.uniba.di.sms1920.everit.utils.request.core.CRUDRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public class OpeningTimeRequest extends CRUDRequest<OpeningTime> implements CRUD<OpeningTime> {

    private String RESTAURATEUR = "/restaurateur";
    private String OPENING_TIMES = "/openingTime";
    @Override
    public void create(OpeningTime model, RequestListener<OpeningTime> RequestListener) {
        super.create(model, RESTAURATEUR+OPENING_TIMES, RequestListener, OpeningTime.class, true);
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
        super.delete(Providers.getAuthProvider().getUser().getId(), RESTAURATEUR+OPENING_TIMES+'/'+Providers.getAuthProvider().getUser().getId(), RequestListener, true);
    }
}
