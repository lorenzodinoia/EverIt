package it.uniba.di.sms1920.everit.utils.provider;

public final class FirebaseTokenProvider {
    public void setFirebaseToken(String firebaseToken) {
        Providers.getPreferencesProvider().saveFirebaseToken(firebaseToken);
    }

    public String getFirebaseToken() {
        return Providers.getPreferencesProvider().loadFirebaseToken();
    }

    public void removeFirebaseToken() {
        Providers.getPreferencesProvider().removeFirebaseToken();
    }
}
