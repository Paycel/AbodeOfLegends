package com.example.adobeoflegends;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Layout;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adobeoflegends.Battle;
import com.example.adobeoflegends.R;
import com.google.android.material.resources.TextAppearance;

public class BattleActivity extends AppCompatActivity implements View.OnClickListener {
    Battle battle;
    LinearLayout enemyDeck;
    LinearLayout playerDeck;
    LinearLayout enemyTable;
    LinearLayout playerTable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_battle);
        battle = new Battle();
        enemyDeck = (LinearLayout) findViewById(R.id.enemy_deck);
        playerDeck = (LinearLayout) findViewById(R.id.player_deck);
        playerTable = (LinearLayout) findViewById(R.id.player_table);
        enemyTable = (LinearLayout) findViewById(R.id.enemy_table);
        ViewTreeObserver observer = playerDeck.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                playerDeck.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                enemyDeck.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                drawCard(null, enemyDeck, true);
                drawCard(null, playerDeck, true);
            }
        });

    }

    @Override
    public void onClick(View v) {

        // Здесь будет перенос View на стол
        // Добавить выделение при нажатии, и когда нажата кнопка "ОК" (или галочка), перенести
        // Иначе при нажатии на крестик, отменить выделение
        // Эти кнопки должны появляться только при нажатии на View

        // TABLE - Linear (FIRST)  - Frame - CardView - Linear (SECOND)  - 3 View

        LinearLayout second = (LinearLayout) v;
        CardView cardView = (CardView) ((ViewGroup) second.getParent());
        FrameLayout frameLayout = (FrameLayout) ((ViewGroup) cardView.getParent());
        LinearLayout card = (LinearLayout) ((ViewGroup) frameLayout.getParent());
        addOnTable(card, enemyTable);

        Toast.makeText(this, "ID: " + v.getId(), Toast.LENGTH_SHORT).show();
    }

    void setParams(LinearLayout card, LinearLayout table, int i){
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams l_allWrap_Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        l_allWrap_Params.setMargins(0, 0, 0, 0);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(table.getWidth() / 4 - 15, table.getHeight());
        cardParams.setMargins(5, 0, 5, 0);
        FrameLayout.LayoutParams flParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        CardView.LayoutParams cvParams = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
        cvParams.setMargins(0, 20, 0, 0);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setLayoutParams(cardParams);
        //card.setOnClickListener(this);

        FrameLayout fl = new FrameLayout(card.getContext());
        fl.setLayoutParams(flParams);
        fl.setId(View.generateViewId());

        CardView cv = new CardView(fl.getContext());
        cv.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        cv.setLayoutParams(cvParams);
        cv.setId(View.generateViewId());

        LinearLayout ll = new LinearLayout(cv.getContext());
        ll.setId(View.generateViewId());
        ll.setGravity(Gravity.CENTER);
        ll.setBackgroundColor(Color.parseColor("#FFFFFF"));
        ll.setPadding(5, 0, 0, 0);
        ll.setClickable(true);
        ll.setEnabled(true);
        // НАЖАТИЕ НА АКТИВНОСТЬ КАРТЫ
        ll.setOnClickListener(this);
        ll.setTag("TAG");
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(lParams);

        ImageView image = new ImageView(ll.getContext());
        image.setId(View.generateViewId());
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(150, 250);
        image.setLayoutParams(imageParams);
        if (table == (LinearLayout) findViewById(R.id.player_deck)) {
            image.setImageResource(battle.player.cardList.get(i).pictureID);
            image.setScaleType(ImageView.ScaleType.FIT_START);
        }
        else {
            image.setImageResource(battle.enemy.cardList.get(i).pictureID);
            image.setScaleType(ImageView.ScaleType.FIT_END);
        }

        TextView stats = new TextView(ll.getContext());
        stats.setId(View.generateViewId());
        stats.setLayoutParams(l_allWrap_Params);
        stats.setGravity(Gravity.CENTER);
        stats.setTextAppearance(R.style.TextAppearance_AppCompat_Body1);
        if (table == (LinearLayout) findViewById(R.id.player_deck))
            stats.setText(battle.player.cardList.get(i).damagePoints + " " + battle.player.cardList.get(i).healthPoints);
        else
            stats.setText(battle.enemy.cardList.get(i).damagePoints + " " + battle.player.cardList.get(i).healthPoints);
        TextView desc = new TextView(ll.getContext());
        desc.setId(View.generateViewId());
        l_allWrap_Params.setMargins(0, 15, 0, 8);
        desc.setLayoutParams(l_allWrap_Params);
        desc.setGravity(Gravity.CENTER);
        desc.setTextAppearance(R.style.TextAppearance_AppCompat_Body2);
        if (table == (LinearLayout) findViewById(R.id.player_deck))
            desc.setText(battle.player.cardList.get(i).name);
        else
            desc.setText(battle.enemy.cardList.get(i).name);
        card.setEnabled(true);
        card.setClickable(true);
        card.setId(table.getChildCount() + 1);
        card.addView(fl);
        fl.addView(cv);
        cv.addView(ll);
        ll.addView(image);
        ll.addView(stats);
        ll.addView(desc);
    }

    // отрисовка карт
    void drawCard(LinearLayout card, LinearLayout table, boolean start) {
        if (start) {
            for (int i = 0; i < 4; i++) {
                card = new LinearLayout(table.getContext());
                setParams(card, table, i);
                table.addView(card);
            }
        } else {
            //addOnTable(card, table);
        }
    }

    void deleteCard(LinearLayout card){
        LinearLayout table = (LinearLayout) ((ViewGroup) card.getParent());
        table.removeView(card);
    }

    void addOnTable(LinearLayout card, LinearLayout table){
        deleteCard(card);
        table.addView(card);
    }


}

/*
    Запускается игра, отрисовываются карты в колодах
    Нажатие на карту - вызов 2 кнопок (принять или отмена)
    Если отмена - убрать кнопки
    Если принять - переместить карту на стол, из колоды удаляется
    *СДЕЛАТЬ МЕТОД НА ВЫРАВНИВАНИЕ В СЛУЧАЕ УДАЛЕНИЯ КАРТЫ ИЗ КОЛОДЫ - вызывается при Принятии*
    *Ход противника - выкидывает карту* --- дописать логику
    Следующий ход: карты, выброшенные в прошлом ходе становятся боевыми
    Сначала действует игрок, потом противник -- меняются местами
    В случае ХП < 0: удаляется карта со стола
    *СДЕЛАТЬ МЕТОД НА ВЫРАВНИВАНИЕ В СЛУЧАЕ УДАЛЕНИЯ КАРТЫ СО СТОЛА*

    Данные методы можно пихнуть в один

    Всё то же самое, но на методах

    onCreate
    ---ХОД ИГРОКА---
    onClick - нажатие на карту
    onClick - нажатие на кнопку -> addOnTable
    addOnTable:
        deleteCard
        drawCard
    ---ХОД ПРОТИВНИКА---
    enemyMove:
        deleteCard
        drawCard


 */

















