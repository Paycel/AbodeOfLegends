package com.example.adobeoflegends;

public class Battle{
    Player player;
    Enemy enemy;

    Battle(){
        Card[] temp = new Card[3];
        for (int i = 0; i < 3; i++)
            temp[i] = new Card();
        player = new Player(20, 30, temp);
        Card[] temp1 = new Card[3];
        for (int i = 0; i < 3; i++)
            temp1[i] = new Card();
        enemy = new Enemy(30, 20, temp1);
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
