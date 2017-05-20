package loginrx.example.elanic.loginrxjava.presenter;

import android.text.TextUtils;

import java.util.concurrent.TimeUnit;

import loginrx.example.elanic.loginrxjava.LoginCallback;
import loginrx.example.elanic.loginrxjava.model.LoginProvider;
import loginrx.example.elanic.loginrxjava.view.LoginView;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ramya on 17/5/17.
 */

public class LoginPresenterImpl implements LoginPresenter {
    private LoginView loginView;
    private LoginProvider loginProvider;
    private CompositeSubscription compositeSubscription=new CompositeSubscription();
    private Observable<CharSequence> usernameObservable;
    private Observable<CharSequence> passwordObservable;


    public LoginPresenterImpl(LoginView loginView, LoginProvider loginProvider) {
        this.loginView = loginView;
        this.loginProvider = loginProvider;
    }


    @Override
    public void login(String username, String password) {
        loginView.showProgressBar(true);
        loginProvider.requestLogin(username, password, new LoginCallback() {
            @Override
            public void onSuccess(boolean login, String message) {
                if(login){
                    loginView.loginStatus(true);
                    loginView.showProgressBar(false);
                }
                else
                {
                    loginView.showError(message);
                    loginView.showProgressBar(false);
                }
            }

            @Override
            public void onFailure() {
                loginView.loginStatus(false);
                loginView.showProgressBar(false);
                loginView.showError("Please Try again !!");
            }
        });

    }

}
