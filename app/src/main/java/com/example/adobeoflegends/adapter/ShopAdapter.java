package com.example.adobeoflegends.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;

import com.example.adobeoflegends.R;
import com.example.adobeoflegends.activity.BattleActivity;
import com.example.adobeoflegends.database.DBHelper;
import com.example.adobeoflegends.dialogs.ShopDialog;
import com.example.adobeoflegends.objects.Card;
import com.example.adobeoflegends.objects.Player;

import java.util.List;

public class ShopAdapter extends BaseAdapter {
    private Context context;
    private List<Card> cards;
    private LayoutInflater inflater;
    private String email;
    private FragmentManager fragmentManager;
    private String btn_name;

    public ShopAdapter(FragmentManager fragmentManager, Context context, Player player, String email, String btn_name){
        this.context = context;
        this.btn_name = btn_name;
        this.cards = player.getDeck();
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.email = email;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public int getCount() {
        return cards.size();
    }

    @Override
    public Object getItem(int position) {
        return cards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) view = inflater.inflate(R.layout.item_shop, parent, false);
        int id = 0;
        //TODO костыль
        switch (cards.get(position).getName()){
            case "Крестьянин": id = R.drawable.peasant; break;
            case "Дракон": id = R.drawable.dragon; break;
            case "Волшебник": id = R.drawable.wizard; break;
            case "Вампир": id = R.drawable.vampire; break;
        }
        final int dH = new Card().getDhp(cards.get(position).getName(), cards.get(position).getHealthPoints()),
                dP = new Card().getDdp(cards.get(position).getName(), cards.get(position).getDamagePoints());
        ((ImageView) view.findViewById(R.id.card_image)).setImageResource(id);
        ((TextView) view.findViewById(R.id.tv_prevHP)).setText(String.valueOf(cards.get(position).getHealthPoints()));
        ((TextView) view.findViewById(R.id.tv_addHP)).setText("+" + dH);
        ((TextView) view.findViewById(R.id.tv_prevDP)).setText(String.valueOf(cards.get(position).getDamagePoints()));
        ((TextView) view.findViewById(R.id.tv_addDP)).setText("+" + dP);
        Button btn = (Button) view.findViewById(R.id.btn_up);
        btn.setText(btn_name);
        Log.d("temp", btn_name);
        final DBHelper dbHelper = new DBHelper(context);
        final int cost = new Card().getCardsCost(cards.get(position).getName(), cards.get(position).getHealthPoints(), cards.get(position).getDamagePoints());
        final String name = cards.get(position).getName();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopDialog shopDialog = new ShopDialog(cost, name, dbHelper.getPoints(dbHelper.getWritableDatabase(), email), email, dP, dH);
                shopDialog.show(fragmentManager, "Shop_Dialog");
            }
        });
        return view;
    }
}
