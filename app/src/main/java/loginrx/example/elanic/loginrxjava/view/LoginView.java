package loginrx.example.elanic.loginrxjava.view;

/**
 * Created by ramya on 17/5/17.
 */

public interface LoginView {
    void loginStatus(boolean login);
    void showProgressBar(boolean show);
    void loginEnableStatus(boolean status);
    void showError(String message);
}
