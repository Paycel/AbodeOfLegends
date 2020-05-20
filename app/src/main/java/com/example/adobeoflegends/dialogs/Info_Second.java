package com.example.adobeoflegends.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import com.example.adobeoflegends.R;
import com.example.adobeoflegends.activity.BattleActivity;

import java.util.Objects;

public class Info_Second extends DialogFragment {
    private boolean journal;

    public Info_Second(boolean journal){
        this.journal = journal;
    }

    @NonNull
    @SuppressLint("SetTextI18n")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        ScrollView scrollView = new ScrollView(Objects.requireNonNull(getActivity()).getApplicationContext());
        TextView textView = new TextView(scrollView.getContext());
        textView.setTypeface(ResourcesCompat.getFont(Objects.requireNonNull(getContext()), R.font.pixel));
        if (journal)
        for (int i = 0; i < BattleActivity.log.size(); i++){
            textView.setText(textView.getText().toString() + BattleActivity.log.get(i) + "\n");
        }
        else
            for (int i = 0; i < 12; i++){
                switch (i){
                    case 0: textView.setText(textView.getText().toString() + getResources().getText(R.string.info_1).toString() + "\n");break;
                    case 1: textView.setText(textView.getText().toString() + getResources().getText(R.string.info_2).toString() + "\n");break;
                    case 2: textView.setText(textView.getText().toString() + getResources().getText(R.string.info_3).toString() + "\n");break;
                    case 3: textView.setText(textView.getText().toString() + getResources().getText(R.string.info_4).toString() + "\n");break;
                    case 4: textView.setText(textView.getText().toString() + getResources().getText(R.string.info_5).toString() + "\n");break;
                    case 5: textView.setText(textView.getText().toString() + getResources().getText(R.string.info_6).toString() + "\n");break;
                    case 6: textView.setText(textView.getText().toString() + getResources().getText(R.string.info_7).toString() + "\n");break;
                    case 7: textView.setText(textView.getText().toString() + getResources().getText(R.string.info_8).toString() + "\n");break;
                    case 8: textView.setText(textView.getText().toString() + getResources().getText(R.string.info_9).toString() + "\n");break;
                    case 9: textView.setText(textView.getText().toString() + getResources().getText(R.string.info_10).toString() + "\n");break;
                    case 10: textView.setText(textView.getText().toString() + getResources().getText(R.string.info_11).toString() + "\n");break;
                    case 11: textView.setText(textView.getText().toString() + getResources().getText(R.string.info_12).toString() + "\n");break;
                }
            }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        textView.setTextSize(16);
        textView.setVisibility(View.VISIBLE);
        scrollView.addView(textView);
        builder.setView(scrollView);
        return builder.create();
    }
}
