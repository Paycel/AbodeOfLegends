package com.example.adobeoflegends.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.adobeoflegends.R;
import com.example.adobeoflegends.objects.Player;

import java.util.Map;
import java.util.TreeSet;

public class AchievementsAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private TreeSet<String> achievements;
    private int size;

    public AchievementsAdapter(Context context, Player player){
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.achievements = player.getAchievements();
        this.size = this.achievements.size();
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public Object getItem(int position) {
        return achievements.first();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) view = inflater.inflate(R.layout.item_achieve, parent, false);
        String data = getItem();
        ((TextView) view.findViewById(R.id.number)).setText(String.valueOf(position + 1));
        ((TextView) view.findViewById(R.id.stage)).setText(data.substring(0, data.lastIndexOf(' ')));
        ((TextView) view.findViewById(R.id.points)).setText(data.substring(data.lastIndexOf(' ') + 1));
        return view;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    private String getItem(){
        String item = achievements.pollFirst();
        item = context.getResources().getText(R.string.stage).toString() + item.substring(item.indexOf(' ') + 1);
        return item;
    }

}
