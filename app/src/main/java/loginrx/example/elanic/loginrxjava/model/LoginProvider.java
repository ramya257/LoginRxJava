package loginrx.example.elanic.loginrxjava.model;

import loginrx.example.elanic.loginrxjava.LoginCallback;

/**
 * Created by ramya on 17/5/17.
 */

public interface LoginProvider {
    void requestLogin(String username, String password,LoginCallback loginCallback);
}
