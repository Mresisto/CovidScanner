package com.example.covidscanner.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.covidscanner.ApplicationClass;
import com.example.covidscanner.classes.Learner;
import com.example.covidscanner.R;

public class CreateLearner extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    EditText etName, etSurname, etGrade;
    TextView tvBarcode;
    Button btnSubmit;

    String barcode;
    private final int MAIN_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_learner);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(ApplicationClass.user.getProperty("name").toString());

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        etName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        etGrade = findViewById(R.id.etGrade);
        tvBarcode = findViewById(R.id.tvBarcode);
        btnSubmit = findViewById(R.id.btnSubmit);

        tvBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivityForResult(new Intent(CreateLearner.this, MainActivity.class), MAIN_ACTIVITY);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (etName.getText().toString().isEmpty() || etSurname.getText().toString().isEmpty()
                        || etGrade.getText().toString().isEmpty())
                {
                    ApplicationClass.showToast(getApplicationContext(), "error", "Please enter all fields!");
                }
                else
                {
                    Learner learner = new Learner();
                    learner.setSchoolEmail(ApplicationClass.user.getEmail());
                    learner.setName(etName.getText().toString().trim());
                    learner.setSurname(etSurname.getText().toString().trim());
                    learner.setGrade(etGrade.getText().toString().trim());
                    learner.setCode(barcode);

                    tvLoad.setText(R.string.adding_learner);
                    showProgress(true);

                    Backendless.Persistence.save(learner, new AsyncCallback<Learner>() {
                        @Override
                        public void handleResponse(Learner response)
                        {
                            ApplicationClass.showToast(getApplicationContext(), "correct", "Learner successfully saved.");
                            showProgress(false);

                            ApplicationClass.learner = response;
                            Intent intent = new Intent(CreateLearner.this, MenuActivity.class);
                            setResult(RESULT_OK, intent);

                            CreateLearner.this.finish();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault)
                        {
                            ApplicationClass.showToast(getApplicationContext(), "error","Error: " + fault.getMessage());
                            showProgress(false);
                        }
                    });

                }//end if-else
            }
        });

    }//onCreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MAIN_ACTIVITY && resultCode == RESULT_OK)
        {
            barcode = data.getStringExtra("barcode");
            tvBarcode.setText(barcode);
        }
    }

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