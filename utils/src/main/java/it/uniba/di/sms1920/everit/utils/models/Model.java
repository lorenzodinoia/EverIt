package it.uniba.di.sms1920.everit.utils.models;

import androidx.annotation.Nullable;

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

    @Override
    public boolean equals(@Nullable Object obj) {
        boolean result = false;

        if (obj instanceof Model) {
            Model model = (Model) obj;
            result = (this.id == model.getId());
        }

        return result;
    }
}
