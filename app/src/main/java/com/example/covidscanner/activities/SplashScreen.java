package com.example.covidscanner.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.local.UserIdStorageFactory;
import com.example.covidscanner.ApplicationClass;
import com.example.covidscanner.R;

public class SplashScreen extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {

                tvLoad.setText("Authenticating user...please wait...");
                showProgress(true);

                Backendless.UserService.isValidLogin(new AsyncCallback<Boolean>() {
                    @Override
                    public void handleResponse(Boolean response)
                    {
                        if (response)
                        {
                            String userObjectId = UserIdStorageFactory.instance().getStorage().get();

                            Backendless.Data.of(BackendlessUser.class).findById(userObjectId, new AsyncCallback<BackendlessUser>() {
                                @Override
                                public void handleResponse(BackendlessUser response) {

                                    tvLoad.setText("User Authenticated...signing in...");

                                    ApplicationClass.user = response;

                                    Intent intent = new Intent(SplashScreen.this, MenuActivity.class);
                                    intent.putExtra("school_name", response.getProperty("name").toString());
                                    startActivity(intent);

                                    Toast.makeText(SplashScreen.this, "User: " + response.getProperty("name") +
                                            ", signed in.", Toast.LENGTH_SHORT).show();

                                    SplashScreen.this.finish();
                                }

                                @Override
                                public void handleFault(BackendlessFault fault)
                                {
                                    Toast.makeText(SplashScreen.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else
                        {
                            startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                        }
                    }

                    @Override
                    public void handleFault(BackendlessFault fault)
                    {
                        Toast.makeText(SplashScreen.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 5000);

    }//end onCreate

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}