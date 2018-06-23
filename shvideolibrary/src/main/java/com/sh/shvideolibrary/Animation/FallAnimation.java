package com.example.zhubingjing.test.Animation;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class FallAnimation{
    TranslateAnimation a;

    public FallAnimation(float position){
        a = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, position,  // from X
                Animation.RELATIVE_TO_PARENT, position,  // to X
                Animation.RELATIVE_TO_PARENT, -0.2f, // from Y
                Animation.RELATIVE_TO_PARENT, 1f); // to Y
        a.setInterpolator(new AccelerateInterpolator(1.0f));
        a.setDuration(5000); // milliseconds
    }

    public Animation getAnimation(){
        return a;
    }


}
