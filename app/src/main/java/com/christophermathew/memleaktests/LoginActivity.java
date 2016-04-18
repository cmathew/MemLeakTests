package com.christophermathew.memleaktests;

import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends RxAppCompatActivity {
    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private RestClient restClient;

    private static class AuthResponder implements Func1<Driver, AppUserState> {
        @Override
        public AppUserState call(Driver driver) {
            Integer driverId = driver.getId();
            String authToken = driver.getAuthenticationToken();
            String conveyanceType = driver.getPreferredConveyanceMetatype();

            AppUserState authInfo = new AppUserState(driverId, authToken, conveyanceType);

            return authInfo;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        restClient = new RestClient(getApplicationContext(), new EnvironmentProvider());
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        Observable<AppUserState> obs = restClient.login("chris@deliv.co", "Mayan123")
                .compose(this.<Driver>applyUISchedulers())
                .compose(this.<Driver>bindToLifecycle())
                .map(new AuthResponder());

        obs.subscribe(new Action1<AppUserState>() {
            @Override
            public void call(AppUserState data) {
                finish();

                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(mainIntent);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e("BEER", "DAMMIT");
                throwable.printStackTrace();
            }
        });

        /*
        finish();

        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainIntent);
        */
    }

    //================================================================================
    // Observables
    //================================================================================
    protected  <T> Observable.Transformer<T, T> applyUISchedulers() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}

