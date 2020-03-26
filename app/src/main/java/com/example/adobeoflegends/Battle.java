package com.example.adobeoflegends;

import java.util.ArrayList;
import java.util.List;

public class Battle{
    Player player;
    Enemy enemy;
    public static final int numsOfCards = 4;

    Battle(){
        List<Card> cardList = new ArrayList<>();
        for (int i = 0; i < numsOfCards; i++)
            cardList.add(new Card());
        player = new Player(20, 30, cardList);
        cardList = new ArrayList<>();
        for (int i = 0; i < numsOfCards; i++)
            cardList.add(new Card());
        enemy = new Enemy(30, 20, cardList);
    }

    int fight(Card player, Card enemy, int mode) {
        if (mode == 1) {    // первый бьёт игрок
            enemy.healthPoints -= player.damagePoints;
            if (enemy.healthPoints <= 0)
                return 0;               // победа игрока
            else {
                player.healthPoints -= enemy.damagePoints;
                if (player.healthPoints <= 0){
                    return 1;
                }
            }
        } else {
            player.healthPoints -= enemy.damagePoints;
            if (player.healthPoints <= 0)
                return 1;               // победа врага
            else {
                enemy.healthPoints -= player.damagePoints;
                if (enemy.healthPoints <= 0){
                    return 0;
                }
            }
        }
        return 2;
    }


}
