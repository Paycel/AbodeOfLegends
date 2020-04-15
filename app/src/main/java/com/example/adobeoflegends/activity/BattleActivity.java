package com.example.adobeoflegends.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentManager;

import com.example.adobeoflegends.Battle;
import com.example.adobeoflegends.Card;
import com.example.adobeoflegends.R;
import com.example.adobeoflegends.dialogs.EndGameDialog;
import com.example.adobeoflegends.dialogs.ExitDialog;
import com.example.adobeoflegends.dialogs.Info;
import com.example.adobeoflegends.dialogs.ShowCardDialog;

import java.util.ArrayList;
import java.util.List;

public class BattleActivity extends AppCompatActivity implements View.OnClickListener {
    public static Battle battle;
    private LinearLayout enemyDeck;
    public static LinearLayout playerDeck;
    private LinearLayout enemyTable;
    private LinearLayout playerTable;
    private LinearLayout INFO;
    public static LinearLayout MAIN;
    private Button buttonMOVE;
    private Button buttonOK;
    private Button buttonFACE;
    private Button buttonINFO;
    private TextView playerSTATS;
    private TextView enemySTATS;
    private LinearLayout tappedCard;
    private LinearLayout enemyCard;
    private LinearLayout playerCard;
    public static FragmentManager fragmentManager;
    private EndGameDialog dialog_end;
    private ShowCardDialog dialog_show;
    public static int moveCount;
    private int helper;
    private int rndEnemy, rndPlayer;
    private int tmp1, tmp2;
    public static final String LOG_TAG_FIGHT = "Fight";
    public static final String LOG_TAG_SIZE = "Size";
    public static final String LOG_TAG_DELAY = "Delay";
    private View.OnLongClickListener onLongClickListener;
    private int api;
    public static ArrayList<String> log;
    public static int difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        api = android.os.Build.VERSION.SDK_INT;
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if(api >= Build.VERSION_CODES.KITKAT) {
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

    private void imagePress(View v, boolean isImagePressed, int duration){
        findCard(firstToSecond((LinearLayout) v).getId()).isTapped = !isImagePressed;
        if (!isImagePressed) v.animate().scaleX(0.7f).scaleY(0.7f).setDuration(duration);
        if (isImagePressed) v.animate().scaleX(1f).scaleY(1f).setDuration(duration);
    }

    private void setElementsAndParams(){
        fragmentManager = getSupportFragmentManager();
        log = new ArrayList<>();
        log.add("*Это ваш журнал боя*");
        helper = 0;
        tmp1 = 0;
        tmp2 = 0;
        difficulty = getIntent().getIntExtra("difficulty", 0);
        if (difficulty == 0) difficulty = (int) (4 + Math.random() * 8);
        Log.d(LOG_TAG_FIGHT, "DIFF = " + difficulty);
        Battle.numsOfCards = difficulty + 4;
        battle = new Battle();
        battle.enemy.healthPoints *= (1 + (float)difficulty / 10);
        battle.enemy.manaPoints *= (1 + (float)difficulty / 10);
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
        playerSTATS.setTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.pixel));
        enemySTATS.setTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.pixel));
        playerSTATS.setGravity(Gravity.CENTER);
        enemySTATS.setGravity(Gravity.CENTER);
        buttonMOVE.setTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.pixel));
        buttonOK.setText(getEmoji(Integer.parseInt(getResources().getText(R.string.OK).toString(), 16)));
        buttonFACE.setText(getEmoji(Integer.parseInt(getResources().getText(R.string.sword).toString(), 16)));
        buttonINFO.setText(getEmoji(Integer.parseInt(getResources().getText(R.string.INFO).toString(), 16)));
        buttonFACE.setOnClickListener(this);
        buttonOK.setOnClickListener(this);
        buttonMOVE.setOnClickListener(this);
        buttonINFO.setOnClickListener(this);
        moveCount = 0;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(api >= Build.VERSION_CODES.KITKAT && hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                              View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    private void setCover(LinearLayout card){
        firstToSecond(card).setForeground(getDrawable(R.drawable.cover_of_card));
    }

    private void hideForeground(LinearLayout card){
        firstToSecond(card).setForeground(null);
    }

    //затемнение
    private void setDarkOnCard(LinearLayout card){
        firstToSecond(card).setForeground(new ColorDrawable(getResources().getColor(R.color.dark)));
    }

    // Long Listener
    private void showCardInfo(View v){
        dialog_show = new ShowCardDialog((ConstraintLayout) v);
        dialog_show.show(fragmentManager, "ShowCard_Dialog");

    }

    // LINEAR
    public static LinearLayout getParentTable(LinearLayout card){
        return (LinearLayout) ((ViewGroup) card.getParent());
    }

    private ConstraintLayout firstToSecond(LinearLayout first){
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

    private boolean isButton(View v){
        return v.getId() == buttonFACE.getId() || v.getId() == buttonOK.getId() || v.getId() == buttonMOVE.getId() || v.getId() == buttonINFO.getId();
    }

    private void activateButton(Button b){
        b.setClickable(true);
        b.setEnabled(true);
    }

    private void deactivateButton(Button b){
        b.setClickable(false);
        b.setEnabled(false);
    }

    private void setCardStats(LinearLayout card){
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
        return null;
    }

    private int isEnd(){
        if (battle.player.healthPoints <= 0 || (battle.player.cardList.size() == 0)) return 1; // проиграл игрок
        else if (battle.enemy.healthPoints <= 0 || (battle.enemy.cardList.size() == 0)) return 2;
        return 0;
    }

    private void endLevel(int mode){
        if (mode == 0) return;
        dialog_end = new EndGameDialog(mode);
        dialog_end.show(fragmentManager, "End_Dialog");
    }

    private void setTappedCard(LinearLayout card, boolean red){
        if (card == null) return;
        if (red)
            card.setForeground(getDrawable(R.drawable.pressed_card_red));
        else
            card.setForeground(getDrawable(R.drawable.pressed_card_green));
    }

    private void clearTappedCard(LinearLayout card){
        if (card != null)
        card.setForeground(null);
    }

    private void fight(LinearLayout cardPlayer, LinearLayout cardEnemy, int mode){
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
        } else{
           setCardStats(cardEnemy);
           setCardStats(cardPlayer);
        }
        updateSTATS();
    }

    private void enemyMove() {
        final int num = (int) (Math.random() * enemyDeck.getChildCount());
        final boolean beatPlayer = Math.floor(Math.random() * 2) == 1 || playerTable.getChildCount() == 0; // true - бить игрока
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
                            log.add("Вы (HP: " + battle.player.healthPoints + ") получаете " + findCard(firstToSecond(finalEnemy).getId()).damagePoints +
                                    " урона от карты " + findCard(firstToSecond(finalEnemy).getId()).name +
                                    " (" + findCard(firstToSecond(finalEnemy).getId()).damagePoints + ", " +
                                    findCard(firstToSecond(finalEnemy).getId()).healthPoints + ")");
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

    private void playerMove(final LinearLayout player, final LinearLayout enemy) {
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

    private boolean isActiveCard(LinearLayout card){
        return findCard(firstToSecond(card).getId()).active;
    }

    private boolean isTappedCard(LinearLayout card) {
        return findCard(firstToSecond(card).getId()).isTapped;
    }

    private void updateSTATS(){
        playerSTATS.setText(getResources().getText(R.string.player).toString() + "\n" + getResources().getText(R.string.hp) + battle.player.healthPoints + " "
                + getResources().getText(R.string.mp) + battle.player.manaPoints + "\n" + getResources().getText(R.string.cards) + (battle.player.cardList.size() - playerTable.getChildCount() - playerDeck.getChildCount()));
        enemySTATS.setText(getResources().getText(R.string.enemy).toString() + "\n" + getResources().getText(R.string.hp) + battle.enemy.healthPoints + "\n" +
                getResources().getText(R.string.cards) + (battle.enemy.cardList.size() - enemyTable.getChildCount() - enemyDeck.getChildCount()));
    }

    private void setActiveCard(LinearLayout card){
        findCard(firstToSecond(card).getId()).active = true;
    }

    private void setDeactiveCard(LinearLayout card){
        findCard(firstToSecond(card).getId()).active = false;
    }

    private boolean EnoughMP(LinearLayout card){
        if (card == null) return false;
        if (findCard(firstToSecond(card).getId()).cost > battle.player.manaPoints){
            Toast.makeText(this, "Недостаточно маны!", Toast.LENGTH_SHORT).show();
            imagePress(card, true, 200);
            return false;
        }
        return true;
    }

    private boolean noPlace(){
        if (playerTable.getChildCount() == 4) {
            if (tappedCard != null) imagePress(tappedCard, true, 200);
            Toast.makeText(this, getResources().getText(R.string.no_space).toString(), Toast.LENGTH_SHORT).show();
        }
        return playerTable.getChildCount() == 4;
    }

    @Override
    public void onClick(View v) {
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
                            log.add("Противник (HP: " + battle.enemy.healthPoints + ") получает " + findCard(firstToSecond(playerCard).getId()).damagePoints +
                                    " урона от вашей карты " + findCard(firstToSecond(playerCard).getId()).name +
                                    " (" + findCard(firstToSecond(playerCard).getId()).damagePoints + ", " +
                                    findCard(firstToSecond(playerCard).getId()).healthPoints + ")");
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
                case R.id.btn_INFO:
                    Info info = new Info();
                    info.show(fragmentManager, "info_dialog");
                    break;
                case R.id.btn_move:
                    for (int i = 0; i < playerTable.getChildCount(); i++)
                        if (!isActiveCard((LinearLayout) playerTable.getChildAt(i)))
                            setActiveCard((LinearLayout) playerTable.getChildAt(i));
                    deactivateButton(buttonMOVE);
                    deactivateButton(buttonOK);
                    moveCount++;
                    BattleActivity.log.add( "Ход " + ((int) (moveCount + 1) / 2) + ": ");
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
                    battle.player.manaPoints += 5;
                    battle.enemy.healthPoints += 4 * (1 + difficulty / 10);
                    battle.enemy.manaPoints += 3 * (1 + difficulty / 10);
                    updateSTATS();
                    break;
            }
        }
    }

    private int get_add(boolean player) {
        List<Card> list;
        LinearLayout table1, table2;
        if (player) {
            list = battle.player.cardList;
            table1 = playerTable;
            table2 = playerDeck;
        } else {
            list = battle.enemy.cardList;
            table1 = enemyTable;
            table2 = enemyDeck;
        }
        int id = -1;
        for (int r = 0; r < list.size(); r++) {
            boolean temp_found = true;
            for (int i = 0; i < table1.getChildCount(); i++) {
                if (list.get(r).viewID == firstToSecond((LinearLayout) table1.getChildAt(i)).getId()) {
                    temp_found = false;
                    break;
                }
            }
            if (temp_found) {
                for (int i = 0; i < table2.getChildCount(); i++) {
                    if (list.get(r).viewID == firstToSecond((LinearLayout)table2.getChildAt(i)).getId()) {
                        temp_found = false;
                        break;
                    }
                }
            }
            if (temp_found) {
                id = r;
                break;
            }
        }
        return id;
    }

    private void addCard(boolean player){
        int tmp_player = get_add(true);
        int tmp_enemy = get_add(false);
        tmp1 = tmp_player;
        tmp2 = tmp_enemy;
        Log.d(LOG_TAG_FIGHT, "player_id = " + tmp1);
        Log.d(LOG_TAG_FIGHT, "enemy_id = " + tmp2);
        if (player){
            if (playerDeck.getChildCount() == 4){
                Toast.makeText(this, getResources().getText(R.string.no_space).toString(), Toast.LENGTH_SHORT).show();
                return;
            } else if (tmp_player == -1){
                Toast.makeText(this, getResources().getText(R.string.no_cards).toString(), Toast.LENGTH_SHORT).show();
                return;
            }
            else drawCard(null, playerDeck, false);
        }
        else{
            if (enemyDeck.getChildCount() == 4 || tmp_enemy == -1){
                return;
            }
            else{
                drawCard(null, enemyDeck, false);
            }
        }
        updateSTATS();
    }

    private void drawCard(LinearLayout card, LinearLayout table, boolean start) {
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
            int i;
            if (table == playerDeck) i = tmp1;
            else i = tmp2;
            setParams(card, table, i);
            if (table == enemyDeck){
                setCover(card);
            }
            table.addView(card);
        }
    }

    private void deleteCard(LinearLayout card){
        if (card == null) return;
        LinearLayout table = (LinearLayout) ((ViewGroup) card.getParent());
        table.removeView(card);
    }

    private void moveOnTable(LinearLayout card, LinearLayout table){
        if (card == null || table == null) return;
        deleteCard(card);
        hideForeground(card);
        table.addView(card);
    }

    private void setParams(LinearLayout card, LinearLayout table, int i) {
        ConstraintLayout.LayoutParams lParams = new ConstraintLayout.LayoutParams(table.getWidth() / 4 - 15, table.getHeight() - 5);
        LinearLayout.LayoutParams l_allWrap_Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        l_allWrap_Params.setMargins(0, 0, 0, 0);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(table.getWidth() / 4 - 15, table.getHeight() - 5);
        cardParams.setMargins(0, 10, 10, 10);
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


    private String getEmoji(int unicode){
        return new String(Character.toChars(unicode));
    }

    @Override
    public void onBackPressed() {
        ExitDialog exitDialog = new ExitDialog();
        exitDialog.show(fragmentManager, "exit_dialog");
    }
}