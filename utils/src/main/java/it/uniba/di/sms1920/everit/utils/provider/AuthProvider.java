package it.uniba.di.sms1920.everit.utils.provider;

import it.uniba.di.sms1920.everit.utils.models.Authenticable;
import it.uniba.di.sms1920.everit.utils.models.Model;
import it.uniba.di.sms1920.everit.utils.request.LoginRequest;
import it.uniba.di.sms1920.everit.utils.request.RequestListener;

public final class AuthProvider<T extends Model & Authenticable> {
    private T user;
    private Class<T> userModelType;
    private String authToken;
    private LoginRequest<T> loginRequest;

    AuthProvider(Class<T> userModelType) {
        this.userModelType = userModelType;
        this.loginRequest = new LoginRequest<>(userModelType);
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
        this.loginRequest.login(credential.getEmail(), credential.getPassword(), new RequestListener<T>() {
            @Override
            public void successResponse(T response) {
                user = response;
                Providers.getCredentialProvider().saveCredential(credential);
                loginListener.successResponse(user);
            }

            @Override
            public void errorResponse(String error) {
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
        this.loginRequest.logout(new RequestListener<Boolean>() {
            @Override
            public void successResponse(Boolean response) {
                Providers.getCredentialProvider().removeCredential();
                user = null;
                authToken = null;
                logoutListener.successResponse(true);
            }

            @Override
            public void errorResponse(String error) {
                logoutListener.errorResponse(error);
            }
        });
    }
}
