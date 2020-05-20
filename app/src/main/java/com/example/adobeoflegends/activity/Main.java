package com.example.adobeoflegends.activity;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.adobeoflegends.R;

import java.util.Objects;

public class Main extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 5 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        Objects.requireNonNull(getSupportActionBar()).hide();
        ImageView logo_pic = (ImageView) findViewById(R.id.logo_pic);
        ImageView logo_name = (ImageView) findViewById(R.id.logo_name);
        Animation anim_pic = AnimationUtils.loadAnimation(this, R.anim.splash_logo);
        Animation anim_name = AnimationUtils.loadAnimation(this, R.anim.splash_logo_name);
        logo_pic.startAnimation(anim_pic);
        logo_name.startAnimation(anim_name);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Main.this, Menu.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
