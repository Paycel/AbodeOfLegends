package com.example.adobeoflegends.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.adobeoflegends.R;
import com.example.adobeoflegends.activity.BattleActivity;
import com.example.adobeoflegends.activity.Shop;
import com.example.adobeoflegends.database.DBHelper;

import java.util.Objects;

public class ShopDialog extends DialogFragment {
    private int cost;
    private int points;
    private String email;
    private String name;
    private int dp, hp;

    public ShopDialog(int cost, String name, int points, String email, int dp, int hp){
        this.cost = cost;
        this.points = points;
        this.email = email;
        this.name = name;
        this.dp = dp;
        this.hp = hp;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getText(R.string.dialog_title_shop).toString());
        builder.setMessage(getResources().getText(R.string.dialog_message_shop1).toString() + cost + getResources().getText(R.string.dialog_message_shop2).toString())
                .setPositiveButton(getResources().getText(R.string.yes).toString(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (points < cost) {
                            Toast.makeText(getContext(), getResources().getText(R.string.no_points).toString(), Toast.LENGTH_SHORT).show();
                            dismiss();
                        } else {
                            // TODO обновление поинтов со статами на экране
                            DBHelper dbHelper = new DBHelper(getContext());
                            dbHelper.updateCard(dbHelper.getWritableDatabase(), email, name, dp, hp, cost);
                        }
                    }
                })
                .setNegativeButton(getResources().getText(R.string.no).toString(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });
        return builder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(getActivity()).recreate();
    }
}
