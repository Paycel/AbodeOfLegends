package com.example.adobeoflegends;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.FragmentManager;

public class BattleActivity extends AppCompatActivity implements View.OnClickListener {
    public static Battle battle;
    LinearLayout enemyDeck;
    public static LinearLayout playerDeck;
    LinearLayout enemyTable;
    LinearLayout playerTable;
    LinearLayout INFO;
    public static LinearLayout MAIN;
    Button buttonMOVE;
    Button buttonOK;
    Button buttonFACE;
    Button buttonINFO;
    TextView playerSTATS;
    TextView enemySTATS;
    LinearLayout tappedCard;
    LinearLayout enemyCard;
    LinearLayout playerCard;
    FragmentManager fragmentManager;
    EndGameDialog dialog_end;
    ShowCardDialog dialog_show;
    int moveCount;
    int helper;
    int rndEnemy, rndPlayer;
    int player_add, enemy_add;
    public static final String LOG_TAG_FIGHT = "Fight";
    public static final String LOG_TAG_SIZE = "Size";
    public static final String LOG_TAG_DELAY = "Delay";
    View.OnLongClickListener onLongClickListener;
    private int currentApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        currentApi = android.os.Build.VERSION.SDK_INT;
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if(currentApi >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        decorView.setSystemUiVisibility(flags);
                    }
                }
            });
        }
        setContentView(R.layout.activity_battle);
        setElementsAndParams();
        ViewTreeObserver observer = playerDeck.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                playerDeck.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                enemyDeck.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                findViewById(R.id.MAIN).getViewTreeObserver().removeGlobalOnLayoutListener(this);
                drawCard(null, enemyDeck, true);
                drawCard(null, playerDeck, true);
                updateSTATS();
            }
        });
        onLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showCardInfo(v);
                return true;
            }
        };
    }

    void imagePress(View v, boolean isImagePressed, int duration){
        findCard(firstToSecond((LinearLayout) v).getId()).isTapped = !isImagePressed;
        if (!isImagePressed) v.animate().scaleX(0.7f).scaleY(0.7f).setDuration(duration);
        if (isImagePressed) v.animate().scaleX(1f).scaleY(1f).setDuration(duration);
    }

    void setElementsAndParams(){
        fragmentManager = getSupportFragmentManager();
        helper = 0;
        battle = new Battle();
        player_add = 4;
        enemy_add = 4;
        MAIN = (LinearLayout) findViewById(R.id.MAIN);
        enemyDeck = (LinearLayout) findViewById(R.id.enemy_deck);
        playerDeck = (LinearLayout) findViewById(R.id.player_deck);
        playerTable = (LinearLayout) findViewById(R.id.player_table);
        enemyTable = (LinearLayout) findViewById(R.id.enemy_table);
        INFO = (LinearLayout) findViewById(R.id.info);
        buttonMOVE = (Button) findViewById(R.id.btn_move);
        buttonOK = (Button) findViewById(R.id.btn_OK);
        buttonFACE = (Button) findViewById(R.id.btn_ToFace);
        buttonINFO = (Button) findViewById(R.id.btn_INFO);
        deactivateButton(buttonOK);
        deactivateButton(buttonFACE);
        activateButton(buttonMOVE);
        playerSTATS = (TextView) findViewById(R.id.playerStats);
        enemySTATS = (TextView) findViewById(R.id.enemyStats);
        buttonOK.setText(getEmojiByUnicode(Integer.parseInt(getResources().getText(R.string.OK).toString(), 16)));
        buttonFACE.setText(getEmojiByUnicode(Integer.parseInt(getResources().getText(R.string.sword).toString(), 16)));
        buttonINFO.setText(getEmojiByUnicode(Integer.parseInt(getResources().getText(R.string.INFO).toString(), 16)));
        buttonFACE.setOnClickListener(this);
        buttonOK.setOnClickListener(this);
        buttonMOVE.setOnClickListener(this);
        moveCount = 0;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(currentApi >= Build.VERSION_CODES.KITKAT && hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                              View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    // LINEAR
    void setCover(LinearLayout card){
        firstToSecond(card).setForeground(getDrawable(R.drawable.cover_of_card));
    }

    // LINEAR
    void hideForeground(LinearLayout card){
        firstToSecond(card).setForeground(null);
    }

    //затемнение
    void setDarkOnCard(LinearLayout card){
        firstToSecond(card).setForeground(new ColorDrawable(getResources().getColor(R.color.dark)));
    }

    // Long Listener
    void showCardInfo(View v){
        dialog_show = new ShowCardDialog((ConstraintLayout) v);
        dialog_show.show(fragmentManager, "ShowCard_Dialog");

    }

    // LINEAR
    public static LinearLayout getParentTable(LinearLayout card){
        return (LinearLayout) ((ViewGroup) card.getParent());
    }

    ConstraintLayout firstToSecond(LinearLayout first){
        FrameLayout frameLayout = (FrameLayout) first.getChildAt(0);
        CardView cardView = (CardView) frameLayout.getChildAt(0);
        return (ConstraintLayout) cardView.getChildAt(0);
    }

    public static LinearLayout secondToFirst(View v){
        ConstraintLayout second = (ConstraintLayout) v;
        CardView cardView = (CardView) ((ViewGroup) second.getParent());
        FrameLayout frameLayout = (FrameLayout) ((ViewGroup) cardView.getParent());
        return (LinearLayout) ((ViewGroup) frameLayout.getParent());
    }

    boolean isButton(View v){
        return v.getId() == buttonFACE.getId() || v.getId() == buttonOK.getId() || v.getId() == buttonMOVE.getId() || v.getId() == buttonINFO.getId();
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
        ConstraintLayout s_card = firstToSecond(card);
        // 0 - Пушной - УРОН
        // 1 - крестьянин - ХП
        // 2 - Дракон - мана
        ImageView HP = (ImageView) s_card.getChildAt(1);
        Card person = findCard(s_card.getId());
        switch (person.healthPoints){
            case 1: HP.setImageResource(R.drawable.one); break;
            case 2: HP.setImageResource(R.drawable.two); break;
            case 3: HP.setImageResource(R.drawable.three); break;
            case 4: HP.setImageResource(R.drawable.four); break;
            case 5: HP.setImageResource(R.drawable.five); break;
            case 6: HP.setImageResource(R.drawable.six); break;
            case 7: HP.setImageResource(R.drawable.seven); break;
            case 8: HP.setImageResource(R.drawable.eight); break;
            case 9: HP.setImageResource(R.drawable.nine); break;
            case 10: HP.setImageResource(R.drawable.ten); break;
        }
    }

    public static Card findCard(int id) {
        for (int j = 0; j < battle.player.cardList.size(); j++) {
            if (battle.player.cardList.get(j).viewID == id) {
                return battle.player.cardList.get(j);
            }
        }
        for (int j = 0; j < battle.enemy.cardList.size(); j++) {
            if (battle.enemy.cardList.get(j).viewID == id) {
                return battle.enemy.cardList.get(j);
            }
        }
        // never reached
        return new Card();
    }

    int isEnd(){
        if (battle.player.healthPoints <= 0 || (battle.player.cardList.size() == 0)) return 1; // проиграл игрок
        else if (battle.enemy.healthPoints <= 0 || (battle.enemy.cardList.size() == 0)) return 2;
        return 0;
    }

    void endLevel(int mode){
        if (mode == 0) return;
        dialog_end = new EndGameDialog(mode);
        dialog_end.show(fragmentManager, "End_Dialog");
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
        if (cardEnemy == null) return;
        int idPlayer = firstToSecond(cardPlayer).getId();
        int idEnemy = firstToSecond(cardEnemy).getId();
        int result = battle.fight(findCard(idPlayer), findCard(idEnemy), mode);
        if (result == 0){ // победа игрока
            battle.enemy.cardList.remove(findCard(firstToSecond(cardEnemy).getId()));
            deleteCard(cardEnemy);
            setCardStats(cardPlayer);
        } else if (result == 1){ // победа врага
            battle.player.cardList.remove(findCard(firstToSecond(cardPlayer).getId()));
            deleteCard(cardPlayer);
            setCardStats(cardEnemy);
        } else{ // оба живы
            // обновить карты
           setCardStats(cardEnemy);
           setCardStats(cardPlayer);
        }
        updateSTATS();
    }

    void enemyMove() {
        // выкидывает карту на стол и бьёт случайного игрока
        final int num = (int) (Math.random() * enemyDeck.getChildCount());
        final boolean beatPlayer = Math.floor(Math.random() * 2) == 1; // true - бить игрока
        final boolean emptyDeck = enemyDeck.getChildCount() == 0;
        if (!emptyDeck) {
            LinearLayout temp = (LinearLayout) enemyDeck.getChildAt(num);
            firstToSecond(temp).setOnClickListener(this);
            firstToSecond(temp).setOnLongClickListener(onLongClickListener);
            moveOnTable(temp, enemyTable);
            setDarkOnCard(temp);
        }
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean allNotActive = true;
                LinearLayout enemy = null;
                int delayMS = 1;
                rndEnemy = -1;
                for (int i = 0; i < enemyTable.getChildCount(); i++){
                    enemy = (LinearLayout) enemyTable.getChildAt(i);
                    if (isActiveCard(enemy)){
                        allNotActive = false;
                        delayMS = 2000;
                        break;
                    }
                }
                if (!allNotActive) {
                    do {
                        int i = (int) (Math.random() * enemyTable.getChildCount());
                        enemy = (LinearLayout) enemyTable.getChildAt(i);
                        if (isActiveCard(enemy)) {
                            rndEnemy = i;
                            break;
                        }
                    } while (rndEnemy == -1);
                    enemy = (LinearLayout) enemyTable.getChildAt(rndEnemy);
                }
                // бьём игрока
                if (beatPlayer && !allNotActive){
                    setTappedCard(enemy, true);
                    final LinearLayout finalEnemy = enemy;
                    Log.d(LOG_TAG_FIGHT, "BEAT PLAYER!");
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            battle.player.healthPoints -= findCard(firstToSecond(finalEnemy).getId()).damagePoints;
                            updateSTATS();
                            clearTappedCard(finalEnemy);
                            endLevel(isEnd());
                        }
                    }, 2000);
                    return;
                }
                // бьём персонажа
                if (playerTable.getChildCount() == 0) return;
                if (!allNotActive){
                    rndPlayer = (int) (Math.random() * playerTable.getChildCount());
                    LinearLayout player = (LinearLayout) playerTable.getChildAt(rndPlayer);
                    setTappedCard(enemy, true);
                    setTappedCard(player, true);
                    final int finalDelayMS = delayMS;
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (playerTable.getChildCount() == 0) return;
                            fight((LinearLayout) playerTable.getChildAt(rndPlayer), (LinearLayout) enemyTable.getChildAt(rndEnemy), 2);
                            clearTappedCard((LinearLayout) playerTable.getChildAt(rndPlayer));
                            clearTappedCard((LinearLayout) enemyTable.getChildAt(rndEnemy));
                            endLevel(isEnd());
                            Log.d(LOG_TAG_DELAY, "DELAY = " + finalDelayMS);
                        }
                    }, finalDelayMS);
                }
                setActiveCard((LinearLayout) enemyTable.getChildAt(enemyTable.getChildCount() - 1));

            }
        }, 1000);
    }

    void playerMove(final LinearLayout player, final LinearLayout enemy) {
        // бьёт выбранного игрока соперника
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (player != null && enemy != null)
                    fight(player, enemy, 1);
                endLevel(isEnd());
            }
        });
    }

    boolean isActiveCard(LinearLayout card){
        return findCard(firstToSecond(card).getId()).active;
    }

    boolean isTappedCard(LinearLayout card) {
        return findCard(firstToSecond(card).getId()).isTapped;
    }

    void updateSTATS(){
        playerSTATS.setText(getResources().getText(R.string.player).toString() + "\n" + getResources().getText(R.string.hp) + battle.player.healthPoints + " "
                + getResources().getText(R.string.mp) + battle.player.manaPoints + "\n" + getResources().getText(R.string.cards) + (battle.player.cardList.size() - playerTable.getChildCount() - playerDeck.getChildCount()));
        enemySTATS.setText(getResources().getText(R.string.enemy).toString() + "\n" + getResources().getText(R.string.hp) + battle.enemy.healthPoints + "\n" +
                getResources().getText(R.string.cards) + (battle.enemy.cardList.size() - enemyTable.getChildCount() - enemyDeck.getChildCount()));
    }

    void setActiveCard(LinearLayout card){
        findCard(firstToSecond(card).getId()).active = true;
    }

    void setDeactiveCard(LinearLayout card){
        findCard(firstToSecond(card).getId()).active = false;
    }

    boolean EnoughMP(LinearLayout card){
        if (card == null) return false;
        if (findCard(firstToSecond(card).getId()).cost > battle.player.manaPoints){
            Toast.makeText(this, "Недостаточно маны!", Toast.LENGTH_SHORT).show();
            imagePress(card, true, 200);
            return false;
        }
        return true;
    }

    boolean noPlace(){
        if (playerTable.getChildCount() == 4) {
            if (tappedCard != null) imagePress(tappedCard, true, 200);
            Toast.makeText(this, getResources().getText(R.string.no_space).toString(), Toast.LENGTH_SHORT).show();
        }
        return playerTable.getChildCount() == 4;
    }

    @Override
    public void onClick(View v) {
        // Это слушатель на всё, можно было разделить, но у автора беды с башкой
        // TABLE - Linear (FIRST)  - Frame - CardView - Linear (SECOND)  - 2 View
        LinearLayout card = null;
        if (!isButton(v)) {
            card = secondToFirst(v);
            if (helper == 0){
                activateButton(buttonOK);
                activateButton(buttonFACE);
                helper++;
            }
            switch (getParentTable(card).getId()) {
                case R.id.player_deck:
                    if (tappedCard != null)
                        if (tappedCard != card) {
                            imagePress(tappedCard, true, 200);
                            imagePress(card, false, 200);
                        } else
                            imagePress(tappedCard, isTappedCard(tappedCard), 200);
                    else
                        imagePress(card, false, 200);
                    tappedCard = card;
                    break;
                case R.id.player_table:
                    if (!isActiveCard(card)) return; // не обрабатывается нажатие неактивной карты
                    if (playerCard != null)
                        if (playerCard != card) {
                            imagePress(playerCard, true, 200);
                            imagePress(card, false, 200);
                        } else
                            imagePress(playerCard, isTappedCard(playerCard), 200);
                    else
                        imagePress(card, false, 200);
                    playerCard = card;
                    break;
                case R.id.enemy_table:
                    if (enemyCard != null)
                        if (enemyCard != card) {
                            imagePress(enemyCard, true, 200);
                            imagePress(card, false, 200);
                        } else
                            imagePress(enemyCard, isTappedCard(enemyCard), 200);
                    else
                        imagePress(card, false, 200);
                    enemyCard = card;
                    break;
            }
        } else {
            switch (v.getId()) {
                case R.id.btn_OK:
                    if (tappedCard == null || noPlace() || !EnoughMP(tappedCard)) return;
                    battle.player.manaPoints -= findCard(firstToSecond(tappedCard).getId()).cost;
                    updateSTATS();
                    moveOnTable(tappedCard, playerTable);
                    imagePress(tappedCard, true, 0);
                    setDarkOnCard(tappedCard);
                    tappedCard = null;
                    break;
                case R.id.btn_ToFace:
                    if (playerCard == null){
                        Toast.makeText(this, getResources().getText(R.string.choose_card).toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        if (enemyCard == null && playerCard != null){
                            battle.enemy.healthPoints -= findCard(firstToSecond(playerCard).getId()).damagePoints;
                        } else if (enemyCard != null & playerCard != null){
                            playerMove(playerCard, enemyCard);
                        }
                        updateSTATS();
                        if (playerCard != null) {
                            imagePress(playerCard, true, 0);
                        }
                        if (enemyCard != null) imagePress(enemyCard, true, 0);
                        setDarkOnCard(playerCard);
                        setDeactiveCard(playerCard);
                        playerCard = null;
                        enemyCard = null;
                        endLevel(isEnd());
                    }
                    break;
                case R.id.btn_move:
                    for (int i = 0; i < playerTable.getChildCount(); i++)
                        if (!isActiveCard((LinearLayout) playerTable.getChildAt(i)))
                            setActiveCard((LinearLayout) playerTable.getChildAt(i));
                    deactivateButton(buttonMOVE);
                    deactivateButton(buttonOK);
                    moveCount++;
                    enemyMove();
                    moveCount++;
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            activateButton(buttonMOVE);
                            activateButton(buttonOK);
                            for (int i = 0; i < playerTable.getChildCount(); i++)
                                hideForeground((LinearLayout) playerTable.getChildAt(i));
                            if ((LinearLayout) enemyTable.getChildAt(enemyTable.getChildCount() - 1) != null)
                                hideForeground((LinearLayout) enemyTable.getChildAt(enemyTable.getChildCount() - 1));
                        }
                    }, 3000);
                    if (tappedCard != null) imagePress(tappedCard, true, 0);
                    if (playerCard != null) imagePress(playerCard, true, 0);
                    if (enemyCard != null) imagePress(enemyCard, true, 0);
                    tappedCard = null;
                    enemyCard = null;
                    playerCard = null;
                    addCard(true);
                    addCard(false);
                    Log.d(LOG_TAG_FIGHT, "SIZE_ENEMY = " + battle.enemy.cardList.size());
                    break;
            }
        }
    }

    void addCard(boolean player){
        Log.d(LOG_TAG_FIGHT, "PLAYER_ADD = " + player_add + "\n" + "ENEMY_ADD = " + enemy_add);
        if (player){
            if (playerDeck.getChildCount() == 4){
                Toast.makeText(this, getResources().getText(R.string.no_space).toString(), Toast.LENGTH_SHORT).show();
                return;
            } else if (player_add >= battle.player.cardList.size()){
                Toast.makeText(this, getResources().getText(R.string.no_cards).toString(), Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                drawCard(null, playerDeck, false);
                player_add++;
            }
        }
        else{
            if (enemyDeck.getChildCount() == 4 || enemy_add >= battle.enemy.cardList.size()){
                return;
            }
            else{
                drawCard(null, enemyDeck, false);
                enemy_add++;
            }
        }
        updateSTATS();
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
            card = new LinearLayout(table.getContext());
            int i = 0;
            if (table == playerDeck) i = player_add;
            else i = enemy_add;
            setParams(card, table, i);
            if (table == enemyDeck){
                setCover(card);
            }
            table.addView(card);
        }
    }

    void deleteCard(LinearLayout card){
        if (card == null) return;
        LinearLayout table = (LinearLayout) ((ViewGroup) card.getParent());
        table.removeView(card);
    }

    void moveOnTable(LinearLayout card, LinearLayout table){
        if (card == null || table == null) return;
        deleteCard(card);
        hideForeground(card);
        table.addView(card);
    }

    // при первом запуске
    void setParams(LinearLayout card, LinearLayout table, int i) {
        ConstraintLayout.LayoutParams lParams = new ConstraintLayout.LayoutParams(table.getWidth() / 4 - 15, table.getHeight() - 5);
        LinearLayout.LayoutParams l_allWrap_Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        l_allWrap_Params.setMargins(0, 0, 0, 0);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(table.getWidth() / 4 - 15, table.getHeight() - 5);
        cardParams.setMargins(5, 5, 5, 5);
        FrameLayout.LayoutParams flParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        CardView.LayoutParams cvParams = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
        cvParams.setMargins(0, 0, 0, 0);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setLayoutParams(cardParams);

        int HEIGHT_MAIN = MAIN.getHeight();
        int HEIGHT_TABLE = table.getHeight();
        int HEIGHT_INFO = HEIGHT_MAIN - HEIGHT_TABLE * 4;
        Log.d(LOG_TAG_SIZE, "HEIGHT_MAIN = " + HEIGHT_MAIN + "\nHEIGHT_TABLE = " + HEIGHT_TABLE + "\nHEIGHT_INFO = " + HEIGHT_INFO);

        FrameLayout fl = new FrameLayout(card.getContext());
        fl.setLayoutParams(flParams);
        fl.setId(View.generateViewId());

        CardView cv = new CardView(fl.getContext());
        cv.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        cv.setLayoutParams(cvParams);
        cv.setId(View.generateViewId());

        ConstraintLayout ll = new ConstraintLayout(cv.getContext());
        ll.setId(View.generateViewId());
        if (table == playerDeck) {
            battle.player.cardList.get(i).viewID = ll.getId();
        } else {
            battle.enemy.cardList.get(i).viewID = ll.getId();
        }
        ll.setLayoutParams(lParams);
        ll.setBackgroundResource(findCard(ll.getId()).pictureID);
        ll.setPadding(5, 0, 0, 0);
        ll.setClickable(true);
        ll.setEnabled(true);

        // НАЖАТИЕ НА АКТИВНОСТЬ КАРТЫ
        if (table == playerDeck) {
            ll.setOnClickListener(this);
            ll.setOnLongClickListener(onLongClickListener);
        }

        // View HP && DP - начало
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(50, 50);

        ImageView imageHP = new ImageView(ll.getContext());
        imageHP.setId(View.generateViewId());
        imageHP.setLayoutParams(imageParams);
        Card myCard = findCard(ll.getId());
        switch (myCard.healthPoints) {
            case 1: imageHP.setImageResource(R.drawable.one); break;
            case 2: imageHP.setImageResource(R.drawable.two); break;
            case 3: imageHP.setImageResource(R.drawable.three); break;
            case 4: imageHP.setImageResource(R.drawable.four); break;
            case 5: imageHP.setImageResource(R.drawable.five); break;
            case 6: imageHP.setImageResource(R.drawable.six); break;
            case 7: imageHP.setImageResource(R.drawable.seven); break;
            case 8: imageHP.setImageResource(R.drawable.eight); break;
            case 9: imageHP.setImageResource(R.drawable.nine); break;
            case 10: imageHP.setImageResource(R.drawable.ten); break;
        }
        imageHP.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageView imageDP = new ImageView(ll.getContext());
        imageDP.setId(View.generateViewId());
        imageDP.setLayoutParams(imageParams);
        switch (myCard.damagePoints) {
            case 1: imageDP.setImageResource(R.drawable.one); break;
            case 2: imageDP.setImageResource(R.drawable.two); break;
            case 3: imageDP.setImageResource(R.drawable.three); break;
            case 4: imageDP.setImageResource(R.drawable.four); break;
            case 5: imageDP.setImageResource(R.drawable.five); break;
            case 6: imageDP.setImageResource(R.drawable.six); break;
            case 7: imageDP.setImageResource(R.drawable.seven); break;
            case 8: imageDP.setImageResource(R.drawable.eight); break;
            case 9: imageDP.setImageResource(R.drawable.nine); break;
            case 10: imageDP.setImageResource(R.drawable.ten); break;
        }
        imageDP.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageView imageCOST = new ImageView(ll.getContext());
        imageCOST.setId(View.generateViewId());
        imageCOST.setLayoutParams(imageParams);
        switch (myCard.cost) {
            case 1: imageCOST.setImageResource(R.drawable.one); break;
            case 2: imageCOST.setImageResource(R.drawable.two); break;
            case 3: imageCOST.setImageResource(R.drawable.three); break;
            case 4: imageCOST.setImageResource(R.drawable.four); break;
            case 5: imageCOST.setImageResource(R.drawable.five); break;
            case 6: imageCOST.setImageResource(R.drawable.six); break;
            case 7: imageCOST.setImageResource(R.drawable.seven); break;
            case 8: imageCOST.setImageResource(R.drawable.eight); break;
            case 9: imageCOST.setImageResource(R.drawable.nine); break;
            case 10: imageCOST.setImageResource(R.drawable.ten); break;
        }
        imageCOST.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ll.addView(imageDP);
        ll.addView(imageHP);
        ll.addView(imageCOST);
        // размеры и отступы
        ConstraintSet set = new ConstraintSet();
        set.clone(ll);
        set.connect(imageHP.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        set.connect(imageHP.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        set.connect(imageDP.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        set.connect(imageDP.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
        set.connect(imageCOST.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        set.connect(imageCOST.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        set.applyTo(ll);
        // View HP && DP - конец

        card.setEnabled(true);
        card.setClickable(true);
        card.setId(table.getChildCount() + 1);
        card.addView(fl);
        fl.addView(cv);
        cv.addView(ll);

    }


    String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }

    @Override
    public void onBackPressed() {
        ExitDialog exitDialog = new ExitDialog();
        exitDialog.show(fragmentManager, "exit_dialog");
    }
}