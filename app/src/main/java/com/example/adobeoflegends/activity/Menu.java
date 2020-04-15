package com.example.adobeoflegends.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.adobeoflegends.R;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main_menu);
        Button buttonRandomGame = (Button) findViewById(R.id.btn_RandomGame);
        Button buttonLoad = (Button) findViewById(R.id.btn_Load);
        Button buttonShop = (Button) findViewById(R.id.btn_Shop);
        Button buttonChooseGame = (Button) findViewById(R.id.btn_ChooseLevel);

        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_RandomGame:
                        Intent newGame = new Intent(Menu.this, BattleActivity.class);
                        newGame.putExtra("difficulty", 0);
                        startActivity(newGame);
                        break;
                    case R.id.btn_Load:
                        Intent load = new Intent(Menu.this, Load.class);
                        startActivity(load);
                        break;
                    case R.id.btn_Shop:

                        break;
                    case R.id.btn_ChooseLevel:
                        Intent i = new Intent(Menu.this, Choose_Level.class);
                        startActivity(i);
                        break;
                }
            }
        };

        buttonLoad.setOnClickListener(listener);
        buttonRandomGame.setOnClickListener(listener);
        buttonShop.setOnClickListener(listener);
        buttonChooseGame.setOnClickListener(listener);
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}
