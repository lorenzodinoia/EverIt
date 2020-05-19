package it.uniba.di.sms1920.everit.utils.request;

import java.util.Collection;

import it.uniba.di.sms1920.everit.utils.models.Review;
import it.uniba.di.sms1920.everit.utils.request.core.CRUDRequest;
import it.uniba.di.sms1920.everit.utils.request.core.CRUD;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public final class ReviewRequest extends CRUDRequest<Review> implements CRUD<Review> {

    private final String URL = "restaurateur/feedback";

    @Override
    public void create(Review model, RequestListener<Review> RequestListener) {
        super.create(model, model.getURLInsertion(), RequestListener, Review.class, true);
    }

    @Override
    public void read(long id, RequestListener<Review> RequestListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void readAll(RequestListener<Collection<Review>> RequestListener) {
        super.readAll(URL, RequestListener, Review.class, true);
    }

    @Override
    public void update(Review model, RequestListener<Review> RequestListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(long id, RequestListener<Boolean> RequestListener) {
        super.delete(id, String.format("%s/%d", URL, id), RequestListener, true);
    }
}
