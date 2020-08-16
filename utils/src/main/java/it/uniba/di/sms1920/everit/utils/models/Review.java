package it.uniba.di.sms1920.everit.utils.models;

import android.os.Parcel;

import org.threeten.bp.LocalDateTime;

public class Review extends Model {
    private int vote;
    private String text;
    private LocalDateTime createdAt;
    private Customer customer;
    private Restaurateur restaurateur;

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public Review(int vote, String text, LocalDateTime createdAt, Customer customer, Restaurateur restaurateur) {
        this.vote = vote;
        this.createdAt = createdAt;
        this.text = text;
        this.customer = customer;
        this.restaurateur = restaurateur;
    }

    public Review(long id, int vote, String text, LocalDateTime createdAt, Customer customer, Restaurateur restaurateur) {
        super(id);
        this.vote = vote;
        this.text = text;
        this.createdAt = createdAt;
        this.customer = customer;
        this.restaurateur = restaurateur;
    }

    public Review(Parcel in) {
        super(in);
        this.vote = in.readInt();
        this.text = in.readString();
        this.createdAt = in.readParcelable(LocalDateTime.class.getClassLoader());
        this.customer = in.readParcelable(Customer.class.getClassLoader());
        this.restaurateur = in.readParcelable(Restaurateur.class.getClassLoader());
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Review setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Restaurateur getRestaurateur() {
        return restaurateur;
    }

    public void setRestaurateur(Restaurateur restaurateur) {
        this.restaurateur = restaurateur;
    }

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.vote);
        dest.writeString(this.text);
        dest.writeSerializable(this.createdAt);
        dest.writeParcelable(this.customer, flags);
        dest.writeParcelable(this.restaurateur, flags);
    }
}
