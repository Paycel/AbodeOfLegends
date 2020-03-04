package com.example.adobeoflegends;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adobeoflegends.Battle;
import com.example.adobeoflegends.R;

public class BattleActivity extends AppCompatActivity implements View.OnTouchListener{
    int topY, leftX, rightX, bottomY;
    int eX, eY;
    int offset_x = 0, offset_y = 0;
    boolean dropFlag = false, touchFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_battle);
        final Battle battle = new Battle();
        final ConstraintLayout playerDeckLayout = (ConstraintLayout) findViewById(R.id.player_deck);


        ViewTreeObserver observer = playerDeckLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                playerDeckLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                createCardsViews(playerDeckLayout, battle, 0);
            }
        });

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        LinearLayout.LayoutParams par = (LinearLayout.LayoutParams) v.getLayoutParams();
        switch(v.getId())
        {




        }
        return true;
    }

    // отчаянная попытка создать вручную, но неудачно
    void createCardsViews(ConstraintLayout playerDeck, Battle battle, int i){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        LinearLayout card = (LinearLayout) findViewById(R.id.card1);
        RecyclerView rv1 = (RecyclerView) findViewById(R.id.card1view);
        rv1.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(card.getContext());
        rv1.setLayoutManager(llm);
        RVAdapter adapter = new RVAdapter(battle.player.cardList);
        rv1.setAdapter(adapter);


        //        {
//            // Card stats (HP and Mana)
//            LinearLayout lstats = new LinearLayout(card.getContext());
//            lstats.setOrientation(LinearLayout.VERTICAL);
//            // change to 4 layouts
//            TextView stats = new TextView(lstats.getContext());
//            String statsString = cards[i].damagePoints + " " + cards[i].healthPoints;
//            stats.setText(statsString);
//            stats.setGravity(Gravity.CENTER);
//            lstats.addView(stats, layoutParams);
//
//            // Card description
//            LinearLayout ldescription = new LinearLayout(card.getContext());
//            ldescription.setOrientation(LinearLayout.VERTICAL);
//            TextView description = new TextView(ldescription.getContext());
//            description.setText(cards[i].name);
//            description.setGravity(Gravity.CENTER);
//            ldescription.addView(description, layoutParams);
//
//            // union to main layout
//            card.addView(image);
//            card.addView(lstats);
//            card.addView(ldescription);
//        }

    }





}
