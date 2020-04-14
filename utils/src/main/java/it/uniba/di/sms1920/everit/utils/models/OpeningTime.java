package it.uniba.di.sms1920.everit.utils.models;

import java.time.LocalTime;

public class OpeningTime extends Model {
    private LocalTime open;
    private LocalTime close;
    private OpeningDay openingDay;

    public OpeningTime(LocalTime open, LocalTime close, OpeningDay openingDay) {
        this.open = open;
        this.close = close;
        this.openingDay = openingDay;
    }

    public OpeningTime(long id, LocalTime open, LocalTime close, OpeningDay openingDay) {
        super(id);
        this.open = open;
        this.close = close;
        this.openingDay = openingDay;
    }

    public LocalTime getOpen() {
        return open;
    }

    public OpeningTime setOpen(LocalTime open) {
        this.open = open;
        return this;
    }

    public LocalTime getClose() {
        return close;
    }

    public OpeningTime setClose(LocalTime close) {
        this.close = close;
        return this;
    }

    public OpeningDay getOpeningDay() {
        return openingDay;
    }

    public OpeningTime setOpeningDay(OpeningDay openingDay) {
        this.openingDay = openingDay;
        return this;
    }
}
