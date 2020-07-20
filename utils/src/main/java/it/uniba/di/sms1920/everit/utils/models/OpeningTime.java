package it.uniba.di.sms1920.everit.utils.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

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

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }

    @NonNull
    @Override
    public String toString() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        return open.format(timeFormatter)+" - "+close.format(timeFormatter);
    }
}
