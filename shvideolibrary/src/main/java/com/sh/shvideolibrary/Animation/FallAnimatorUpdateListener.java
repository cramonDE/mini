package com.sh.shvideolibrary.Animation;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.sh.shvideolibrary.R;

public class FallAnimatorUpdateListener implements ValueAnimator.AnimatorUpdateListener {
    boolean shoot = false;
    ImageView target;
    private Animation bingoAnim;

    public FallAnimatorUpdateListener(Context context, ImageView target){
        this.target = target;
        this.bingoAnim = AnimationUtils.loadAnimation(context, R.anim.bingo);            // 表情匹配动画
        bingoAnim.setAnimationListener(new BingoAnimationListener());
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float yPosition = (Float)animation.getAnimatedValue();

        if(!shoot && yPosition > 600 && yPosition < 1300){
//                    startTakePhoto();
            Log.d("SHOOOOOT", "onAnimationUpdate: "+"START");

            shoot = true;
        }else if(shoot && (yPosition > 1300)){
//                    stopTakePhoto();
            Log.d("SHOOOOOT", "onAnimationUpdate: "+"STOP");
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
