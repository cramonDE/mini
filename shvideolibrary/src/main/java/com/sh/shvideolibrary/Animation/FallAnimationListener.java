package com.sh.shvideolibrary.Animation;


import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

public class FallAnimationListener implements Animation.AnimationListener {
    ImageView view;

    public Animation.AnimationListener setView(ImageView view) {
        this.view = view;
        return this;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        view.setVisibility(View.INVISIBLE);
        view.clearAnimation();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
