package com.example.adobeoflegends;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
                        Intent newGame = new Intent(Menu.this, BattleActivity.class);
                        startActivity(newGame);
                        break;
                    case R.id.btn_Load:
                        Intent load = new Intent(Menu.this, Load.class);
                        startActivity(load);
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
