package it.uniba.di.sms1920.everit.utils.models;

import org.threeten.bp.LocalDateTime;

public class Review extends Model {
    private int vote;
    private String text;
    private LocalDateTime createdAt;
    private Customer customer;
    private Restaurateur restaurateur;

    public Review(int vote, String text, LocalDateTime createdAt, Customer customer, Restaurateur restaurateur) {
        this.vote = vote;
        this.text = text;
        this.createdAt = createdAt;
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
}
