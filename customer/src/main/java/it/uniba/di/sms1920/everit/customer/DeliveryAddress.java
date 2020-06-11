package it.uniba.di.sms1920.everit.customer;

import it.uniba.di.sms1920.everit.utils.Address;

/**
 * Class used to hold the user's address which will be used to ship the order.
 * The delivery address must be unique in the whole app.
 * The address is set when the user searches restaurants nearby an address
 */
public final class DeliveryAddress {
    private static Address deliveryAddress;

    /**
     * @return Returns the Address chosen by the user for the delivery
     */
    public static Address get() {
        return deliveryAddress;
    }

    /**
     * @param deliveryAddress Address chosen by the user
     */
    public static void set(Address deliveryAddress) {
        DeliveryAddress.deliveryAddress = deliveryAddress;
    }
}
