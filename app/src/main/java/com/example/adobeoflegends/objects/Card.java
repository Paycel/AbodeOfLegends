package com.example.adobeoflegends.objects;

import android.content.Context;

import com.example.adobeoflegends.R;

import java.util.ArrayList;
import java.util.List;

public class Card {
    private int healthPoints;
    private int damagePoints;
    private String name;
    private int pictureID;
    private int viewID;
    private boolean active;
    private boolean isTapped;
    private int cost;
    private String[] names;
    private int[] dps = {4, 6, 5, 7, 2};
    private int[] hps = {4, 2, 6, 7, 6};
    private int[] costs = {5, 7, 7, 7, 6};
    private int[] ids = {R.drawable.peasant, R.drawable.archer, R.drawable.swordsman, R.drawable.vampire, R.drawable.wizard};

    public Card(Context context){
        this.names = new String[]{
            context.getResources().getText(R.string.card_name_peasant).toString(),
            context.getResources().getText(R.string.card_name_archer).toString(),
            context.getResources().getText(R.string.card_name_swordsman).toString(),
            context.getResources().getText(R.string.card_name_vampire).toString(),
            context.getResources().getText(R.string.card_name_wizard).toString()
        };
        int num = (int) (Math.random()*names.length);
        this.healthPoints = hps[num];
        this.damagePoints = dps[num];

        this.name = names[num];
        this.cost = costs[num];
        this.pictureID = ids[num];
        active = false;
        isTapped = false;
    }

    public int getCardsCost(String name, int hp, int dp){
        int prev_hp = 0, prev_dp = 0;
        for (int i = 0; i < names.length; i++)
            if (name.equals(names[i])){
                prev_dp = dps[i];
                prev_hp = hps[i];
                break;
            }
        return 20 * ((hp - prev_hp) + (dp - prev_dp) + 1);
    }

    public int getDhp(String name, int hp){
        int prev_hp = 0;
        for (int i = 0; i < names.length; i++)
            if (name.equals(names[i])){
                prev_hp = hps[i];
                break;
            }
        return hp - prev_hp + 1;
    }

    public int getDdp(String name, int dp){
        int prev_dp = 0;
        for (int i = 0; i < names.length; i++)
            if (name.equals(names[i])){
                prev_dp = dps[i];
                break;
            }
        return dp - prev_dp + 1;
    }

    public List<Card> getNewDeck(Context context){
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < names.length; i++){
            Card card = new Card(context);
            int hp = hps[i], dp = dps[i], cost = costs[i], id = ids[i];
            card.setDamagePoints(dp);
            card.setHealthPoints(hp);
            card.setCost(cost);
            card.setPictureID(id);
            card.setName(names[i]);
            cards.add(card);
        }
        return cards;
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

    public int[] getIds() {
        return ids;
    }

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
        for (int i = 0; i < names.length; i++) {
            if (cost == costs[i] && damagePoints == dps[i] && healthPoints == hps[i]) {
                name = names[i];
                break;
            }
        }
    }
}
