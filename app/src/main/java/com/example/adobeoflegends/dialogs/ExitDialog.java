package com.example.adobeoflegends.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.adobeoflegends.activity.BattleActivity;
import com.example.adobeoflegends.activity.Menu;
import com.example.adobeoflegends.R;

import static com.example.adobeoflegends.activity.BattleActivity.currentUser;

public class ExitDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String title = getResources().getText(R.string.exit_dialog_title).toString();
        String message = getResources().getText(R.string.exit_dialog_message).toString();
        String btnYes = getResources().getText(R.string.yes).toString();
        String btnNo = getResources().getText(R.string.no).toString();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        // правая кнопка
        builder.setPositiveButton(btnYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent i = new Intent(getActivity(), Menu.class);
                i.putExtra("points", BattleActivity.getPoints());
                i.putExtra("currentUser", currentUser);
                startActivity(i);
            }
        });
        // левая кнопка
        builder.setNegativeButton(btnNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dismiss();
            }
        });
        setCancelable(false);
        return builder.create();
    }
}
