package com.example.adobeoflegends;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class EndGameDialog extends DialogFragment {
    /*
    mode - выбор режима:
    1 - окончание игры - поражение
    2 - окончание игры - победа
     */

    int mode;

    EndGameDialog(int mode){
        this.mode = mode;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = "";
        String message = "";
        String btnAgain = getResources().getText(R.string.again).toString();
        String btnExit = getResources().getText(R.string.exit).toString();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        switch (mode){
            case 1:
                title = getResources().getText(R.string.end_title_lose).toString();
                message = getResources().getText(R.string.end_message_lose).toString();
                break;
            case 2:
                title = getResources().getText(R.string.end_title_win).toString();
                message = getResources().getText(R.string.end_message_win).toString();
                break;
        }
        builder.setTitle(title);
        builder.setMessage(message);
        // правая кнопка
        builder.setPositiveButton(btnExit, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent i = new Intent(getActivity(), Menu.class);
                startActivity(i);
            }
        });
        // левая кнопка
        builder.setNegativeButton(btnAgain, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent i = new Intent(getActivity(), BattleActivity.class);
                startActivity(i);
            }
        });
        setCancelable(false);
        return builder.create();
    }
}
