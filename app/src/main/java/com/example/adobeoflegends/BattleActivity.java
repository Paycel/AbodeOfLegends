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

public class BattleActivity extends AppCompatActivity {
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
        View root = findViewById(android.R.id.content).getRootView();
        final LinearLayout card1 = (LinearLayout) findViewById(R.id.card1);

        card1.setOnTouchListener(this);
        root.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        topY = card1.getTop();
                        leftX = card1.getLeft();
                        rightX = card1.getRight();
                        bottomY = card1.getBottom();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        eX = (int) event.getX();
                        eY = (int) event.getY();
                        int x = (int) event.getX() - offset_x;
                        int y = (int) event.getY() - offset_y;
                        int w = getWindowManager().getDefaultDisplay().getWidth() - 50;
                        int h = getWindowManager().getDefaultDisplay().getHeight() - 10;
                        if (x > w) x = w;
                        if (y > h) y = h;
                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(new ViewGroup.MarginLayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                        lp.setMargins(x, y, 0, 0);

                        if (eX > leftX && eX < rightX && eY > topY && eY < bottomY) {

                            dropFlag = true;
                        } else {
                            card1.setBackgroundColor(Color.BLUE);
                        }
                        card1.setLayoutParams(lp);

                        break;

                     case MotionEvent.ACTION_UP:
                         touchFlag = false;
                         if (dropFlag) {
                             dropFlag = false;
                         } else {
                         }
                         break;


                }
                return true;
            }
        });
        ViewTreeObserver observer = playerDeckLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                playerDeckLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                for (int i = 0; i < Battle.numsOfCards; i++)
                createCardsLayouts(playerDeckLayout, battle, i);
            }
        });

    }

    // отчаянная попытка создать вручную, но неудачно
    void createCardsLayouts(ConstraintLayout playerDeck, Battle battle, int i){
        Card cards[] = battle.player.cards;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout card = (LinearLayout) findViewById(R.id.card1);
        // Layout for 1 card
        switch (i){
            case 0:  card = (LinearLayout) findViewById(R.id.card1); break;
            case 1:  card = (LinearLayout) findViewById(R.id.card2); break;
            case 2:  card = (LinearLayout) findViewById(R.id.card3); break;
            case 3:  card = (LinearLayout) findViewById(R.id.card4); break;
        }


        // Card Image
        LinearLayout image = new LinearLayout(card.getContext());
        image.setOrientation(LinearLayout.VERTICAL);

        // Card stats (HP and Mana)
        LinearLayout lstats = new LinearLayout(card.getContext());
        lstats.setOrientation(LinearLayout.VERTICAL);
        // change to 4 layouts
        TextView stats = new TextView(lstats.getContext());
        String statsString = cards[i].damagePoints + " " + cards[i].healthPoints;
        stats.setText(statsString);
        stats.setGravity(Gravity.CENTER);
        lstats.addView(stats, layoutParams);

        // Card description
        LinearLayout ldescription = new LinearLayout(card.getContext());
        ldescription.setOrientation(LinearLayout.VERTICAL);
        TextView description = new TextView(ldescription.getContext());
        description.setText(cards[i].name);
        description.setGravity(Gravity.CENTER);
        ldescription.addView(description, layoutParams);

        // union to main layout
        card.addView(image);
        card.addView(lstats);
        card.addView(ldescription);

    }





}
