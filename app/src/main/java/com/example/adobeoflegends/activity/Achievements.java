package com.example.adobeoflegends.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.adobeoflegends.R;
import com.example.adobeoflegends.adapter.AchievementsAdapter;
import com.example.adobeoflegends.database.DBHelper;
import com.example.adobeoflegends.objects.Player;

import java.util.Objects;

public class Achievements extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);
        Objects.requireNonNull(getSupportActionBar()).hide();
        ListView rvAchievements = (ListView) findViewById(R.id.achievements);
        Button buttonBack = (Button) findViewById(R.id.btn_backToMain);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Achievements.this, Menu.class);
                startActivity(i);
            }
        });
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        dbHelper.showInfo(Menu.dataBase);
        Player player = dbHelper.getPlayer(Menu.dataBase, getIntent().getStringExtra("currentUser"));
        player.sort(); // sorting achievements by value
        AchievementsAdapter adapter = new AchievementsAdapter(this, player);
        rvAchievements.setAdapter(adapter);
    }

}
