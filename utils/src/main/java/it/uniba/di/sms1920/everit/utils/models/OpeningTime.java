package it.uniba.di.sms1920.everit.utils.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.threeten.bp.LocalTime;

public class OpeningTime extends Model implements Comparable<OpeningTime> {
    @SerializedName("opening_time")
    private LocalTime open;
    @SerializedName("closing_time")
    private LocalTime close;

    public OpeningTime(LocalTime open, LocalTime close) {
        this.open = open;
        this.close = close;
    }

    public OpeningTime(long id, LocalTime open, LocalTime close) {
        super(id);
        this.open = open;
        this.close = close;
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

    @Override
    public int compareTo(OpeningTime o) {
        return this.open.compareTo(o.getOpen());
    }

    @NonNull
    @Override
    public String toString() {
        return open.toString()+" - "+close.toString();
    }
}
