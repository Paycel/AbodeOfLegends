package com.example.adobeoflegends.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.gridlayout.widget.GridLayout;

import com.example.adobeoflegends.R;
import com.example.adobeoflegends.adapter.ShopAdapter;
import com.example.adobeoflegends.database.DBHelper;
import com.example.adobeoflegends.objects.Card;
import com.example.adobeoflegends.objects.Player;

import java.util.List;
import java.util.Objects;

public class Shop extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        Objects.requireNonNull(getSupportActionBar()).hide();
        String currentUser = getIntent().getStringExtra("currentUser");
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        Player player = dbHelper.getPlayer(Menu.dataBase, currentUser);
        TextView points = (TextView) findViewById(R.id.tv_points_shop);
        points.setText(getResources().getText(R.string.points).toString() + dbHelper.getPoints(dbHelper.getWritableDatabase(), currentUser));
        GridView table = (GridView) findViewById(R.id.table);
        FragmentManager fragmentManager = getSupportFragmentManager();
        ShopAdapter shopAdapter = new ShopAdapter(fragmentManager, getApplicationContext(), player, currentUser, getResources().getText(R.string.upgrade).toString());
        table.setAdapter(shopAdapter);
        Button btn = (Button) findViewById(R.id.btn_back_shop);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Shop.this, Menu.class);
                startActivity(i);
            }
        });
    }
}
