package loginrx.example.elanic.loginrxjava;

/**
 * Created by ramya on 17/5/17.
 */

public interface LoginCallback {
    void onSuccess(boolean login,String message);
    void onFailure();
}
