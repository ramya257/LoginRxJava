package loginrx.example.elanic.loginrxjava.view;

import android.database.Observable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import loginrx.example.elanic.loginrxjava.R;
import loginrx.example.elanic.loginrxjava.model.MockLoginProvider;
import loginrx.example.elanic.loginrxjava.presenter.LoginPresenter;
import loginrx.example.elanic.loginrxjava.presenter.LoginPresenterImpl;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.subjects.PublishSubject;

public class LoginActivity extends AppCompatActivity implements LoginView {
    private EditText name_edit_text;
    private EditText password_edit_text;
    private Button login_button;
    private LoginPresenter loginPresenter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        name_edit_text=(EditText)findViewById(R.id.name_edit_text);
        password_edit_text=(EditText)findViewById(R.id.password_edit_text);
        login_button=(Button)findViewById(R.id.login_button);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        login_button.setEnabled(false);
        loginPresenter=new LoginPresenterImpl(this,new MockLoginProvider());
        rx.Observable<CharSequence> name_observable = RxTextView.textChanges(name_edit_text);
        rx.Observable<CharSequence>password_observable=RxTextView.textChanges(password_edit_text);
        Subscription nameSubscription = name_observable
                .doOnNext(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        hideNameError();
                    }
                })
                .debounce(400, TimeUnit.MILLISECONDS)
                .filter(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence charSequence) {
                        return !TextUtils.isEmpty(charSequence);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()) // UI Thread
                .subscribe(new Subscriber<CharSequence>() {
                    @Override
                    public void onCompleted() {
                        password_edit_text.requestFocus();

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(CharSequence charSequence) {
                        boolean isNameValid = validateName(charSequence.toString());
                        if (!isNameValid) {
                            shownNameError("Name is invalid");
                        } else {
                            hideNameError();
                        }
                    }
                });
        Subscription passwordSubscription = password_observable
                .doOnNext(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        hidePasswordError();
                    }
                })
                .debounce(400, TimeUnit.MILLISECONDS)
                .filter(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence charSequence) {
                        return !TextUtils.isEmpty(charSequence);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()) // UI Thread
                .subscribe(new Subscriber<CharSequence>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(CharSequence charSequence) {
                        boolean isPasswordValid = validatePassword(charSequence.toString());
                        if (!isPasswordValid) {
                            showPasswordError();
                        } else {
                            hidePasswordError();
                        }
                    }
                });
        Subscription LogInFieldsSubscription = rx.Observable.combineLatest(name_observable, password_observable, new Func2<CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean call(CharSequence name, CharSequence password) {
                boolean isNameValid = validateName(name.toString());
                boolean isPasswordValid = validatePassword(password.toString());

                return isNameValid && isPasswordValid;
            }
        }).observeOn(AndroidSchedulers.mainThread()) // UI Thread
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Boolean validFields) {
                        if (validFields) {
                            loginEnableStatus(true);
                        } else {
                            loginEnableStatus(false);
                        }
                    }
                });
}



    @Override
    public void loginStatus(boolean login) {
        if(login)
        {
        Toast.makeText(this,"Success",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void showProgressBar(boolean show) {
        if(show){
            progressBar.setVisibility(View.VISIBLE);
        }
        else {
            progressBar.setVisibility(View.GONE);
        }

    }

    @Override
    public void loginEnableStatus(boolean status) {
        if(status)
        {
            login_button.setEnabled(true);
            login_button.setOnClickListener
                    (v -> loginPresenter.login(name_edit_text.getText().toString()
                            ,password_edit_text.getText().toString()));
            Toast.makeText(this,"Proceed to login",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void showError(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }


    private boolean validateName(String s) {
        if (TextUtils.isEmpty(s))
            return false;
        if (s.length() <=6 && s.length() > 0) {
            shownNameError("Name must have at least 6 characters");

        }
        return s.length() > 6;
    }
    private boolean validatePassword(String password) {
        return password.length() > 5;
    }

    private void shownNameError(String message){
        name_edit_text.setError(message);
    }

    private void hideNameError(){

        name_edit_text.setError(null);
    }

    private void showPasswordError(){

        password_edit_text.setError("Password must be at least 6 characters");
    }

    private void hidePasswordError(){

        password_edit_text.setError(null);
    }


}
