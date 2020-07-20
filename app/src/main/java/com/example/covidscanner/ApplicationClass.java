package com.example.covidscanner;

import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.example.covidscanner.classes.Learner;

public class ApplicationClass extends Application
{
    public static final String APPLICATION_ID = "611DEFEF-E940-9D09-FFA0-0990239C4800";
    public static final String API_KEY = "F2B8EEA6-D601-49D2-8987-A6002A6E56BA";
    public static final String SERVER_URL = "https://api.backendless.com";

    public static BackendlessUser user = null;
    public static Learner learner = null;

    @Override
    public void onCreate() {
        super.onCreate();

        Backendless.setUrl( SERVER_URL );
        Backendless.initApp( getApplicationContext(),
                APPLICATION_ID,
                API_KEY );
    }

    public static void showToast (Context context, String image, String message)
    {
        View toastView = LayoutInflater.from(context).inflate(R.layout.toast, null);
        TextView tvMessage = toastView.findViewById(R.id.tvMessage);
        ImageView ivPic = toastView.findViewById(R.id.ivPic);

        tvMessage.setText(message);

        if (image.equals("error"))
        {
            ivPic.setImageResource(R.drawable.error);
        }
        else if (image.equals("correct"))
        {
            ivPic.setImageResource(R.drawable.check);
        }

        Toast toast = new Toast(context);
        toast.setView(toastView);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.FILL_HORIZONTAL,0,0);
        toast.show();
    }//end showToast
}
