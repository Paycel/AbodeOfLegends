package com.example.adobeoflegends.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;

import com.example.adobeoflegends.R;

public class Choose_Level extends AppCompatActivity {
    ConstraintLayout main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.choose_level);
        main = (ConstraintLayout) findViewById(R.id.choose_constr);
        TextView tv = (TextView) findViewById(R.id.tv_choose);
        Button back = (Button) findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Choose_Level.this, Menu.class);
                startActivity(i);
            }
        });
        Display display = getWindowManager().getDefaultDisplay();
        int width = (int) display.getWidth();
        final int dp = (int)((float)width * 0.2);
        for (int i = 0; i < 12; i++){
            Button btn = new Button(main.getContext());
            btn.setId(i+1);
            String text = Integer.toString(i + 1);
            btn.setText(text);
            btn.setTextSize(20);
            btn.setTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.pixel_bold));
            ConstraintLayout.LayoutParams lprams = new ConstraintLayout.LayoutParams(dp, dp);
            btn.setLayoutParams(lprams);
            ConstraintSet set = new ConstraintSet();
            final int finalI = i;
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(Choose_Level.this, BattleActivity.class);
                    intent.putExtra("difficulty", finalI + 1);
                    intent.putExtra("currentUser", getIntent().getStringExtra("currentUser"));
                    startActivity(intent);
                }
            });
            main.addView(btn);
            set.clone(main);
            if (i > 0) {
                if ((i - 1) % 3 == 0) { // центральные
                    set.connect(i + 1, ConstraintSet.LEFT, main.getId(), ConstraintSet.LEFT, dp / 10);
                    set.connect(i + 1, ConstraintSet.RIGHT, main.getId(), ConstraintSet.RIGHT, dp / 10);
                    set.connect(i + 1, ConstraintSet.TOP, i, ConstraintSet.TOP, 0);
                }
                else if (i % 3 == 0){ // левые (2 нижние)
                    set.connect(i + 1, ConstraintSet.LEFT, main.getId(), ConstraintSet.LEFT, dp / 10);
                    set.connect(i + 1, ConstraintSet.TOP, i - 2, ConstraintSet.BOTTOM, dp / 10);
                } else if ((i+1) % 3 == 0) { // правые все
                    set.connect(i + 1, ConstraintSet.RIGHT, main.getId(), ConstraintSet.RIGHT, dp / 10);
                    if (i != 2)
                        set.connect(i + 1, ConstraintSet.TOP, i - 2, ConstraintSet.BOTTOM, dp / 10);
                    else
                        set.connect(i + 1, ConstraintSet.TOP, tv.getId(), ConstraintSet.BOTTOM, dp / 10);
                }
            } else { // левая верхняя
                set.connect(i + 1, ConstraintSet.LEFT, main.getId(), ConstraintSet.LEFT, dp / 10);
                set.connect(i + 1, ConstraintSet.TOP, tv.getId(), ConstraintSet.BOTTOM, dp / 10);
            }
            set.applyTo(main);
            btn.setBackgroundResource(R.drawable.round_button);
        }

    }
}
