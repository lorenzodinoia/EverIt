package it.uniba.di.sms1920.everit.rider;

interface IBackgroundLocationService {
    boolean isWorking();
    void stop();
}