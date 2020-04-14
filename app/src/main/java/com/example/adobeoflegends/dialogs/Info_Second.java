package com.example.adobeoflegends.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.adobeoflegends.Battle;
import com.example.adobeoflegends.R;
import com.example.adobeoflegends.activity.BattleActivity;

import java.util.ArrayList;

public class Info_Second extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        ScrollView scrollView = new ScrollView(getActivity().getApplicationContext());
        TextView textView = new TextView(getActivity().getApplicationContext());
        for (int i = 0; i < BattleActivity.log.size(); i++){
            textView.setText(textView.getText().toString() + BattleActivity.log.get(i) + "\n");
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        textView.setTextSize(15);
        scrollView.addView(textView);
        builder.setView(scrollView);
        return builder.create();
    }
}
