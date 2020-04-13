package com.example.adobeoflegends.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.adobeoflegends.R;

public class Load extends Activity {
    /**
    public static final String APP_PREFERENCES = "mysetting";
    public static final String APP_PREFERENCES_COUNTER = "counter";
    private SharedPreferences mSettings;
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        // mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){

                }

            }
        };

    }

    /**
   @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_COUNTER, c);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSettings.contains(APP_PREFERENCES_COUNTER)){
            c = mSettings.getString(APP_PREFERENCES_COUNTER, "0");
            final TextView tv = (TextView) findViewById(R.id.textView2);
            tv.setText(c);
        }
    }
    */
}
