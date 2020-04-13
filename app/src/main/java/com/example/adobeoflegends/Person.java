package com.example.adobeoflegends;

import java.util.ArrayList;
import java.util.List;

public abstract class Person {
    public int healthPoints;
    public int manaPoints;
    public List<Card> cardList;

    Person(int hp, int mp, List<Card> cardList){
        this.healthPoints = hp;
        this.manaPoints = mp;
        this.cardList = cardList;
    }

}
