package com.example.adobeoflegends;

import android.app.Activity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BattleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_battle);
        final Battle battle = new Battle();
        final LinearLayout playerDeckLayout = (LinearLayout) findViewById(R.id.player_deck);

        ViewTreeObserver observer = playerDeckLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                playerDeckLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                createCardsLayouts(playerDeckLayout, battle, 0);
                createCardsLayouts(playerDeckLayout, battle, 1);

            }
        });


    }


    void createCardsLayouts(LinearLayout linearLayout, Battle battle, int i){
        Card cards[] = battle.player.cards;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setWeightSum(2);

        // Layout for 1 card
        LinearLayout linearLayout1 = new LinearLayout(linearLayout.getContext());
        linearLayout1.setOrientation(LinearLayout.VERTICAL);
        linearLayout1.setWeightSum(3);

        // Card Image
        LinearLayout linearLayout2 = new LinearLayout(linearLayout1.getContext());
        linearLayout2.setOrientation(LinearLayout.VERTICAL);

        // Card stats (HP and Mana)
        LinearLayout linearLayout3 = new LinearLayout(linearLayout1.getContext());
        linearLayout3.setOrientation(LinearLayout.VERTICAL);
        // change to 4 layouts
        TextView stats = new TextView(linearLayout3.getContext());
        String statsString = cards[i].damagePoints + " " + cards[i].healthPoints;
        stats.setText(statsString);
        stats.setGravity(Gravity.CENTER);
        linearLayout3.addView(stats, layoutParams);

        // Card description
        LinearLayout linearLayout4 = new LinearLayout(linearLayout1.getContext());
        linearLayout4.setOrientation(LinearLayout.VERTICAL);
        TextView description = new TextView(linearLayout4.getContext());
        description.setText(cards[i].name);
        description.setGravity(Gravity.CENTER);
        linearLayout4.addView(description, layoutParams);

        // union to main layout
        linearLayout1.addView(linearLayout2);
        linearLayout1.addView(linearLayout3);
        linearLayout1.addView(linearLayout4);
        linearLayout.addView(linearLayout1, layoutParams);

    }





}
