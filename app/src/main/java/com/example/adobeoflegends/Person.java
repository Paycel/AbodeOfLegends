package com.example.adobeoflegends;

public abstract class Person {
    int healthPoints;
    int manaPoints;
    Card cards[];

    Person(int hp, int mp, Card cards[]){
        this.healthPoints = hp;
        this.manaPoints = mp;
        this.cards = cards;
    }
}