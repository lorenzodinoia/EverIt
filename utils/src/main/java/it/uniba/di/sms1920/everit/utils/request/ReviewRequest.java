package it.uniba.di.sms1920.everit.utils.request;

import java.util.Collection;

import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.models.Review;
import it.uniba.di.sms1920.everit.utils.request.core.CRUDRequest;
import it.uniba.di.sms1920.everit.utils.request.core.CRUD;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public final class ReviewRequest extends CRUDRequest<Review> implements CRUD<Review> {

    private final String URL = "review";

    @Override
    public void create(Review model, RequestListener<Review> RequestListener) {
        super.create(model, String.format("restaurateur/%d/review", model.getRestaurateur().getId()), RequestListener, Review.class, true);
    }

    @Override
    public void read(long id, RequestListener<Review> RequestListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void readAll(RequestListener<Collection<Review>> RequestListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(Review model, RequestListener<Review> RequestListener) {
        super.update(model, String.format("customer/review/%d", model.getId()), RequestListener, Review.class, true);
    }

    @Override
    public void delete(long id, RequestListener<Boolean> RequestListener) {
        super.delete(id, String.format("customer/review/%d", id), RequestListener, true);
    }

    public void readCustomerReviews(RequestListener<Collection<Review>> requestListener) {
        super.readAll("customer/review/all", requestListener, Review.class, true);
    }

    public void readRestaurateurReviews(RequestListener<Collection<Review>> requestListener) {
        super.readAll("restaurateur/review/all", requestListener, Review.class, true);
    }
}
