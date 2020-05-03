package com.example.adobeoflegends.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.example.adobeoflegends.R;
import com.example.adobeoflegends.activity.BattleActivity;

public class Info extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getResources().getText(R.string.info_message).toString())
                .setPositiveButton(getResources().getText(R.string.close).toString(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton(getResources().getText(R.string.how_to_play).toString(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Info_Second info_second = new Info_Second(false);
                        dismiss();
                        info_second.show(BattleActivity.fragmentManager, "info_second");
                    }
                })
                .setNeutralButton(getResources().getText(R.string.journal).toString(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Info_Second info_second = new Info_Second(true);
                        dismiss();
                        info_second.show(BattleActivity.fragmentManager, "info_second_dialog");
                    }
                });
        return builder.create();
    }
}
