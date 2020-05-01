package com.example.adobeoflegends;

import java.util.ArrayList;
import java.util.List;

public abstract class Person {
    private int healthPoints;
    private int manaPoints;
    private List<Card> cardList;

    Person(int hp, int mp, List<Card> cardList){
        this.healthPoints = hp;
        this.manaPoints = mp;
        this.cardList = cardList;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public int getManaPoints() {
        return manaPoints;
    }

    public void setManaPoints(int manaPoints) {
        this.manaPoints = manaPoints;
    }

    public List<Card> getCardList() {
        return cardList;
    }

    public void setCardList(List<Card> cardList) {
        this.cardList = cardList;
    }
}
