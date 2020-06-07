package it.uniba.di.sms1920.everit.restaurateur.activities.openingTime;

import it.uniba.di.sms1920.everit.utils.models.OpeningTime;

public interface OpeningTimeCRUDInterface {

    void create(int listPosition, OpeningTime openingTime);
    void delete(int listPosition, int expandedListPosition);

}
