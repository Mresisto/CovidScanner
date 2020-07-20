package com.example.covidscanner.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.covidscanner.ApplicationClass;
import com.example.covidscanner.R;

public class RegisterActivity extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;


    EditText etSchool, etMail, etPassword, etRetype;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        etSchool = findViewById(R.id.etSchool);
        etMail = findViewById(R.id.etMail);
        etPassword = findViewById(R.id.etPassword);
        etRetype = findViewById(R.id.etRetype);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (etSchool.getText().toString().isEmpty() || etMail.getText().toString().isEmpty()
                        || etPassword.getText().toString().isEmpty() || etRetype.getText().toString().isEmpty())
                {
                    ApplicationClass.showToast(getApplicationContext(), "error", "Please enter all fields!");
                }
                else
                {
                    if (etRetype.getText().toString().equals(etPassword.getText().toString().trim()))
                    {
                        tvLoad.setText(R.string.registering);
                        showProgress(true);

                        BackendlessUser user = new BackendlessUser();
                        user.setEmail(etMail.getText().toString().trim());
                        user.setPassword(etPassword.getText().toString().trim());
                        user.setProperty("name", etSchool.getText().toString().trim());

                        Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
                            @Override
                            public void handleResponse(BackendlessUser response)
                            {
                                showProgress(false);
                                ApplicationClass.showToast(getApplicationContext(), "correct", "User Successfully Registered.");
                                RegisterActivity.this.finish();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault)
                            {
                                ApplicationClass.showToast(getApplicationContext(), "error",
                                        "Error: " + fault.getMessage());
                                showProgress(false);
                            }
                        });
                    }
                    else
                    {
                        ApplicationClass.showToast(getApplicationContext(), "error", "Passwords do not match!");
                    }
                }
            }
        });


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