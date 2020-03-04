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

    /*
    void showInfo(){
        System.out.printf("Player:\nHP = %d, MP = %d\n",player.healthPoints, player.manaPoints);
        for (int i = 0; i < 3; i++)
            System.out.printf("Card %d: HP = %d, DP = %d, Name = %s\n",
                    i, player.cards[i].healthPoints, player.cards[i].damagePoints, player.cards[i].name);

        System.out.printf("Enemy:\nHP = %d, MP = %d\n",enemy.healthPoints, enemy.manaPoints);
        for (int i = 0; i < 3; i++)
            System.out.printf("Card %d: HP = %d, DP = %d, Name = %s\n",
                    i, enemy.cards[i].healthPoints, enemy.cards[i].damagePoints, enemy.cards[i].name);
    }
    */ // не открывать


}
