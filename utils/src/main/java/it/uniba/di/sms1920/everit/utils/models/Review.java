package it.uniba.di.sms1920.everit.utils.models;

public class Review extends Model {

    private int vote;
    private String text;
    private Customer customer;
    private Restaurateur restaurateur;

    public Review(int vote, String description, Restaurateur restaurateur, Customer customer){
        this.vote = vote;
        this.text = description ;
        this.restaurateur = restaurateur;
        this.customer = customer;
    }

    public String getURLInsertion() {
        return String.format("restaurateur/%d/feedback", this.restaurateur.getId());
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
