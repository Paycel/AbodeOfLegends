package com.example.adobeoflegends.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.adobeoflegends.activity.Battle;
import com.example.adobeoflegends.activity.Menu;
import com.example.adobeoflegends.R;
import com.example.adobeoflegends.database.DBHelper;

import static com.example.adobeoflegends.activity.Battle.currentUser;
import static com.example.adobeoflegends.activity.Battle.difficulty;

public class EndGameDialog extends DialogFragment {
    /*
    mode - выбор режима:
    1 - окончание игры - поражение
    2 - окончание игры - победа
     */

    int mode;

    public EndGameDialog(int mode){
        this.mode = mode;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = "";
        String message = "";
        String btnAgain = getResources().getText(R.string.again).toString();
        String btnExit = getResources().getText(R.string.exit).toString();
        String btnYes = getResources().getText(R.string.yes).toString();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        switch (mode){
            case 1:
                title = getResources().getText(R.string.end_title_lose).toString();
                message = getResources().getText(R.string.end_message_lose).toString();
                // правая кнопка
                builder.setPositiveButton(btnExit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(getActivity(), Menu.class);
                        i.putExtra("points", Battle.getPoints());
                        DBHelper dbHelper = new DBHelper(getContext());
                        dbHelper.addACH(dbHelper.getWritableDatabase(), currentUser,
                                getResources().getText(R.string.stage).toString() + difficulty + " " + Battle.getLevelPoints());
                        startActivity(i);
                    }
                });
                // левая кнопка
                builder.setNegativeButton(btnAgain, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(getActivity(), Battle.class);
                        i.putExtra("points", Battle.getPoints());
                        DBHelper dbHelper = new DBHelper(getContext());
                        dbHelper.addACH(dbHelper.getWritableDatabase(), currentUser,
                                getResources().getText(R.string.stage).toString() + difficulty + " " + Battle.getLevelPoints());
                        startActivity(i);
                    }
                });
                break;
            case 2:
                title = getResources().getText(R.string.end_title_win).toString();
                if (Battle.difficulty == 12)
                    message = getResources().getText(R.string.last_level).toString();
                else message = getResources().getText(R.string.end_message_win).toString();
                // правая кнопка
                builder.setPositiveButton(btnExit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(getActivity(), Menu.class);
                        i.putExtra("points", Battle.getPoints());
                        i.putExtra("currentUser", currentUser);
                        DBHelper dbHelper = new DBHelper(getContext());
                        dbHelper.addACH(dbHelper.getWritableDatabase(), currentUser,
                                getResources().getText(R.string.stage).toString() + difficulty + " " + Battle.getLevelPoints());
                        startActivity(i);
                    }
                });
                // левая кнопка
                builder.setNegativeButton(btnAgain, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(getActivity(), Battle.class);
                        i.putExtra("points", Battle.getPoints());
                        i.putExtra("currentUser", currentUser);
                        DBHelper dbHelper = new DBHelper(getContext());
                        dbHelper.addACH(dbHelper.getWritableDatabase(), currentUser,
                                getResources().getText(R.string.stage).toString() + difficulty + " " + Battle.getLevelPoints());
                        startActivity(i);
                    }
                });
                if (Battle.difficulty != 12) {
                    builder.setNeutralButton(btnYes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(getActivity(), Battle.class);
                            i.putExtra("points", Battle.getPoints());
                            i.putExtra("difficulty", Battle.difficulty + 1);
                            i.putExtra("currentUser", currentUser);
                            DBHelper dbHelper = new DBHelper(getContext());
                            dbHelper.addACH(dbHelper.getWritableDatabase(), currentUser,
                                    getResources().getText(R.string.stage).toString() + difficulty + " " + Battle.getLevelPoints());
                            startActivity(i);
                        }
                    });
                }
                break;
        }
        builder.setTitle(title);
        builder.setMessage(message);
        setCancelable(false);
        return builder.create();
    }
}
