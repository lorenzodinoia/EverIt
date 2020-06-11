package it.uniba.di.sms1920.everit.customer.cart;

/**
 * Interface used to connect an activity or a fragment to UI components which manages cart items such as products, notes, times, etc...
 */
public interface CartConnector {
    /**
     * @return Cart instance
     */
    Cart getCart();

    /**
     * Check if an order is previous created for a restaurateur.
     * If there isn't an order for that restaurateur, a new instance will be created
     *
     * @return Previously created order if exists, a new instance otherwise
     */
    PartialOrder getPartialOrder();
}
