package com.example.adobeoflegends.objects;

import android.util.Log;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class Player extends Person{

    private TreeSet<String> achievements;
    private List<Card> deck;

    Player(int hp, int mp, List<Card>cardList) {
        super(hp, mp, cardList);
    }

    public Player(){
        super(0, 0, null);
        achievements = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int point1 = Integer.parseInt(o1.substring(o1.lastIndexOf(' ') + 1));
                int point2 = Integer.parseInt(o2.substring(o2.lastIndexOf(' ') + 1));
                if (point1 == point2) return -1;
                return point2 - point1;
            }
        });
    }

    public void addAchievement(String achievement) {
        if (achievements == null) achievements = new TreeSet<>();
        String stage = achievement.substring(0, achievement.lastIndexOf(' '));
        int new_p = Integer.parseInt(achievement.substring(achievement.lastIndexOf(' ') + 1));
        ArrayList<String> temp = new ArrayList<>(achievements);
        boolean added = false;
        boolean having = false;
        for (String s : temp) {
            Log.d("MY_LOGS", s);
            String c_stage = s.substring(0, s.lastIndexOf(' '));
            int last_p = Integer.parseInt(s.substring(s.lastIndexOf(' ') + 1));
            if (stage.equals(c_stage) && last_p < new_p){
                temp.remove(s);
                temp.add(achievement);
                added = true;
                break;
            } else if (stage.equals(c_stage)){
                having = true;
                break;
            }
        }
        if (!added && !having) temp.add(achievement);
        if (temp.size() == 11) temp.remove(temp.get(10));
        achievements.clear();
        achievements = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int point1 = Integer.parseInt(o1.substring(o1.lastIndexOf(' ') + 1));
                int point2 = Integer.parseInt(o2.substring(o2.lastIndexOf(' ') + 1));
                if (point1 == point2) return -1;
                return point2 - point1;
            }
        });
        achievements.addAll(temp);
    }

    public void sort(){
        ArrayList<String> temp = new ArrayList<>(achievements);
        achievements.clear();
        achievements = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int point1 = Integer.parseInt(o1.substring(o1.lastIndexOf(' ') + 1));
                int point2 = Integer.parseInt(o2.substring(o2.lastIndexOf(' ') + 1));
                if (point1 == point2) return -1;
                return point2 - point1;
            }
        });
        achievements.addAll(temp);
    }

    public List<Card> getDeck() {
        return deck;
    }

    public void setDeck(List<Card> cards){
        this.deck = cards;
    }

    public TreeSet<String> getAchievements(){
        return achievements;
    }
}
