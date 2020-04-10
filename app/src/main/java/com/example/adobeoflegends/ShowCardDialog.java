package com.example.adobeoflegends;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.DialogFragment;

import static com.example.adobeoflegends.BattleActivity.MAIN;
import static com.example.adobeoflegends.BattleActivity.findCard;
import static com.example.adobeoflegends.BattleActivity.getParentTable;
import static com.example.adobeoflegends.BattleActivity.playerDeck;
import static com.example.adobeoflegends.BattleActivity.secondToFirst;


public class ShowCardDialog extends DialogFragment {

    ConstraintLayout card;

    ShowCardDialog(ConstraintLayout card){
        this.card = card;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        ConstraintLayout copy = new ConstraintLayout(getActivity().getApplicationContext());
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int width = (int) ((float)display.getWidth() * 0.8);
        int height = (int) ((float)display.getHeight() * 0.8);
        int imageWidth = (int) ((float) width * 0.2);
        int imageHeight = (int) ((float) height * 0.2);
        Card myCard = findCard(card.getId());
        copy.setBackgroundResource(myCard.pictureID);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(imageWidth, imageHeight);
        ImageView imageHP = new ImageView(copy.getContext());
        imageHP.setId(View.generateViewId());
        imageHP.setLayoutParams(imageParams);
        if (getParentTable(secondToFirst(card)) == playerDeck) {
            imageHP.setImageResource(R.drawable.cover_of_card);
            imageHP.setScaleType(ImageView.ScaleType.FIT_CENTER);
        } else {
            imageHP.setImageResource(R.drawable.cover_of_card);
            imageHP.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
        ImageView imageDP = new ImageView(copy.getContext());
        imageDP.setId(View.generateViewId());
        imageDP.setLayoutParams(imageParams);
        if (getParentTable(secondToFirst(card)) == playerDeck) {
            imageDP.setImageResource(R.drawable.kek);
            imageDP.setScaleType(ImageView.ScaleType.FIT_CENTER);
        } else {
            imageDP.setImageResource(R.drawable.kek);
            imageDP.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
        ImageView imageCOST = new ImageView(copy.getContext());
        imageCOST.setId(View.generateViewId());
        imageCOST.setLayoutParams(imageParams);
        if (getParentTable(secondToFirst(card)) == playerDeck) {
            imageCOST.setImageResource(R.drawable.dragon);
            imageCOST.setScaleType(ImageView.ScaleType.FIT_CENTER);
        } else {
            imageCOST.setImageResource(R.drawable.dragon);
            imageCOST.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
        copy.addView(imageHP);
        copy.addView(imageDP);
        copy.addView(imageCOST);
        ConstraintSet set = new ConstraintSet();
        set.clone(copy);
        set.connect(imageHP.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        set.connect(imageHP.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
        set.connect(imageHP.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, height - imageHeight);
        set.connect(imageHP.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, width - imageWidth);

        set.connect(imageDP.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        set.connect(imageDP.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        set.connect(imageDP.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, height - imageHeight);
        set.connect(imageDP.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, width - imageWidth);

        set.connect(imageCOST.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, height - imageHeight);
        set.connect(imageCOST.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        set.connect(imageCOST.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        set.connect(imageCOST.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, width - imageWidth);
        set.applyTo(copy);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(width, height);
        copy.setLayoutParams(params);
        Dialog dialog = new Dialog(getActivity());
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        dialog.setContentView(copy);
        return dialog;
    }

}
