package it.uniba.di.sms1920.everit.utils.models;

import android.os.Parcel;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class OpeningDay extends Model implements Comparable<OpeningDay> {
    private String name;
    private List<OpeningTime> openingTimes = new ArrayList<>();

    public static final Creator<OpeningDay> CREATOR = new Creator<OpeningDay>() {
        @Override
        public OpeningDay createFromParcel(Parcel in) {
            return new OpeningDay(in);
        }

        @Override
        public OpeningDay[] newArray(int size) {
            return new OpeningDay[size];
        }
    };

    public OpeningDay(long id, String name) {
        super(id);
        this.name = name;
    }

    public OpeningDay(long id, String name, List<OpeningTime> openingTimes) {
        super(id);
        this.name = name;
        this.openingTimes = openingTimes;
    }

    public OpeningDay(Parcel in) {
        super(in);
        this.name = in.readString();
        this.openingTimes = in.createTypedArrayList(OpeningTime.CREATOR);
    }

    public String getName() {
        return name;
    }

    public List<OpeningTime> getOpeningTimes() {
        return openingTimes;
    }

    public OpeningDay setOpeningTimes(List<OpeningTime> openingTimes) {
        this.openingTimes = openingTimes;
        return this;
    }

    public void sortOpeningTimes() {
        Collections.sort(this.openingTimes);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof OpeningDay) {
            OpeningDay instance = (OpeningDay) obj;
            return this.getName().equals(instance.getName());
        }

        return false;
    }

    @Override
    public int compareTo(OpeningDay o) {
        return Long.compare(super.getId(), o.getId());
    }

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.name);
        dest.writeTypedList(this.openingTimes);
    }
}
