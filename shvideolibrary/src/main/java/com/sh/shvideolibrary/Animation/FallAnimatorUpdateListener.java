package com.sh.shvideolibrary.Animation;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.sh.shvideolibrary.R;
import com.sh.shvideolibrary.VideoInputActivity;

public class FallAnimatorUpdateListener implements ValueAnimator.AnimatorUpdateListener {
    boolean shoot = false;
    ImageView target;
    private Animation bingoAnim;
    VideoInputActivity main;
    public FallAnimatorUpdateListener(Context context, ImageView target, VideoInputActivity main){
        this.target = target;
        this.bingoAnim = AnimationUtils.loadAnimation(context, R.anim.bingo);            // 表情匹配动画
        bingoAnim.setAnimationListener(new BingoAnimationListener());
        this.main = main;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float yPosition = (Float)animation.getAnimatedValue();

        if(!shoot && yPosition > 600 && yPosition < 1300){

            main.startCaptureFrame();
            shoot = true;
        }else if(shoot && (yPosition > 1300)){
            main.stopCaptureFrame();
            shoot = false;
        }
    }

    public void bingo(final ImageView target){
        target.animate().scaleXBy(1.2f).scaleYBy(1.2f).setDuration(1000);
        target.animate().scaleYBy(0.2f).scaleXBy(0.2f).translationYBy(-100f).alpha(0).withEndAction(new Runnable() {
            @Override
            public void run() {
                target.setImageBitmap(null);
            }
        });
    }


}
