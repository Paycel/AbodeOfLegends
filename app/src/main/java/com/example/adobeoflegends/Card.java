package com.example.adobeoflegends;

import android.media.Image;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class Card {
    int healthPoints;
    int damagePoints;
    String name;
    int pictureID;
    int viewID;
    boolean active;

    Card(){
        int hp =  (int)(Math.random()*10 + 1);
        int dp =  (int)(Math.random()*10 + 1);
        int num = (int) (Math.random()*Battle.numsOfCards);
        String names[] = {"Дракон", "Крестьянин", "Вампир", "Волшебник"};
        this.healthPoints = hp;
        this.damagePoints = dp;
        this.name = names[num];
        switch (num){
            case 0: this.pictureID = R.drawable.dragon; break;
            case 1: this.pictureID = R.drawable.kek; break;
            case 2: this.pictureID = R.drawable.vampire; break;
            case 3: this.pictureID = R.drawable.wizard; break;
        }
        active = false;
    }

    public int getPictureID() {
        return pictureID;
    }
}
