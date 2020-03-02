package com.example.adobeoflegends;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main_menu);
        Button buttonNewGame = (Button) findViewById(R.id.btn_NewGame);
        Button buttonLoad = (Button) findViewById(R.id.btn_Load);
        Button buttonShop = (Button) findViewById(R.id.btn_Shop);

        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_NewGame:

                        break;
                    case R.id.btn_Load:
                        Intent i = new Intent(Menu.this, Load.class);
                        startActivity(i);
                        break;
                    case R.id.btn_Shop:

                        break;
                }
            }
        };

        buttonLoad.setOnClickListener(listener);
        buttonNewGame.setOnClickListener(listener);
        buttonShop.setOnClickListener(listener);

    }

}
