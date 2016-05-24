package com.mkmkbug.my2048;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.HashMap;

public class Card extends FrameLayout {

    private static int cardWidth;
    private static HashMap<Integer, Integer> fontMap;
    private static HashMap<Integer, Integer> resMap;

    static {
        fontMap = new HashMap<>(6);
        fontMap.put(0, 0);
        fontMap.put(1, 34);
        fontMap.put(2, 34);
        fontMap.put(3, 34);
        fontMap.put(4, 32);
        fontMap.put(5, 27);
        fontMap.put(6, 24);
        resMap = new HashMap<>(12);
        resMap.put(0, R.drawable.card_shape_0);
        resMap.put(2, R.drawable.card_shape_2);
        resMap.put(4, R.drawable.card_shape_4);
        resMap.put(8, R.drawable.card_shape_8);
        resMap.put(16, R.drawable.card_shape_16);
        resMap.put(32, R.drawable.card_shape_32);
        resMap.put(64, R.drawable.card_shape_64);
        resMap.put(128, R.drawable.card_shape_128);
        resMap.put(256, R.drawable.card_shape_256);
        resMap.put(512, R.drawable.card_shape_512);
        resMap.put(1024, R.drawable.card_shape_1024);
        resMap.put(2048, R.drawable.card_shape_2048);
        resMap.put(4096, R.drawable.card_shape_4096);
    }

    private int number;
    private TextView numberText;

    public Card(Context context) {
        super(context);
        init();
    }

    public Card(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Card(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
        numberText.setText(number == 0 ? "" : String.valueOf(number));
        numberText.setTextSize(fontMap.get(numberText.getText().length()));
        Integer resId = resMap.get(number);
        if (resId != null) {
            numberText.setBackgroundResource(resMap.get(number));
        } else {
            numberText.setBackgroundResource(R.drawable.card_shape_default);
        }
        if (number != 0) {
            ScaleAnimation anim = new ScaleAnimation(1.0f, 0.6f, 1.0f, 0.6f, 1, 0.5f, 1, 0.5f);
            anim.setDuration(150);
            startAnimation(anim);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    if (numberText.getText().toString().equals("")) {
                        animation.cancel();
                    }
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    ScaleAnimation anim = new ScaleAnimation(0.6f, 1.0f, 0.6f, 1.0f, 1, 0.5f, 1, 0.5f);
                    anim.setDuration(150);
                    Card.this.startAnimation(anim);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    private void init() {
        if (cardWidth == 0) {
            cardWidth = ((MainActivity) getContext()).getGameContainerLength() / 4 - 10 * 2;
        }
        setBackgroundResource(R.drawable.card_shape_0);
        numberText = new TextView(getContext());
        numberText.setWidth(cardWidth);
        numberText.setHeight(cardWidth);
        numberText.setGravity(Gravity.CENTER);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        addView(numberText, params);
    }

    public boolean equals(Card o) {
        return number == o.number;
    }

}
