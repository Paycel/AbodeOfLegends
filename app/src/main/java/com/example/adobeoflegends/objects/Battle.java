package com.example.adobeoflegends.objects;

import android.content.Context;

import com.example.adobeoflegends.activity.Menu;
import com.example.adobeoflegends.database.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class Battle{
    private Player player;
    private Enemy enemy;
    private static int numsOfCards;
    private Context context;

    public Battle(Context context, String currentUser){
        DBHelper dbHelper = new DBHelper(context);
        this.context = context;
        List<Card> cardList = new ArrayList<>();
        Player player = dbHelper.getPlayer(Menu.dataBase, currentUser);
        List<Card> great = player.getDeck();
        for (int i = 0; i < numsOfCards; i++) {
            Card card = new Card(context);
            for (Card temp : great) if (card.getName().equals(temp.getName())) {
                    card.setDamagePoints(temp.getDamagePoints());
                    card.setHealthPoints(temp.getHealthPoints());
                    cardList.add(card);
                    break;
                }
        }
        player.setCardList(cardList);
        this.player = new Player(player.getHealthPoints(), player.getManaPoints(), player.getCardList());
        cardList = new ArrayList<>();
        for (int i = 0; i < numsOfCards; i++)
            cardList.add(new Card(context));
        enemy = new Enemy(30, 30, cardList);
    }

    public int fight(Card player, Card enemy) {
        int e_hp = enemy.getHealthPoints();
        int p_hp = player.getHealthPoints();
        enemy.setHealthPoints(enemy.getHealthPoints() - player.getDamagePoints());
        player.setHealthPoints(player.getHealthPoints() - enemy.getDamagePoints());
        if (enemy.getHealthPoints() <= 0 && player.getHealthPoints() <= 0){
            return 3;
        } else if (enemy.getHealthPoints() <= 0 && player.getHealthPoints() > 0){
            return 2;
        } else if (enemy.getHealthPoints() > 0 && player.getHealthPoints() <= 0){
            return 1;
        } else {
            return 0;
        }
    }

    public void setEnemyHP(int hp){
        this.enemy.setHealthPoints(hp);
    }

    public void setPlayerHP(int hp){
        this.player.setHealthPoints(hp);
    }

    public void setEnemyMP(int mp){
        this.enemy.setManaPoints(mp);
    }

    public void setPlayerMP(int mp){
        this.player.setManaPoints(mp);
    }

    public int getEnemyHP(){
        return this.enemy.getHealthPoints();
    }

    public int getPlayerHP(){
        return this.player.getHealthPoints();
    }

    public int getEnemyMP(){
        return this.enemy.getManaPoints();
    }

    public int getPlayerMP(){
        return this.player.getManaPoints();
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
    }

    public static int getNumsOfCards() {
        return numsOfCards;
    }

    public static void setNumsOfCards(int numsOfCards) {
        com.example.adobeoflegends.objects.Battle.numsOfCards = numsOfCards;
    }
}
