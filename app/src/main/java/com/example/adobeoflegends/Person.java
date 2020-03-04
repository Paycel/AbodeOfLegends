package com.example.adobeoflegends;

import java.util.ArrayList;
import java.util.List;

public abstract class Person {
    int healthPoints;
    int manaPoints;
    List<Card> cardList;

    Person(int hp, int mp, List<Card> cardList){
        this.healthPoints = hp;
        this.manaPoints = mp;
        this.cardList = cardList;
    }

}
