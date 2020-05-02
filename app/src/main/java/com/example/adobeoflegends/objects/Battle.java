package com.example.adobeoflegends.objects;

import java.util.ArrayList;
import java.util.List;

public class Battle{
    private Player player;
    private Enemy enemy;
    private static int numsOfCards;

    public Battle(){
        List<Card> cardList = new ArrayList<>();
        for (int i = 0; i < numsOfCards; i++)
            cardList.add(new Card());
        player = new Player(20, 20, cardList);
        cardList = new ArrayList<>();
        for (int i = 0; i < numsOfCards; i++)
            cardList.add(new Card());
        enemy = new Enemy(30, 30, cardList);
    }

    public int fight(Card player, Card enemy) {
        int e_hp = enemy.getHealthPoints();
        int p_hp = player.getHealthPoints();
        enemy.setHealthPoints(enemy.getHealthPoints() - player.getDamagePoints());
        player.setHealthPoints(player.getHealthPoints() - enemy.getDamagePoints());
        if (enemy.getHealthPoints() <= 0 && player.getHealthPoints() <= 0){
            com.example.adobeoflegends.activity.Battle.log.add("Ваша карта " + player.getName() + "(" + player.getDamagePoints() + ", " + p_hp + ")" +
                    " погибла от " + enemy.getName() + " (" + enemy.getDamagePoints() + ", " + e_hp + "), но в ответ карта противника погибла!");
            return 3;
        } else if (enemy.getHealthPoints() <= 0 && player.getHealthPoints() > 0){
            com.example.adobeoflegends.activity.Battle.log.add("Ваша карта " + player.getName() + "(" + player.getDamagePoints() + ", " + p_hp + ")" +
                    " победила " + enemy.getName() + " (" + enemy.getDamagePoints() + ", " + e_hp +")");
            return 2;
        } else if (enemy.getHealthPoints() > 0 && player.getHealthPoints() <= 0){
            com.example.adobeoflegends.activity.Battle.log.add("Ваша карта " + player.getName() + "(" + player.getDamagePoints() + ", " + p_hp + ")" +
                    " погибла от " + enemy.getName() + " (" + enemy.getDamagePoints() + ", " + e_hp + ")");
            return 1;
        } else {
            com.example.adobeoflegends.activity.Battle.log.add("Ваша карта " + player.getName() + "(" + player.getDamagePoints() + ", " + p_hp + ")" +
                    " наносит " + player.getDamagePoints() + " урона " + enemy.getName() + " (" + enemy.getDamagePoints() + ", " + e_hp +
                    ") и теряет " + (p_hp - player.getHealthPoints()) + " здоровья");
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
