package com.example.adobeoflegends;

import com.example.adobeoflegends.activity.BattleActivity;

import java.util.ArrayList;
import java.util.List;

public class Battle{
    public Player player;
    public Enemy enemy;
    public static int numsOfCards;

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
        int e_hp = enemy.healthPoints;
        int p_hp = player.healthPoints;
        enemy.healthPoints -= player.damagePoints;
        player.healthPoints -= enemy.damagePoints;
        if (enemy.healthPoints <= 0 && player.healthPoints <= 0){
            BattleActivity.log.add("Ваша карта " + player.name + "(" + player.damagePoints + ", " + p_hp + ")" +
                    " погибла от " + enemy.name + " (" + enemy.damagePoints + ", " + e_hp + "), но в ответ карта противника погибла!");
            return 3;
        } else if (enemy.healthPoints <= 0 && player.healthPoints > 0){
            BattleActivity.log.add("Ваша карта " + player.name + "(" + player.damagePoints + ", " + p_hp + ")" +
                    " победила " + enemy.name + " (" + enemy.damagePoints + ", " + e_hp +")");
            return 2;
        } else if (enemy.healthPoints > 0 && player.healthPoints <= 0){
            BattleActivity.log.add("Ваша карта " + player.name + "(" + player.damagePoints + ", " + p_hp + ")" +
                    " погибла от " + enemy.name + " (" + enemy.damagePoints + ", " + e_hp + ")");
            return 1;
        } else {
            BattleActivity.log.add("Ваша карта " + player.name + "(" + player.damagePoints + ", " + p_hp + ")" +
                    " наносит " + player.damagePoints + " урона " + enemy.name + " (" + enemy.damagePoints + ", " + e_hp +
                    ") и теряет " + (p_hp - player.healthPoints) + " здоровья");
            return 0;
        }
    }


}
