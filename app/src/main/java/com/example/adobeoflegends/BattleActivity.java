package com.example.adobeoflegends;

import android.graphics.Color;
import android.os.Bundle;
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

public class BattleActivity extends AppCompatActivity implements View.OnClickListener {
    Battle battle;
    LinearLayout enemyDeck;
    LinearLayout playerDeck;
    LinearLayout enemyTable;
    LinearLayout playerTable;
    Button nextMove;
    Button buttonOK;
    Button buttonNO;
    TextView playerSTATS;
    TextView enemySTATS;
    LinearLayout tappedCard;
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
        nextMove = (Button) findViewById(R.id.btn_move);
        buttonOK = (Button) findViewById(R.id.btn_OK);
        buttonNO = (Button) findViewById(R.id.btn_NO);
        buttonOK.setClickable(false);
        buttonNO.setClickable(false);
        playerSTATS = (TextView) findViewById(R.id.playerStats);
        enemySTATS = (TextView) findViewById(R.id.enemyStats);
        buttonOK.setText(getEmojiByUnicode(0x2713));
        buttonNO.setText(getEmojiByUnicode(0x2716));
        buttonNO.setOnClickListener(this);
        buttonOK.setOnClickListener(this);
        nextMove.setOnClickListener(this);
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
        FrameLayout frameLayout = (FrameLayout) card.getChildAt(0);
        CardView cardView = (CardView) frameLayout.getChildAt(0);
        LinearLayout linearLayout = (LinearLayout) cardView.getChildAt(0);
        linearLayout.setForeground(getDrawable(R.drawable.cover_of_card));
    }

    void hideCover(LinearLayout card){
        FrameLayout frameLayout = (FrameLayout) card.getChildAt(0);
        CardView cardView = (CardView) frameLayout.getChildAt(0);
        LinearLayout linearLayout = (LinearLayout) cardView.getChildAt(0);
        linearLayout.setForeground(null);
    }

    LinearLayout getParentTable(LinearLayout card){
        return (LinearLayout) ((ViewGroup) card.getParent());
    }

    LinearLayout secondToFirst(View v){
        LinearLayout second = (LinearLayout) v;
        CardView cardView = (CardView) ((ViewGroup) second.getParent());
        FrameLayout frameLayout = (FrameLayout) ((ViewGroup) cardView.getParent());
        return (LinearLayout) ((ViewGroup) frameLayout.getParent());
    }

    boolean isButton(View v){
        return v.getId() == buttonNO.getId() || v.getId() == buttonOK.getId() || v.getId() == nextMove.getId();
    }

    // updateCard - обновляет после боя

    void enemyMove(){
        int num = (int) (Math.random() * enemyDeck.getChildCount());
        moveOnTable((LinearLayout) enemyDeck.getChildAt(num), enemyTable);
        // Toast с результатами хода: потери здоровья у *имя персонажа*
        // добавить метод на боевку, с вложенным updateCard в конце
    }

    @Override
    public void onClick(View v) {

        // Это слушатель на всё
        // Добавить выделение при нажатии, и когда нажата кнопка "ОК" (или галочка), перенести
        // Иначе при нажатии на крестик, отменить выделение
        // Эти кнопки должны появляться только при нажатии на View

        // TABLE - Linear (FIRST)  - Frame - CardView - Linear (SECOND)  - 3 View
        // ОБВОДКА - сделать 2 background одинаковых, 1 с обводкой, другой чистый
        // пока без обводки

        LinearLayout card = null;
        if (!isButton(v)) {
            card = secondToFirst(v);
            // клик и перенос карты
            switch (getParentTable(card).getId()) {
                case R.id.player_deck:
                    buttonOK.setClickable(true);
                    buttonNO.setClickable(true);
                    tappedCard = card;
                    break;
            }
        } else {
            // кнопка OK и отмена
            buttonOK.setClickable(false);
            buttonNO.setClickable(false);
            switch (v.getId()){
                case R.id.btn_OK:
                    moveOnTable(tappedCard, playerTable);
                    break;
                case R.id.btn_NO:
                    break;
                case R.id.btn_move:
                    enemyMove();
                    break;
            }
        }

        //Toast.makeText(this, "ID: " + v.getId(), Toast.LENGTH_SHORT).show();
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
        ll.setTag("TAG");
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(lParams);

        ImageView image = new ImageView(ll.getContext());
        image.setId(View.generateViewId());
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(150, 250);
        image.setLayoutParams(imageParams);
        if (table == playerDeck) {
            image.setImageResource(battle.player.cardList.get(i).pictureID);
            image.setScaleType(ImageView.ScaleType.FIT_START);
        } else {
            image.setImageResource(battle.enemy.cardList.get(i).pictureID);
            image.setScaleType(ImageView.ScaleType.FIT_END);
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

















