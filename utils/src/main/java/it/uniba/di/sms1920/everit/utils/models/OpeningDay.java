package it.uniba.di.sms1920.everit.utils.models;

import java.util.Collection;

public class OpeningDay extends Model {
    private String name;
    private Collection<OpeningTime> openingTimes;

    public OpeningDay(long id, String name) {
        super(id);
        this.name = name;
    }

    public OpeningDay(long id, String name, Collection<OpeningTime> openingTimes) {
        super(id);
        this.name = name;
        this.openingTimes = openingTimes;
    }

    public String getName() {
        return name;
    }

    public Collection<OpeningTime> getOpeningTimes() {
        return openingTimes;
    }

    public OpeningDay setOpeningTimes(Collection<OpeningTime> openingTimes) {
        this.openingTimes = openingTimes;
        return this;
    }
}
