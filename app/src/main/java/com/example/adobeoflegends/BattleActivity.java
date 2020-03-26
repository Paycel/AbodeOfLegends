package com.example.adobeoflegends;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.HashMap;
import java.util.Map;

public class BattleActivity extends AppCompatActivity implements View.OnClickListener {
    Battle battle;
    LinearLayout enemyDeck;
    LinearLayout playerDeck;
    LinearLayout enemyTable;
    LinearLayout playerTable;
    Button buttonMOVE;
    Button buttonOK;
    Button buttonNO;
    Button buttonINFO;
    TextView playerSTATS;
    TextView enemySTATS;
    LinearLayout tappedCard;
    LinearLayout enemyCard;
    LinearLayout playerCard;
    int moveCount;
    int helper;
    public static final String LOG_TAG = "Fight";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_battle);
        helper = 0;
        battle = new Battle();
        enemyDeck = (LinearLayout) findViewById(R.id.enemy_deck);
        playerDeck = (LinearLayout) findViewById(R.id.player_deck);
        playerTable = (LinearLayout) findViewById(R.id.player_table);
        enemyTable = (LinearLayout) findViewById(R.id.enemy_table);
        buttonMOVE = (Button) findViewById(R.id.btn_move);
        buttonOK = (Button) findViewById(R.id.btn_OK);
        buttonNO = (Button) findViewById(R.id.btn_NO);
        buttonINFO = (Button) findViewById(R.id.btn_INFO);
        deactivateButton(buttonOK);
        deactivateButton(buttonNO);
        activateButton(buttonMOVE);
        playerSTATS = (TextView) findViewById(R.id.playerStats);
        enemySTATS = (TextView) findViewById(R.id.enemyStats);
        buttonOK.setText(getEmojiByUnicode(0x2713));
        buttonNO.setText(getEmojiByUnicode(0x2716));
        buttonINFO.setText(getEmojiByUnicode(0x24D8));
        buttonNO.setOnClickListener(this);
        buttonOK.setOnClickListener(this);
        buttonMOVE.setOnClickListener(this);
        moveCount = 0;
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

    void setCover(LinearLayout card){
        firstToSecond(card).setForeground(getDrawable(R.drawable.cover_of_card));
    }

    void hideCover(LinearLayout card){
        firstToSecond(card).setForeground(null);
    }

    LinearLayout getParentTable(LinearLayout card){
        return (LinearLayout) ((ViewGroup) card.getParent());
    }

    LinearLayout firstToSecond(LinearLayout first){
        FrameLayout frameLayout = (FrameLayout) first.getChildAt(0);
        CardView cardView = (CardView) frameLayout.getChildAt(0);
        return (LinearLayout) cardView.getChildAt(0);
    }

    LinearLayout secondToFirst(View v){
        LinearLayout second = (LinearLayout) v;
        CardView cardView = (CardView) ((ViewGroup) second.getParent());
        FrameLayout frameLayout = (FrameLayout) ((ViewGroup) cardView.getParent());
        return (LinearLayout) ((ViewGroup) frameLayout.getParent());
    }

    boolean isButton(View v){
        return v.getId() == buttonNO.getId() || v.getId() == buttonOK.getId() || v.getId() == buttonMOVE.getId();
    }

    void activateButton(Button b){
        b.setClickable(true);
        b.setEnabled(true);
    }

    void deactivateButton(Button b){
        b.setClickable(false);
        b.setEnabled(false);
    }

    void setCardStats(LinearLayout card){
        LinearLayout s_card = firstToSecond(card);
        TextView stats = (TextView) s_card.getChildAt(1);
        int sword = 0x2694;
        int heart = 0x2665;
        if (getParentTable(card) == playerTable)
            stats.setText(getEmojiByUnicode(sword) + " " + findCard(s_card.getId()).damagePoints + " " + getEmojiByUnicode(heart) + " " + findCard(s_card.getId()).healthPoints);
        else
            stats.setText(getEmojiByUnicode(sword) + " " + findCard(s_card.getId()).damagePoints + " " + getEmojiByUnicode(heart) + " " + findCard(s_card.getId()).healthPoints);
    }

    Card findCard(int id){
        for (int i = 0; i < 2; i++){
            for (int j = 0; j < Battle.numsOfCards; j++){
                if (battle.player.cardList.get(j).viewID == id && i == 0){
                    return battle.player.cardList.get(j);
                } else if (battle.enemy.cardList.get(j).viewID == id && i == 1){
                    return battle.enemy.cardList.get(j);
                }
            }
        }
        return new Card();
    }

    void setTappedCard(LinearLayout card, boolean red){
        if (card == null) return;
        if (red)
            card.setForeground(getDrawable(R.drawable.pressed_card_red));
        else
            card.setForeground(getDrawable(R.drawable.pressed_card_green));
    }

    void clearTappedCard(LinearLayout card){
        if (card != null)
        card.setForeground(null);
    }

    void fight(LinearLayout cardPlayer, LinearLayout cardEnemy, int mode){
        int idPlayer = firstToSecond(cardPlayer).getId();
        int idEnemy = firstToSecond(cardEnemy).getId();
        int result = battle.fight(findCard(idPlayer), findCard(idEnemy), mode);
        if (result == 0){ // победа игрока
            deleteCard(cardEnemy);
            setCardStats(cardPlayer);
        } else if (result == 1){ // победа врага
            deleteCard(cardPlayer);
            setCardStats(cardEnemy);
        } else{ // оба живы
            // обновить карты
            setCardStats(cardEnemy);
            setCardStats(cardPlayer);
        }
    }

    void enemyMove() {
        // выкидывает карту на стол и бьёт случайного игрока

//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
        final int num = (int) (Math.random() * enemyDeck.getChildCount());
        firstToSecond((LinearLayout) enemyDeck.getChildAt(num)).setOnClickListener(this);
        moveOnTable((LinearLayout) enemyDeck.getChildAt(num), enemyTable);

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {

                if (playerTable.getChildCount() == 0) return;
                final int rndEnemy = (int) (Math.random() * enemyTable.getChildCount());
                final int rndPlayer = (int) (Math.random() * playerTable.getChildCount());
                setTappedCard((LinearLayout) playerTable.getChildAt(rndPlayer), true);
                setTappedCard((LinearLayout) enemyTable.getChildAt(rndEnemy), true);
                // ИЩЕМ NULL POINTER EXCEPTION
                // для избежания надо всё делать в хендлере
                if (playerTable.getChildAt(rndPlayer) == null || enemyTable.getChildAt(rndEnemy) == null){
                    Log.d(LOG_TAG, "\nrndEnemy = " + rndEnemy + ", rndPlayer = " +
                            rndPlayer + "\nenemyTable = " + enemyTable.getChildCount() + ", playerTable = " + playerTable.getChildCount());
                    int d = 0;
                }
                fight((LinearLayout) playerTable.getChildAt(rndPlayer), (LinearLayout) enemyTable.getChildAt(rndEnemy), 2);
                activateButton(buttonMOVE);
                clearTappedCard((LinearLayout) playerTable.getChildAt(rndPlayer));
                clearTappedCard((LinearLayout) enemyTable.getChildAt(rndEnemy));
            }
        });


    }

    void playerMove(){
        // бьёт выбранного игрока соперника
        if (playerCard != null && enemyCard != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    fight(playerCard, enemyCard, 1);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {

        // Это слушатель на всё
        // Добавить выделение при нажатии, и когда нажата кнопка "ОК" (или галочка), перенести
        // Иначе при нажатии на крестик, отменить выделение
        // Эти кнопки должны появляться только при нажатии на View

        // TABLE - Linear (FIRST)  - Frame - CardView - Linear (SECOND)  - 3 View


        LinearLayout card = null;
        if (!isButton(v)) {
            card = secondToFirst(v);
            if (helper == 0){
                activateButton(buttonOK);
                activateButton(buttonNO);
                helper++;
            }
            switch (getParentTable(card).getId()) {
                case R.id.player_deck:
                    if (tappedCard != null) clearTappedCard(tappedCard);
                    setTappedCard(card, false);
                    tappedCard = card;
                    break;
                case R.id.player_table:
                    if (playerCard != null) clearTappedCard(playerCard);
                    setTappedCard(card, false);
                    playerCard = card;
                    break;
                case R.id.enemy_table:
                    if (enemyCard != null) clearTappedCard(enemyCard);
                    setTappedCard(card, true);
                    enemyCard = card;
                    break;
            }
        } else {
            // кнопка OK и отмена
            switch (v.getId()) {
                case R.id.btn_OK:
                    moveOnTable(tappedCard, playerTable);
                    if (tappedCard != null) clearTappedCard(tappedCard);
                    deactivateButton(buttonOK);
                    break;
                case R.id.btn_NO:
                    if (tappedCard != null) clearTappedCard(tappedCard);
                    if (playerCard != null) clearTappedCard(playerCard);
                    if (enemyCard != null) clearTappedCard(enemyCard);
                    break;
                case R.id.btn_move:
                    if (moveCount % 2 == 0) {
                       playerMove();
                    }
                    moveCount++;
                    enemyMove();
                    moveCount++;
                    activateButton(buttonOK);
                    break;
            }
        }
        /*
                БОЙ
          0 ход - игрок кидает карты и нажимает ХОД (бить некого)
          1 ход - соперник кидает карты и бьёт
          2 ход - игрок кидает карты и бьёт
          ...
         */
    }

    // отрисовка карт
    void drawCard(LinearLayout card, LinearLayout table, boolean start) {
        if (start) {
            for (int i = 0; i < 4; i++) {
                card = new LinearLayout(table.getContext());
                setParams(card, table, i);
                if (table == enemyDeck){
                    setCover(card);
                }
                table.addView(card);
            }
        } else {
            //addOnTable(card, table);
            //скорее всего бесполезно, убирать start?
        }
    }

    void deleteCard(LinearLayout card){
        LinearLayout table = (LinearLayout) ((ViewGroup) card.getParent());
        table.removeView(card);
    }

    void moveOnTable(LinearLayout card, LinearLayout table){
        if (table.getChildCount() == 4){
            Toast.makeText(this, "No space for card!", Toast.LENGTH_SHORT).show();
            return;
        }
        deleteCard(card);
        hideCover(card);
        table.addView(card);
    }
    // при первом запуске
    void setParams(LinearLayout card, LinearLayout table, int i) {
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

        FrameLayout fl = new FrameLayout(card.getContext());
        fl.setLayoutParams(flParams);
        fl.setId(View.generateViewId());

        CardView cv = new CardView(fl.getContext());
        cv.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        cv.setLayoutParams(cvParams);
        cv.setId(View.generateViewId());

        LinearLayout ll = new LinearLayout(cv.getContext());
        ll.setId(View.generateViewId());
        if (table == playerDeck){
            battle.player.cardList.get(i).viewID = ll.getId();
        } else {
            battle.enemy.cardList.get(i).viewID = ll.getId();
        }

        ll.setGravity(Gravity.CENTER);
        ll.setBackgroundColor(Color.parseColor("#FFFFFF"));
        ll.setPadding(5, 0, 0, 0);
        ll.setClickable(true);
        ll.setEnabled(true);

        // НАЖАТИЕ НА АКТИВНОСТЬ КАРТЫ
        if (table == playerDeck)
           ll.setOnClickListener(this);

        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(lParams);

        ImageView image = new ImageView(ll.getContext());
        image.setId(View.generateViewId());
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(150, 250);
        image.setLayoutParams(imageParams);
        if (table == playerDeck) {
            image.setImageResource(battle.player.cardList.get(i).pictureID);
            image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        } else {
            image.setImageResource(battle.enemy.cardList.get(i).pictureID);
            image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }

        TextView stats = new TextView(ll.getContext());
        stats.setId(View.generateViewId());
        stats.setLayoutParams(l_allWrap_Params);
        stats.setGravity(Gravity.CENTER);
        stats.setTextAppearance(R.style.TextAppearance_AppCompat_Body1);
        int sword = 0x2694;
        int heart = 0x2665;
        if (table == playerDeck)
            stats.setText(getEmojiByUnicode(sword) + " " + battle.player.cardList.get(i).damagePoints + " " + getEmojiByUnicode(heart) + " " + battle.player.cardList.get(i).healthPoints);
        else
            stats.setText(getEmojiByUnicode(sword) + " " + battle.enemy.cardList.get(i).damagePoints + " " + getEmojiByUnicode(heart) + " " + battle.enemy.cardList.get(i).healthPoints);
        TextView desc = new TextView(ll.getContext());
        desc.setId(View.generateViewId());
        l_allWrap_Params.setMargins(0, 10, 0, 5);
        desc.setLayoutParams(l_allWrap_Params);
        desc.setGravity(Gravity.CENTER);
        desc.setTextAppearance(R.style.TextAppearance_AppCompat_Body2);
        if (table == playerDeck)
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

    String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
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
    ...

 */

















