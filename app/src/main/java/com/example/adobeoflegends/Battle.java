package com.example.adobeoflegends;

import com.example.adobeoflegends.activity.BattleActivity;

import java.util.ArrayList;
import java.util.List;

public class Battle{
    public Player player;
    public Enemy enemy;
    public static final int numsOfCards = 7;

    public Battle(){
        List<Card> cardList = new ArrayList<>();
        for (int i = 0; i < numsOfCards; i++)
            cardList.add(new Card());
        player = new Player(20, 20, cardList);
        cardList = new ArrayList<>();
        for (int i = 0; i < numsOfCards; i++)
            cardList.add(new Card());
        enemy = new Enemy(30, 20, cardList);
    }

    public int fight(Card player, Card enemy, int mode) {
        if (mode == 1) {    // первый бьёт игрок
            BattleActivity.log.add("Ваша карта " + player.name + "(" + player.damagePoints + ", " + player.healthPoints + ")" +
                    " наносит " + player.damagePoints + " урона " + enemy.name + " (" + enemy.damagePoints + ", " + enemy.healthPoints +
                    ")");
            enemy.healthPoints -= player.damagePoints;
            if (enemy.healthPoints <= 0) {
                BattleActivity.log.add("Ваша карта " + player.name + "(" + player.damagePoints + ", " + player.healthPoints + ")" +
                        " победила " + enemy.name + " (" + enemy.damagePoints + ", 0)");
                return 0;               // победа игрока
            }
            else {
                BattleActivity.log.add("Карта противника " + enemy.name + "(" + enemy.damagePoints + ", " + enemy.healthPoints + ")" +
                        " наносит контратаку " + player.name + " (" + player.damagePoints + ", " + player.healthPoints + ")");
                player.healthPoints -= enemy.damagePoints;
                if (player.healthPoints <= 0){
                    BattleActivity.log.add("Ваша карта " + player.name + "(" + player.damagePoints + ", 0)" +
                            " погибла от " + enemy.name + " (" + enemy.damagePoints + ", " + enemy.healthPoints + ")");
                    return 1;
                }
            }
        } else {
            BattleActivity.log.add("Вашей карте " + player.name + "(" + player.damagePoints + ", " + player.healthPoints + ")" +
                " наносится урон " + enemy.damagePoints + " от " + enemy.name + " (" + enemy.damagePoints + ", " + enemy.healthPoints +
                ")");
            player.healthPoints -= enemy.damagePoints;
            if (player.healthPoints <= 0) {
                BattleActivity.log.add("Ваша карта " + player.name + "(" + player.damagePoints + ", 0)" +
                        " погибла от " + enemy.name + " (" + enemy.damagePoints + ", " + enemy.healthPoints + ")");
                return 1;               // победа врага
            }
            else {
                BattleActivity.log.add("Ваша карта " + player.name + "(" + player.damagePoints + ", " + player.healthPoints + ")" +
                        " наносит контратаку " + enemy.name + " (" + enemy.damagePoints + ", " + enemy.healthPoints + ")");
                enemy.healthPoints -= player.damagePoints;
                if (enemy.healthPoints <= 0){
                    BattleActivity.log.add("Ваша карта " + player.name + "(" + player.damagePoints + ", " + player.healthPoints + ")" +
                            " победила " + enemy.name + " (" + enemy.damagePoints + ", 0)");
                    return 0;
                }
            }
        }
        return 2;
    }


}
