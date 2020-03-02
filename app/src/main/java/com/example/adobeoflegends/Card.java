package com.example.adobeoflegends;

public class Card {
    int healthPoints;
    int damagePoints;
    String name;

    Card(){
        int hp =  (int)(Math.random()*10 + 1);
        int dp =  (int)(Math.random()*10 + 1);
        String names[] = {"Дракон", "Крестьянин", "Вампир", "Волшебник"};
        this.healthPoints = hp;
        this.damagePoints = dp;
        this.name = names[(int) (Math.random()*3)];
    }
}
