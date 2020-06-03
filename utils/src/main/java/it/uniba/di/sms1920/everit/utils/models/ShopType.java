package it.uniba.di.sms1920.everit.utils.models;

import androidx.annotation.NonNull;

public class ShopType extends Model {

    private String name;

    public ShopType(long id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @NonNull
    @Override
    public String toString() {
        return this.getName();
    }
}
