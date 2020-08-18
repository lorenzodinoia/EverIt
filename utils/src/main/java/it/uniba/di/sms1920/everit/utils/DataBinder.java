package it.uniba.di.sms1920.everit.utils;

/**
 * Interface to connect two components (eg. Fragment, Activity, Service, etc...) in order to perform actions on managed data ignoring the class type
 * Eg. Fragments A, B and C holds a set of data. These fragments are managed by an activity. The activity can performs some actions on the data ignoring the Fragment class types and casting issues.
 */
public interface DataBinder {
    /**
     * Refresh the data
     */
    void refreshData();
}
