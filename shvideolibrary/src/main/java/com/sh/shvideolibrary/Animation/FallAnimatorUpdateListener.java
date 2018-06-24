package com.example.zhubingjing.test.Animation;

import android.animation.ValueAnimator;
import android.util.Log;

public class FallAnimatorUpdateListener implements ValueAnimator.AnimatorUpdateListener {
    boolean shoot = false;

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

}
