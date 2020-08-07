package it.uniba.di.sms1920.everit.utils.models;

import android.os.Parcel;

import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.ChronoUnit;

import it.uniba.di.sms1920.everit.utils.Constants;

public class Proposal extends Model {
    private Order order;
    private Restaurateur restaurateur;
    private LocalTime pickupTime;

    public static final Creator<Proposal> CREATOR = new Creator<Proposal>() {
        @Override
        public Proposal createFromParcel(Parcel in) {
            return new Proposal(in);
        }

        @Override
        public Proposal[] newArray(int size) {
            return new Proposal[size];
        }
    };

    public Proposal(long id) {
        super(id);
    }

    public Proposal(Parcel in) {
        super(in);
        this.order = in.readParcelable(Order.class.getClassLoader());
        this.restaurateur = in.readParcelable(Restaurateur.class.getClassLoader());
        this.pickupTime = (LocalTime) in.readSerializable();
    }

    public Restaurateur getRestaurateur() {
        return restaurateur;
    }

    public Order getOrder() {
        return order;
    }

    public LocalTime getPickupTime() {
        return pickupTime;
    }

    public String getRestaurateurAddress() {
        return this.restaurateur.getAddress().getFullAddress();
    }

    public String getDeliveryAddress() {
        return this.order.getDeliveryAddress().getFullAddress();
    }

    public int getRemainingTime() {
        LocalTime currentTime = LocalTime.now();
        return (int) currentTime.until(pickupTime, ChronoUnit.MINUTES);
    }

    public String getPickupTimeAsString() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(Constants.TIME_FORMAT);
        return timeFormatter.format(this.pickupTime);
    }

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.order, flags);
        dest.writeParcelable(this.restaurateur, flags);
        dest.writeSerializable(this.pickupTime);
    }
}
