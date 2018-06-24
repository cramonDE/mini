package com.sh.shvideolibrary.Animation;

import android.view.animation.Animation;
import android.widget.ImageView;

public class BingoAnimationListener implements Animation.AnimationListener {
    ImageView view;

    public void BingoAnimationListener(ImageView view) {
        this.view = view;
    }

    public void onAnimationEnd(Animation animation) {
        view.setImageBitmap(null);
    }
    public void onAnimationRepeat(Animation animation) {
    }
    public void onAnimationStart(Animation animation) {

    }
}
