package com.example.adobeoflegends.activity;

import android.annotation.SuppressLint;
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

import com.example.adobeoflegends.objects.Card;
import com.example.adobeoflegends.R;
import com.example.adobeoflegends.dialogs.EndGameDialog;
import com.example.adobeoflegends.dialogs.ExitDialog;
import com.example.adobeoflegends.dialogs.Info;
import com.example.adobeoflegends.dialogs.ShowCardDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BattleActivity extends AppCompatActivity implements View.OnClickListener {
    @SuppressLint("StaticFieldLeak")
    public static com.example.adobeoflegends.objects.Battle battle;
    private static int points;
    private static int levelPoints;
    private LinearLayout enemyDeck;
    @SuppressLint("StaticFieldLeak")
    public static LinearLayout playerDeck;
    private LinearLayout enemyTable;
    private LinearLayout playerTable;
    @SuppressLint("StaticFieldLeak")
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
    public static int moveCount;
    private int helper;
    private int rndEnemy, rndPlayer;
    private int tmp1, tmp2;
    private int maxEnemyHP, maxEnemyMP, maxPlayerHP, maxPlayerMP;
    public static final String LOG_TAG_FIGHT = "Fight";
    public static final String LOG_TAG_SIZE = "Size";
    public static final String LOG_TAG_DELAY = "Delay";
    public static final String LOG_TAG_TEMP = "temp";
    private View.OnLongClickListener onLongClickListener;
    private int api;
    public static ArrayList<String> log;
    public static int difficulty;
    public static String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        // set flags to hide to support action bar with high panel (with notifications)
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
        setElementsAndParams(); // initialize params
        // set observer listener -- need to delete cards
        ViewTreeObserver observer = playerDeck.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                playerDeck.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                enemyDeck.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                findViewById(R.id.MAIN).getViewTreeObserver().removeGlobalOnLayoutListener(this);
                drawCard(enemyDeck, true);
                drawCard(playerDeck, true);
                updateSTATS();
            }
        });
        // long click -- show card
        onLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showCardInfo(v);
                return true;
            }
        };
    }

    // animation of tapped card
    private void imagePress(View v, boolean isImagePressed, int duration){
        Objects.requireNonNull(findCard(firstToSecond((LinearLayout) v).getId())).setTapped(!isImagePressed);
        if (!isImagePressed) v.animate().scaleX(0.7f).scaleY(0.7f).setDuration(duration);
        if (isImagePressed) v.animate().scaleX(1f).scaleY(1f).setDuration(duration);
    }

    public static int getLevelPoints() {
        return levelPoints;
    }

    // initialize params
    private void setElementsAndParams(){
        fragmentManager = getSupportFragmentManager();
        log = new ArrayList<>();
        log.add(getResources().getText(R.string.journal_start).toString());
        helper = 0;
        tmp1 = 0;
        levelPoints = 0;
        tmp2 = 0;
        currentUser = getIntent().getStringExtra("currentUser");
        points = getIntent().getIntExtra("points", 0);
        difficulty = getIntent().getIntExtra("difficulty", 0);
        if (difficulty == 0) difficulty = (int) (4 + Math.random() * 8);
        Log.d(LOG_TAG_FIGHT, "DIFF = " + difficulty);
        com.example.adobeoflegends.objects.Battle.setNumsOfCards(difficulty + 4);
        // names of cards (needed for language)
        String[] names = new String[]{
                getResources().getText(R.string.card_name_peasant).toString(),
                getResources().getText(R.string.card_name_archer).toString(),
                getResources().getText(R.string.card_name_swordsman).toString(),
                getResources().getText(R.string.card_name_vampire).toString(),
                getResources().getText(R.string.card_name_wizard).toString()
        };
        battle = new com.example.adobeoflegends.objects.Battle(getApplicationContext(), currentUser);
        List<Card> temp = new ArrayList<>(battle.getPlayer().getCardList());
        for (Card card: temp) card.setNames(names);
        battle.getPlayer().setCardList(temp);
        temp = new ArrayList<>(battle.getEnemy().getCardList());
        for (Card card: temp) card.setNames(names);
        battle.getEnemy().setCardList(temp);
        battle.setEnemyHP((int)(battle.getEnemyHP() * (1 + (float)difficulty / 10)));
        battle.setEnemyMP((int)(battle.getEnemyMP() * (1 + (float)difficulty / 10)));
        maxEnemyHP = battle.getEnemyHP();
        maxEnemyMP = battle.getEnemyMP();
        maxPlayerHP = battle.getPlayerHP();
        maxPlayerMP = battle.getPlayerMP();
        MAIN = (LinearLayout) findViewById(R.id.MAIN);
        enemyDeck = (LinearLayout) findViewById(R.id.enemy_deck);
        playerDeck = (LinearLayout) findViewById(R.id.player_deck);
        playerTable = (LinearLayout) findViewById(R.id.player_table);
        enemyTable = (LinearLayout) findViewById(R.id.enemy_table);
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

    public static int getPoints() {
        return points;
    }

    // flags to hide to support action bar with high panel (with notifications) (override method)
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

    // set cover to enemy cards
    private void setCover(LinearLayout card){
        firstToSecond(card).setForeground(getDrawable(R.drawable.cover_of_card));
    }

    private void hideForeground(LinearLayout card){
        firstToSecond(card).setForeground(null);
    }

    // set fogging to card
    private void setDarkOnCard(LinearLayout card){
        firstToSecond(card).setForeground(new ColorDrawable(getResources().getColor(R.color.dark)));
    }

    // Long Listener to show card at full screen
    private void showCardInfo(View v){
        ShowCardDialog dialog_show = new ShowCardDialog((ConstraintLayout) v);
        dialog_show.show(fragmentManager, "ShowCard_Dialog");
    }

    // get table of card (deck (2) or table*(2))
    public static LinearLayout getParentTable(LinearLayout card){
        return (LinearLayout) ((ViewGroup) card.getParent());
    }

    // get *hidden* layout
    private ConstraintLayout firstToSecond(LinearLayout first){
        FrameLayout frameLayout = (FrameLayout) first.getChildAt(0);
        CardView cardView = (CardView) frameLayout.getChildAt(0);
        return (ConstraintLayout) cardView.getChildAt(0);
    }

    // get upper layout
    public static LinearLayout secondToFirst(View v){
        ConstraintLayout second = (ConstraintLayout) v;
        CardView cardView = (CardView) ((ViewGroup) second.getParent());
        FrameLayout frameLayout = (FrameLayout) ((ViewGroup) cardView.getParent());
        return (LinearLayout) ((ViewGroup) frameLayout.getParent());
    }

    // is clicked button?
    private boolean isButton(View v){
        return v.getId() == buttonFACE.getId() || v.getId() == buttonOK.getId() || v.getId() == buttonMOVE.getId() || v.getId() == buttonINFO.getId();
    }

    // no comments
    private void activateButton(Button b){
        b.setClickable(true);
        b.setEnabled(true);
    }

    // no comments
    private void deactivateButton(Button b){
        b.setClickable(false);
        b.setEnabled(false);
    }

    // set stats for card after battle
    private void setCardStats(LinearLayout card){
        ConstraintLayout s_card = firstToSecond(card);
        ImageView HP = (ImageView) s_card.getChildAt(1);
        Card person = findCard(s_card.getId());
        assert person != null;
        switch (person.getHealthPoints()){
            case 1: HP.setImageResource(R.drawable.onehp); break;
            case 2: HP.setImageResource(R.drawable.twohp); break;
            case 3: HP.setImageResource(R.drawable.threehp); break;
            case 4: HP.setImageResource(R.drawable.fourhp); break;
            case 5: HP.setImageResource(R.drawable.fivehp); break;
            case 6: HP.setImageResource(R.drawable.sixhp); break;
            case 7: HP.setImageResource(R.drawable.sevenhp); break;
            case 8: HP.setImageResource(R.drawable.eighthp); break;
            case 9: HP.setImageResource(R.drawable.ninehp); break;
            case 10: HP.setImageResource(R.drawable.tenhp); break;
        }
    }

    // find card by id
    public static Card findCard(int id) {
        for (int j = 0; j < battle.getPlayer().getCardList().size(); j++) {
            if (battle.getPlayer().getCardList().get(j).getViewID() == id) {
                return battle.getPlayer().getCardList().get(j);
            }
        }
        for (int j = 0; j < battle.getEnemy().getCardList().size(); j++) {
            if (battle.getEnemy().getCardList().get(j).getViewID() == id) {
                return battle.getEnemy().getCardList().get(j);
            }
        }
        // never reached
        return null;
    }

    // is that end? -- checked after fight
    private int isEnd(){
        if (battle.getPlayerHP() <= 0 || (battle.getPlayer().getCardList().size() == 0)) return 1; // player lose
        else if (battle.getEnemyHP() <= 0 || (battle.getEnemy().getCardList().size() == 0)) {
            points += 10;
            levelPoints += 10;
            return 2; // player win!
        }
        return 0; // nothing happened ((
    }

    // mode == isEnd()
    private void endLevel(int mode){
        if (mode == 0) return;
        EndGameDialog dialog_end = new EndGameDialog(mode);
        dialog_end.show(fragmentManager, "End_Dialog");
    }

    // tapped Card = red (for enemy), only for visual convenience)
    private void setTappedCard(LinearLayout card){
        if (card == null) return;
        card.setForeground(getDrawable(R.drawable.pressed_card_red));
    }

    // set null foreground for card
    private void clearTappedCard(LinearLayout card){
        if (card != null)
        card.setForeground(null);
    }

    // fight between 2 cards
    private void fight(LinearLayout cardPlayer, LinearLayout cardEnemy){
        if (cardEnemy == null) return;
        int idPlayer = firstToSecond(cardPlayer).getId();
        int idEnemy = firstToSecond(cardEnemy).getId();
        Card player = findCard(firstToSecond(cardPlayer).getId());
        Card enemy = findCard(firstToSecond(cardEnemy).getId());
        assert enemy != null;
        int e_hp = enemy.getHealthPoints();
        assert player != null;
        int p_hp = player.getHealthPoints();
        // result of fight
        int result = battle.fight(Objects.requireNonNull(findCard(idPlayer)), Objects.requireNonNull(findCard(idEnemy)));
        if (result == 2){ // player card win
            points += 3;
            levelPoints += 3;
            battle.getEnemy().getCardList().remove(findCard(firstToSecond(cardEnemy).getId()));
            log.add(getResources().getText(R.string.your_card).toString() + " " + player.getName() + " (" + player.getDamagePoints() + ", " + p_hp + ")" + " " +
                    getResources().getText(R.string.killed).toString()+ " " + enemy.getName() + " (" + enemy.getDamagePoints() + ", " + e_hp +")");
            deleteCard(cardEnemy);
            setCardStats(cardPlayer);
        } else if (result == 1){ // enemy card win
            battle.getPlayer().getCardList().remove(findCard(firstToSecond(cardPlayer).getId()));
            BattleActivity.log.add(getResources().getText(R.string.your_card).toString()+ " " + player.getName() + " (" + player.getDamagePoints() + ", " + p_hp + ")" +
                    getResources().getText(R.string.dead_from).toString()+ " " + enemy.getName() + " (" + enemy.getDamagePoints() + ", " + e_hp + ")");
            deleteCard(cardPlayer);
            setCardStats(cardEnemy);
        } else if (result == 0){ // nothing happened, 2 cards alive
            points += 2;
            levelPoints += 2;
            BattleActivity.log.add(getResources().getText(R.string.your_card).toString()+ " " + player.getName() + " (" + player.getDamagePoints() + ", " + p_hp + ")" +
                    getResources().getText(R.string.deal_damage).toString()+ " " + player.getDamagePoints() + " " + getResources().getText(R.string.damage_log).toString()+ " " + enemy.getName() + " (" + enemy.getDamagePoints() + ", " + e_hp +
                    ")" + getResources().getText(R.string.n_lose).toString() + " " + (p_hp - player.getHealthPoints()) + " " + getResources().getText(R.string.health_).toString());
           setCardStats(cardEnemy);
           setCardStats(cardPlayer);
        } else { // 2 cards dead
            points += 1;
            levelPoints += 1;
            battle.getPlayer().getCardList().remove(findCard(firstToSecond(cardPlayer).getId()));
            battle.getEnemy().getCardList().remove(findCard(firstToSecond(cardEnemy).getId()));
            log.add(getResources().getText(R.string.your_card).toString() + " " + player.getName() + " (" + player.getDamagePoints() + ", " + p_hp + ")" +
                    getResources().getText(R.string.dead_from).toString() + " " + enemy.getName() + " (" + enemy.getDamagePoints() + ", " + e_hp + "), " +
                    getResources().getText(R.string.card_answer_dead).toString());
            deleteCard(cardEnemy);
            deleteCard(cardPlayer);
        }
        Log.d("LOGS", "Points (battle) = " + points);
        updateSTATS(); // update params of cards
    }

    // enemy move
    private void enemyMove() {
        final int num = (int) (Math.random() * enemyDeck.getChildCount());
        final boolean beatPlayer = Math.floor(Math.random() * 2) == 1 || playerTable.getChildCount() == 0; // true - beat player
        final boolean emptyDeck = enemyDeck.getChildCount() == 0;
        if (!emptyDeck && enemyTable.getChildCount() != 4) { // if enemy can throw card to table from deck
            LinearLayout temp = (LinearLayout) enemyDeck.getChildAt(num);
            firstToSecond(temp).setOnClickListener(this);
            firstToSecond(temp).setOnLongClickListener(onLongClickListener);
            moveOnTable(temp, enemyTable);
            setDarkOnCard(temp);
        }
        // handler with 1 sec delay
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean allNotActive = true;
                LinearLayout enemy = null;
                int delayMS = 1;
                rndEnemy = -1;
                // find active card
                for (int i = 0; i < enemyTable.getChildCount(); i++){
                    enemy = (LinearLayout) enemyTable.getChildAt(i);
                    Log.d(LOG_TAG_TEMP, "enemy #" + i + " is " + isActiveCard(enemy));
                    if (isActiveCard(enemy)){
                        allNotActive = false;
                        delayMS = 2000;
                        break;
                    }
                }
                // if active card found
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
                // beat player
                if (beatPlayer && !allNotActive){
                    setTappedCard(enemy); // set red card
                    final LinearLayout finalEnemy = enemy;
                    Log.d(LOG_TAG_FIGHT, "BEAT PLAYER!");
                    // handler 2 sec delay
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // update log
                            log.add(getResources ().getText(R.string.you).toString() + " (HP: " + battle.getPlayerHP() + ") " + getResources().getText(R.string.gets_).toString() + " " + Objects.requireNonNull(findCard(firstToSecond(finalEnemy).getId())).getDamagePoints() +
                                    " " + getResources().getText(R.string.damage_from_card).toString() + " " + Objects.requireNonNull(findCard(firstToSecond(finalEnemy).getId())).getName() +
                                    " (" + Objects.requireNonNull(findCard(firstToSecond(finalEnemy).getId())).getDamagePoints() + ", " +
                                    Objects.requireNonNull(findCard(firstToSecond(finalEnemy).getId())).getHealthPoints() + ")");
                            // update player HP
                            battle.setPlayerHP(battle.getPlayerHP() - Objects.requireNonNull(findCard(firstToSecond(finalEnemy).getId())).getDamagePoints());
                            updateSTATS();
                            // clear red card
                            clearTappedCard(finalEnemy);
                            endLevel(isEnd());
                        }
                    }, 2000);
                    return;
                }
                // beat card
                if (playerTable.getChildCount() == 0) return;
                if (!allNotActive){
                    rndPlayer = (int) (Math.random() * playerTable.getChildCount());
                    LinearLayout player = (LinearLayout) playerTable.getChildAt(rndPlayer);
                    setTappedCard(enemy);
                    setTappedCard(player);
                    final int finalDelayMS = delayMS;
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (playerTable.getChildCount() == 0) return;
                            fight((LinearLayout) playerTable.getChildAt(rndPlayer), (LinearLayout) enemyTable.getChildAt(rndEnemy));
                            clearTappedCard((LinearLayout) playerTable.getChildAt(rndPlayer));
                            clearTappedCard((LinearLayout) enemyTable.getChildAt(rndEnemy));
                            endLevel(isEnd());
                            Log.d(LOG_TAG_DELAY, "DELAY = " + finalDelayMS);
                        }
                    }, finalDelayMS);
                }
                for (int i = 0; i < enemyTable.getChildCount(); i++){
                    setActiveCard((LinearLayout) enemyTable.getChildAt(i));
                }

            }
        }, 1000);
    }

    // player move
    private void playerMove(final LinearLayout player, final LinearLayout enemy) {
        // бьёт выбранного игрока соперника
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (player != null && enemy != null)
                    fight(player, enemy);
                endLevel(isEnd());
            }
        });
    }

    // is card active
    private boolean isActiveCard(LinearLayout card){
        return Objects.requireNonNull(findCard(firstToSecond(card).getId())).isActive();
    }

    // is card tapped
    private boolean isTappedCard(LinearLayout card) {
        return Objects.requireNonNull(findCard(firstToSecond(card).getId())).isTapped();
    }

    @SuppressLint("SetTextI18n")
    // updating stats of player and enemy
    private void updateSTATS(){
        playerSTATS.setText(getResources().getText(R.string.player).toString() + "\n" + getResources().getText(R.string.hp) + battle.getPlayerHP() + " "
                + getResources().getText(R.string.mp) + battle.getPlayerMP() + "\n" + getResources().getText(R.string.cards) + (battle.getPlayer().getCardList().size() - playerTable.getChildCount() - playerDeck.getChildCount()));
        enemySTATS.setText(getResources().getText(R.string.enemy).toString() + "\n" + getResources().getText(R.string.hp) + battle.getEnemyHP() + "\n" +
                getResources().getText(R.string.cards) + (battle.getEnemy().getCardList().size() - enemyTable.getChildCount() - enemyDeck.getChildCount()));
    }

    private void setActiveCard(LinearLayout card){
        Objects.requireNonNull(findCard(firstToSecond(card).getId())).setActive(true);
    }

    private void setDeactiveCard(LinearLayout card){
        Objects.requireNonNull(findCard(firstToSecond(card).getId())).setActive(false);
    }

    // is enough mp to throw card to table
    private boolean EnoughMP(LinearLayout card){
        if (card == null) return false;
        if (Objects.requireNonNull(findCard(firstToSecond(card).getId())).getCost() > battle.getPlayerMP()){
            Toast.makeText(this, getResources().getText(R.string.no_mana).toString(), Toast.LENGTH_SHORT).show();
            imagePress(card, true, 200);
            return false;
        }
        return true;
    }

    // is place to throw card to table
    private boolean noPlace(){
        if (playerTable.getChildCount() == 4) {
            if (tappedCard != null) imagePress(tappedCard, true, 200);
            Toast.makeText(this, getResources().getText(R.string.no_space).toString(), Toast.LENGTH_SHORT).show();
        }
        return playerTable.getChildCount() == 4;
    }

    @Override
    // *global* onClick method, called when clicked button or cards
    public void onClick(View v) {
        LinearLayout card;
        if (!isButton(v)) { // if clicked button
            card = secondToFirst(v);
            if (helper == 0){ // helper == 0 only at first move
                activateButton(buttonOK);
                activateButton(buttonFACE);
                helper++;
            }
            switch (getParentTable(card).getId()) { // which table is clicked
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
                    if (!isActiveCard(card)) return; // pressing an inactive card is not processed
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
        } else { // clicked button
            switch (v.getId()) { // which button is clicked
                case R.id.btn_OK:
                    if (tappedCard == null || noPlace() || !EnoughMP(tappedCard)) return;
                    battle.setPlayerMP(battle.getPlayerMP() - Objects.requireNonNull(findCard(firstToSecond(tappedCard).getId())).getCost());
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
                        if (enemyCard == null){
                            log.add(getResources().getText(R.string.enemy).toString() + " (HP: " + battle.getEnemyHP() + ") " + getResources().getText(R.string.gets) + " " + Objects.requireNonNull(findCard(firstToSecond(playerCard).getId())).getDamagePoints() +
                                    " " + getResources().getText(R.string.damage_from_your_card).toString() + " " + Objects.requireNonNull(findCard(firstToSecond(playerCard).getId())).getName() +
                                    " (" + Objects.requireNonNull(findCard(firstToSecond(playerCard).getId())).getDamagePoints() + ", " +
                                    Objects.requireNonNull(findCard(firstToSecond(playerCard).getId())).getHealthPoints() + ")");
                            battle.setEnemyHP(battle.getEnemyHP() - Objects.requireNonNull(findCard(firstToSecond(playerCard).getId())).getDamagePoints());
                        } else {
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
                    deactivateButton(buttonFACE);
                    moveCount++;
                    BattleActivity.log.add( " " + getResources().getText(R.string.next_move).toString() + " " + ((int) (moveCount + 1) / 2) + ": ");
                    enemyMove();
                    moveCount++;
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            activateButton(buttonMOVE);
                            activateButton(buttonOK);
                            activateButton(buttonFACE);
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
                    Log.d(LOG_TAG_FIGHT, "SIZE_ENEMY = " + battle.getEnemy().getCardList().size());
                    if (battle.getPlayerMP() + 5 <= maxPlayerHP) battle.setPlayerMP(battle.getPlayerMP() + 5);
                    else battle.setPlayerMP(maxPlayerMP);
                    if (battle.getEnemyHP() + 2 * (1 + (float)difficulty / 10) <= maxEnemyHP)
                        battle.setEnemyHP((int)(battle.getEnemyHP() + 2 * (1 + (float)difficulty / 10)));
                    else battle.setEnemyHP(maxEnemyHP);
                    if (battle.getEnemyMP() + 3 * (1 + (float)difficulty / 10) <= maxEnemyMP)
                        battle.setEnemyMP((int)(battle.getEnemyMP() + 3 * (1 + (float)difficulty / 10)));
                    else battle.setEnemyMP(maxEnemyMP);
                    updateSTATS();
                    break;
            }
        }
    }

    // get index of card to add into deck
    private int get_add(boolean player) {
        List<Card> list;
        LinearLayout table1, table2;
        if (player) {
            list = battle.getPlayer().getCardList();
            table1 = playerTable;
            table2 = playerDeck;
        } else {
            list = battle.getEnemy().getCardList();
            table1 = enemyTable;
            table2 = enemyDeck;
        }
        int id = -1;
        for (int r = 0; r < list.size(); r++) {
            boolean temp_found = true;
            for (int i = 0; i < table1.getChildCount(); i++) {
                if (list.get(r).getViewID() == firstToSecond((LinearLayout) table1.getChildAt(i)).getId()) {
                    temp_found = false;
                    break;
                }
            }
            if (temp_found) {
                for (int i = 0; i < table2.getChildCount(); i++) {
                    if (list.get(r).getViewID() == firstToSecond((LinearLayout)table2.getChildAt(i)).getId()) {
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

    // add card to deck
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
            else drawCard(playerDeck, false);
        }
        else{
            if (enemyDeck.getChildCount() == 4 || tmp_enemy == -1) return;
            else drawCard(enemyDeck, false);
        }
        updateSTATS();
    }

    // draw card -- with create of activity
    private void drawCard(LinearLayout table, boolean start) {
        LinearLayout card;
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

    // card rendering
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
            battle.getPlayer().getCardList().get(i).setViewID(ll.getId());
        } else {
            battle.getEnemy().getCardList().get(i).setViewID(ll.getId());
        }
        ll.setLayoutParams(lParams);
        ll.setBackgroundResource(Objects.requireNonNull(findCard(ll.getId())).getPictureID());
        ll.setPadding(5, 0, 0, 0);
        ll.setClickable(true);
        ll.setEnabled(true);

        // click on card
        if (table == playerDeck) {
            ll.setOnClickListener(this);
            ll.setOnLongClickListener(onLongClickListener);
        }

        // View HP && DP - begin
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams((int)((float) lParams.height* 0.15), (int)((float) lParams.height* 0.15));

        ImageView imageHP = new ImageView(ll.getContext());
        imageHP.setId(View.generateViewId());
        imageHP.setLayoutParams(imageParams);
        Card myCard = findCard(ll.getId());
        assert myCard != null;
        switch (myCard.getHealthPoints()) {
            case 1: imageHP.setImageResource(R.drawable.onehp); break;
            case 2: imageHP.setImageResource(R.drawable.twohp); break;
            case 3: imageHP.setImageResource(R.drawable.threehp); break;
            case 4: imageHP.setImageResource(R.drawable.fourhp); break;
            case 5: imageHP.setImageResource(R.drawable.fivehp); break;
            case 6: imageHP.setImageResource(R.drawable.sixhp); break;
            case 7: imageHP.setImageResource(R.drawable.sevenhp); break;
            case 8: imageHP.setImageResource(R.drawable.eighthp); break;
            case 9: imageHP.setImageResource(R.drawable.ninehp); break;
            case 10: imageHP.setImageResource(R.drawable.tenhp); break;
        }
        imageHP.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageView imageDP = new ImageView(ll.getContext());
        imageDP.setId(View.generateViewId());
        imageDP.setLayoutParams(imageParams);
        switch (myCard.getDamagePoints()) {
            case 1: imageDP.setImageResource(R.drawable.onedp); break;
            case 2: imageDP.setImageResource(R.drawable.twodp); break;
            case 3: imageDP.setImageResource(R.drawable.threedp); break;
            case 4: imageDP.setImageResource(R.drawable.fourdp); break;
            case 5: imageDP.setImageResource(R.drawable.fivedp); break;
            case 6: imageDP.setImageResource(R.drawable.sixdp); break;
            case 7: imageDP.setImageResource(R.drawable.sevendp); break;
            case 8: imageDP.setImageResource(R.drawable.eightdp); break;
            case 9: imageDP.setImageResource(R.drawable.ninedp); break;
            case 10: imageDP.setImageResource(R.drawable.tendp); break;
        }
        imageDP.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageView imageCOST = new ImageView(ll.getContext());
        imageCOST.setId(View.generateViewId());
        imageCOST.setLayoutParams(imageParams);
        switch (myCard.getCost()) {
            case 1: imageCOST.setImageResource(R.drawable.onemp); break;
            case 2: imageCOST.setImageResource(R.drawable.twomp); break;
            case 3: imageCOST.setImageResource(R.drawable.threemp); break;
            case 4: imageCOST.setImageResource(R.drawable.fourmp); break;
            case 5: imageCOST.setImageResource(R.drawable.fivemp); break;
            case 6: imageCOST.setImageResource(R.drawable.sixmp); break;
            case 7: imageCOST.setImageResource(R.drawable.sevenmp); break;
            case 8: imageCOST.setImageResource(R.drawable.eightmp); break;
            case 9: imageCOST.setImageResource(R.drawable.ninemp); break;
            case 10: imageCOST.setImageResource(R.drawable.tenmp); break;
        }
        imageCOST.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ll.addView(imageDP);
        ll.addView(imageHP);
        ll.addView(imageCOST);
        // margins
        ConstraintSet set = new ConstraintSet();
        set.clone(ll);
        int m = (int) ((float) lParams.height * 0.13);
        int c = (int) ((float) lParams.height * 0.1);
        set.connect(imageHP.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, m);
        set.connect(imageHP.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        set.connect(imageDP.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, m);
        set.connect(imageDP.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
        set.connect(imageCOST.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, c);
        set.connect(imageCOST.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        set.applyTo(ll);
        // View HP && DP - end
        card.setEnabled(true);
        card.setClickable(true);
        card.setId(table.getChildCount() + 1);
        card.addView(fl);
        fl.addView(cv);
        cv.addView(ll);
    }

    // emoji by unicode
    private String getEmoji(int unicode){
        return new String(Character.toChars(unicode));
    }

    @Override
    // when licked Back button - creating dialog with the confirmation
    public void onBackPressed() {
        ExitDialog exitDialog = new ExitDialog();
        Log.d("LOGS", "Points (toDialog) = " + points);
        exitDialog.show(fragmentManager, "exit_dialog");
    }
}