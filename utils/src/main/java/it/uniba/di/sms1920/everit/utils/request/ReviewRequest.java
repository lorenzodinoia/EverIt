package it.uniba.di.sms1920.everit.utils.request;

import java.util.Collection;

import it.uniba.di.sms1920.everit.utils.models.Review;
import it.uniba.di.sms1920.everit.utils.request.core.CRUDRequest;
import it.uniba.di.sms1920.everit.utils.request.core.CRUD;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public final class ReviewRequest extends CRUDRequest<Review> implements CRUD<Review> {

    private final String REVIEW = "review";
    private final String RESTAURATEUR = "restaurateur";
    private final String CUSTOMER = "customer";

    @Override
    public void create(Review model, RequestListener<Review> RequestListener) {
        super.create(model, String.format("%s/%d/%s", RESTAURATEUR, model.getRestaurateur().getId(), REVIEW), RequestListener, Review.class, true);
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
        super.update(model, String.format("%s/%s/%d", CUSTOMER, REVIEW, model.getId()), RequestListener, Review.class, true);
    }

    @Override
    public void delete(long id, RequestListener<Boolean> RequestListener) {
        super.delete(id, String.format("%s/%s/%d", CUSTOMER, id, REVIEW), RequestListener, true);
    }

    public void readCustomerReviews(RequestListener<Collection<Review>> requestListener) {
        super.readAll(String.format("%s/%s/all", CUSTOMER, REVIEW), requestListener, Review.class, true);
    }

    public void readRestaurateurReviews(RequestListener<Collection<Review>> requestListener) {
        super.readAll(String.format("%s/%s/all", RESTAURATEUR, REVIEW), requestListener, Review.class, true);
    }

    public void readRestaurateurReviewsFromCustomer(long restaurateurId, RequestListener<Collection<Review>> requestListener) {
        super.readAll(String.format("%s/%d/%s/all", RESTAURATEUR, restaurateurId, REVIEW), requestListener, Review.class, false);
    }
}
