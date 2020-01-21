package it.uniba.di.sms1920.everit.models;

public abstract class Model {
    private long id;

    public Model() {}

    public Model(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
