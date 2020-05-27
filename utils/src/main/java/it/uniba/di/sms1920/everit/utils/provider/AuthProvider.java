package it.uniba.di.sms1920.everit.utils.provider;

import it.uniba.di.sms1920.everit.utils.models.Authenticable;
import it.uniba.di.sms1920.everit.utils.models.Model;
import it.uniba.di.sms1920.everit.utils.request.AccessRequest;
import it.uniba.di.sms1920.everit.utils.request.core.RequestException;
import it.uniba.di.sms1920.everit.utils.request.core.RequestListener;

public final class AuthProvider<T extends Model & Authenticable> {
    private T user;
    private Class<T> userModelType;
    private String authToken;
    private AccessRequest<T> accessRequest;

    AuthProvider(Class<T> userModelType) {
        this.userModelType = userModelType;
        this.accessRequest = new AccessRequest<>(userModelType);
    }

    public T getUser() {
        return this.user;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void login(CredentialProvider.Credential credential, RequestListener<T> loginListener) {
        this.accessRequest.login(credential.getEmail(), credential.getPassword(), new RequestListener<T>() {
            @Override
            public void successResponse(T response) {
                user = response;
                Providers.getCredentialProvider().saveCredential(credential);
                loginListener.successResponse(user);
            }

            @Override
            public void errorResponse(RequestException error) {
                loginListener.errorResponse(error);
            }
        });
    }

    public void loginFromSavedCredential(RequestListener<T> loginListener) throws NoSuchCredentialException {
        CredentialProvider.Credential credential = Providers.getCredentialProvider().loadCredential();

        if (credential != null) {
            this.login(credential, loginListener);
        }
        else {
            throw new NoSuchCredentialException();
        }
    }

    public void logout(RequestListener<Boolean> logoutListener) {
        this.accessRequest.logout(new RequestListener<Boolean>() {
            @Override
            public void successResponse(Boolean response) {
                removeAllUserData();
                logoutListener.successResponse(true);
            }

            @Override
            public void errorResponse(RequestException error) {
                logoutListener.errorResponse(error);
            }
        });
    }

    public void removeAllUserData() {
        this.user = null;
        this.authToken = null;
        Providers.getCredentialProvider().removeCredential();
    }
}
