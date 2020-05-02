package com.example.adobeoflegends.objects;

import com.example.adobeoflegends.R;

public class Card {
    private int healthPoints;
    private int damagePoints;
    private String name;
    private int pictureID;
    private int viewID;
    private boolean active;
    private boolean isTapped;
    private int cost;

    public Card(){
        int hp =  (int)(Math.random()*10 + 1);
        int dp =  (int)(Math.random()*10 + 1);
        int cost = (int)((Math.random()*10 + 1));
        String names[] = {
                "Дракон",
                "Крестьянин",
                "Вампир",
                "Волшебник"};
        int num = (int) (Math.random()*names.length);
        this.healthPoints = hp;
        this.damagePoints = dp;
        this.name = names[num];
        this.cost = cost;
        switch (num){
            case 0: this.pictureID = R.drawable.dragon; break;
            case 1: this.pictureID = R.drawable.peasant; break;
            case 2: this.pictureID = R.drawable.vampire; break;
            case 3: this.pictureID = R.drawable.wizard; break;
        }
        active = false;
        isTapped = false;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public int getDamagePoints() {
        return damagePoints;
    }

    public void setDamagePoints(int damagePoints) {
        this.damagePoints = damagePoints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPictureID() {
        return pictureID;
    }

    public void setPictureID(int pictureID) {
        this.pictureID = pictureID;
    }

    public int getViewID() {
        return viewID;
    }

    public void setViewID(int viewID) {
        this.viewID = viewID;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isTapped() {
        return isTapped;
    }

    public void setTapped(boolean tapped) {
        isTapped = tapped;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
