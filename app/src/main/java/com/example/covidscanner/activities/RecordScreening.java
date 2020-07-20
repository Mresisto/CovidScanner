package com.example.covidscanner.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.covidscanner.ApplicationClass;
import com.example.covidscanner.R;
import com.example.covidscanner.classes.Screening;

public class RecordScreening extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    TextView tvName, tvGrade, tvCode;
    CheckBox checkTravel, checkContact, checkSymptoms;
    EditText etTemp;
    Button btnResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_screening);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(ApplicationClass.user.getProperty("name").toString());

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        tvName = findViewById(R.id.tvName);
        tvGrade = findViewById(R.id.tvGrade);
        tvCode = findViewById(R.id.tvCode);
        checkTravel = findViewById(R.id.checkTravel);
        checkContact = findViewById(R.id.checkContact);
        checkSymptoms = findViewById(R.id.checkSymptoms);
        etTemp = findViewById(R.id.etTemp);
        btnResults = findViewById(R.id.btnResults);

        if (getIntent().getStringExtra("code").equals(ApplicationClass.learner.getCode()))
        {
            tvName.setText(String.format("%s %s", ApplicationClass.learner.getName(), ApplicationClass.learner.getSurname()));
            tvGrade.setText(ApplicationClass.learner.getGrade());
            tvCode.setText(ApplicationClass.learner.getCode());
        }
        else
        {
            startActivity(new Intent(RecordScreening.this, MenuActivity.class));
            RecordScreening.this.finish();
        }

        btnResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Screening screening = new Screening();
                screening.setTemperature(Double.parseDouble(etTemp.getText().toString().trim()));
                screening.setSchoolEmail(ApplicationClass.user.getEmail());
                screening.setCode(ApplicationClass.learner.getCode());

                if (checkTravel.isChecked())
                {
                    screening.setHighRiskCountry(checkTravel.isChecked());
                }
                else if (checkContact.isChecked())
                {
                    screening.setInContactWithCovidPeople(checkContact.isChecked());
                }
                else if (checkSymptoms.isChecked())
                {
                    screening.setHaveSymptoms(checkSymptoms.isChecked());
                }
                else
                {
                    screening.setHighRiskCountry(!checkTravel.isChecked());
                    screening.setInContactWithCovidPeople(!checkContact.isChecked());
                    screening.setHaveSymptoms(!checkSymptoms.isChecked());
                }

                tvLoad.setText("Adding screening for learner...loading...");
                showProgress(true);

                Backendless.Persistence.save(screening, new AsyncCallback<Screening>() {
                    @Override
                    public void handleResponse(Screening response)
                    {
                        ApplicationClass.showToast(getApplicationContext(), "correct", "Screening for the learner added.");
                        showProgress(false);

                        startActivity(new Intent(RecordScreening.this, MenuActivity.class));
                        RecordScreening.this.finish();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault)
                    {
                        ApplicationClass.showToast(getApplicationContext(), "error", "Error: " + fault.getMessage());
                        showProgress(false);
                    }
                });

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