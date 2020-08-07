package it.uniba.di.sms1920.everit.utils.models;

import android.os.Parcel;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

public class OpeningTime extends Model implements Comparable<OpeningTime> {
    @SerializedName("opening_time")
    private LocalTime open;
    @SerializedName("closing_time")
    private LocalTime close;

    public static final Creator<OpeningTime> CREATOR = new Creator<OpeningTime>() {
        @Override
        public OpeningTime createFromParcel(Parcel in) {
            return new OpeningTime(in);
        }

        @Override
        public OpeningTime[] newArray(int size) {
            return new OpeningTime[size];
        }
    };

    public OpeningTime(LocalTime open, LocalTime close) {
        this.open = open;
        this.close = close;
    }

    public OpeningTime(long id, LocalTime open, LocalTime close) {
        super(id);
        this.open = open;
        this.close = close;
    }

    public OpeningTime(Parcel in) {
        super(in);
        this.open = (LocalTime) in.readSerializable();
        this.close = (LocalTime) in.readSerializable();
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
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        return open.format(timeFormatter)+" - "+close.format(timeFormatter);
    }

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeSerializable(this.open);
        dest.writeSerializable(this.close);
    }
}
