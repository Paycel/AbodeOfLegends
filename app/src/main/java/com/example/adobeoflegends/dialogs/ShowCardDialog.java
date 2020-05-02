package com.example.adobeoflegends.dialogs;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.DialogFragment;

import com.example.adobeoflegends.objects.Card;
import com.example.adobeoflegends.R;

import static com.example.adobeoflegends.activity.Battle.findCard;


public class ShowCardDialog extends DialogFragment {

    ConstraintLayout card;

    public ShowCardDialog(ConstraintLayout card){
        this.card = card;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        ConstraintLayout copy = new ConstraintLayout(getActivity().getApplicationContext());
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int width = (int) ((float)display.getWidth() * 0.8);
        int height = (int) ((float)display.getHeight() * 0.8);
        int imageWidth = (int) ((float) width * 0.15);
        int imageHeight = (int) ((float) height * 0.15);
        Card myCard = findCard(card.getId());
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), myCard.getPictureID());
        BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);
        drawable.setAntiAlias(false);
        copy.setBackground(drawable);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(imageWidth, imageHeight);
        ImageView imageHP = new ImageView(copy.getContext());
        imageHP.setId(View.generateViewId());
        imageHP.setLayoutParams(imageParams);
        imageHP.setScaleType(ImageView.ScaleType.FIT_CENTER);
        switch (myCard.getHealthPoints()){
            case 1: imageHP.setImageResource(R.drawable.onehp); break;
            case 2: imageHP.setImageResource(R.drawable.twohp); break;
            case 3: imageHP.setImageResource(R.drawable.threehp); break;
            case 4: imageHP.setImageResource(R.drawable.fourhp); break;
            case 5: imageHP.setImageResource(R.drawable.fivehp); break;
            case 6: imageHP.setImageResource(R.drawable.sixhp); break;
            case 7: imageHP.setImageResource(R.drawable.sevenhp); break;
            case 8: imageHP.setImageResource(R.drawable.eighthp); break;
            case 9: imageHP.setImageResource(R.drawable.ninehp); break;
            case 10: imageHP.setImageResource(R.drawable.tenhp); break;
        }
        ImageView imageDP = new ImageView(copy.getContext());
        imageDP.setId(View.generateViewId());
        imageDP.setLayoutParams(imageParams);
        switch (myCard.getDamagePoints()){
            case 1: imageDP.setImageResource(R.drawable.onedp); break;
            case 2: imageDP.setImageResource(R.drawable.twodp); break;
            case 3: imageDP.setImageResource(R.drawable.threedp); break;
            case 4: imageDP.setImageResource(R.drawable.fourdp); break;
            case 5: imageDP.setImageResource(R.drawable.fivedp); break;
            case 6: imageDP.setImageResource(R.drawable.sixdp); break;
            case 7: imageDP.setImageResource(R.drawable.sevendp); break;
            case 8: imageDP.setImageResource(R.drawable.eightdp); break;
            case 9: imageDP.setImageResource(R.drawable.ninedp); break;
            case 10: imageDP.setImageResource(R.drawable.tendp); break;
        }
        ImageView imageCOST = new ImageView(copy.getContext());
        imageCOST.setId(View.generateViewId());
        imageCOST.setLayoutParams(imageParams);
        switch (myCard.getCost()){
            case 1: imageCOST.setImageResource(R.drawable.onemp); break;
            case 2: imageCOST.setImageResource(R.drawable.twomp); break;
            case 3: imageCOST.setImageResource(R.drawable.threemp); break;
            case 4: imageCOST.setImageResource(R.drawable.fourmp); break;
            case 5: imageCOST.setImageResource(R.drawable.fivemp); break;
            case 6: imageCOST.setImageResource(R.drawable.sixmp); break;
            case 7: imageCOST.setImageResource(R.drawable.sevenmp); break;
            case 8: imageCOST.setImageResource(R.drawable.eightmp); break;
            case 9: imageCOST.setImageResource(R.drawable.ninemp); break;
            case 10: imageCOST.setImageResource(R.drawable.tenmp); break;
        }
        copy.addView(imageDP);
        copy.addView(imageHP);
        copy.addView(imageCOST);
        ConstraintSet set = new ConstraintSet();
        int m = (int) ((float) height * 0.13);
        int c = (int) ((float) height * 0.1);
        set.clone(copy);
        set.connect(imageHP.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, m);
        set.connect(imageHP.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, width - imageWidth);
        set.connect(imageHP.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, height - imageHeight);
        set.connect(imageHP.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);

        set.connect(imageDP.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, m);
        set.connect(imageDP.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, width - imageWidth);
        set.connect(imageDP.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, height - imageHeight);
        set.connect(imageDP.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);

        set.connect(imageCOST.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, height - imageHeight);
        set.connect(imageCOST.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        set.connect(imageCOST.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, c);
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
