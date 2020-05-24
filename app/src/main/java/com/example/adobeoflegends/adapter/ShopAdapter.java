package com.example.adobeoflegends.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.example.adobeoflegends.R;
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
    private Player player;

    public ShopAdapter(FragmentManager fragmentManager, Context context, Player player, String email, String btn_name){
        this.context = context;
        this.btn_name = btn_name;
        this.cards = player.getDeck();
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.email = email;
        this.fragmentManager = fragmentManager;
        this.player = player;
    }

    @Override
    public int getCount() {
        return cards.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return cards.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position - 1;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) view = inflater.inflate(R.layout.item_shop, parent, false);
        int id;
        // first card - is Player
        if (position == 0){
            id = R.drawable.you;
            ((TextView) view.findViewById(R.id.tv_prevHP)).setText("" + player.getHealthPoints());
            ((TextView) view.findViewById(R.id.tv_addHP)).setText("+ 1");
            ((TextView) view.findViewById(R.id.tv_prevDP)).setText("" + player.getManaPoints());
            ((TextView) view.findViewById(R.id.tv_prevDP)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.mana, 0, 0, 0);
            ((TextView) view.findViewById(R.id.tv_addDP)).setText("+ 1");
            Button btn = (Button) view.findViewById(R.id.btn_up);
            btn.setText(btn_name);
            final DBHelper dbHelper = new DBHelper(context);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShopDialog shopDialog = new ShopDialog((player.getHealthPoints() - 19) * 20, "", dbHelper.getPoints(dbHelper.getWritableDatabase(), email),
                            email, 1, 1);
                    shopDialog.show(fragmentManager, "Shop_Dialog");
                }
            });
        } else { // Player cards
            position -= 1;
            id = cards.get(position).getIds()[position];
            final int dH = new Card(context).getDhp(cards.get(position).getName(), cards.get(position).getHealthPoints()),
                    dP = new Card(context).getDdp(cards.get(position).getName(), cards.get(position).getDamagePoints());
            ((TextView) view.findViewById(R.id.tv_prevHP)).setText(String.valueOf(cards.get(position).getHealthPoints()));
            ((TextView) view.findViewById(R.id.tv_addHP)).setText("+" + dH);
            ((TextView) view.findViewById(R.id.tv_prevDP)).setText(String.valueOf(cards.get(position).getDamagePoints()));
            ((TextView) view.findViewById(R.id.tv_addDP)).setText("+" + dP);
            Button btn = (Button) view.findViewById(R.id.btn_up);
            btn.setText(btn_name);
            final DBHelper dbHelper = new DBHelper(context);
            final int cost = new Card(context).getCardsCost(cards.get(position).getName(), cards.get(position).getHealthPoints(), cards.get(position).getDamagePoints());
            final String name = cards.get(position).getName();
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShopDialog shopDialog = new ShopDialog(cost, name, dbHelper.getPoints(dbHelper.getWritableDatabase(), email), email, dP, dH);
                    shopDialog.show(fragmentManager, "Shop_Dialog");
                }
            });
        }
        ((ImageView) view.findViewById(R.id.card_image)).setImageResource(id);
        return view;
    }
}
