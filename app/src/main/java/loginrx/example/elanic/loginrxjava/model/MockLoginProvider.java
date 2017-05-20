package loginrx.example.elanic.loginrxjava.model;

import java.util.logging.Handler;

import loginrx.example.elanic.loginrxjava.LoginCallback;
import loginrx.example.elanic.loginrxjava.presenter.LoginPresenter;

/**
 * Created by ramya on 17/5/17.
 */

public class MockLoginProvider implements LoginProvider{
    private String name="Ramyaaa";
    private String password="asdfg45";
    private LoginPresenter loginPresenter;
    public MockLoginProvider()
    {}


    @Override
    public void requestLogin(String username, String password, LoginCallback loginCallback) {
        if(username.equals(name)&&password.equals(password)){
            loginCallback.onSuccess(true,"success");
        }
        else {
            loginCallback.onFailure();
        }
    }
}
