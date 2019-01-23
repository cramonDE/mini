package com.sh.shvideolibrary.Fragment;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sh.shvideolibrary.Animation.FallAnimatorUpdateListener;
import com.sh.shvideolibrary.R;
import com.sh.shvideolibrary.VideoInputActivity;

import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class GamingFragment extends Fragment {


    VideoInputActivity main;
    View view;



    final long countdownTime = 4500;        // 倒数计时总时长
    final long countdownInterval = 1000;    // 倒数计时间隔时间
    private long gameTime,                  // 游戏总时长
            updateGameTimerInterval,        // 倒数更新间隔
            emojiDuration,                  // 每个emoji掉落动画时长
            emojiInterval;                  // emoji之间掉落间隔

    private CountDownTimer countDownTimer,  // 倒数计时器
            emojiTimer,                     // 表情掉落计时器
            gameTimer;                      // 游戏计时器
    private AlphaAnimation fadeIn;          // 文字淡入效果
    private Animation scaleAnim ;           // 放大效果
    private int positionNum;                // 位置（竖直通道）的数量
    private float deviceWidth;              // 所用设备宽度
    private float deviceHeight;             // 所用设备高度
    private int comboNum = 0;                   // combo个数
    public int musicIndex = 0;

    Button countdownBtn;
    ProgressBar progress;
    TextView countdown;
    ImageView backBtn;
    ImageView rec;
    TextView tv_gamecountdown;
    RelativeLayout gamingLayout;
    TextView combo;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_gaming, container, false);
        countdownBtn = (Button) view.findViewById(R.id.btn_countdown);
        progress = (ProgressBar) view.findViewById(R.id.progressBar);
        countdown = (TextView) view.findViewById(R.id.tv_countdown);
        backBtn = (ImageView) view.findViewById(R.id.ibt_back);
        tv_gamecountdown = (TextView) view.findViewById(R.id.tv_gamecountdown);
        gamingLayout = (RelativeLayout) view.findViewById(R.id.layout_gaming);
        combo = (TextView)view.findViewById(R.id.tv_combo);
        rec = (ImageView) view.findViewById(R.id.imageView_rec);

        //Bind view
        ButterKnife.bind(this, view);

        //Set main activity
        main = (VideoInputActivity)getActivity();

        init(15000, 500,5000, 2000, 5);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //退出界面时取消timer
        countDownTimer.cancel();
        gameTimer.cancel();
        emojiTimer.cancel();
    }

    public void back(){
        main.replaceFragment(main.readyFragment);
    }

    /**
     * 初始化
     */
    public void init(long gameTime, long updateGameTimerInterval, long emojiDuration, long emojiInterval, final int positionNum){

        //设备属性
        DisplayMetrics displayMetrics = new DisplayMetrics();
        main.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        deviceHeight = displayMetrics.heightPixels;
        deviceWidth  = displayMetrics.widthPixels;

        //初始化动画
        fadeIn = new AlphaAnimation(0.0f , 1.0f ) ;
        fadeIn.setDuration(300);
        fadeIn.setFillAfter(true);
        scaleAnim = AnimationUtils.loadAnimation(countdown.getContext(), R.anim.scale);

        //设置游戏时间属性
        this.gameTime = gameTime;
        this.updateGameTimerInterval = updateGameTimerInterval;
        this.emojiDuration = emojiDuration;
        this.emojiInterval = emojiInterval;
        this.positionNum = positionNum;

        this.tv_gamecountdown.setText(gameTime/1000+"");
        this.progress.setMax((int)gameTime);

        final long game = this.gameTime;

        //初始化倒数计时
        countDownTimer = new CountDownTimer(countdownTime, countdownInterval) {

            public void onTick(long millisUntilFinished) {
                updateCountDown(""+(millisUntilFinished/1000-1));

                if(millisUntilFinished/1000==1){
                    countdown.setText("Go!");
                }
            }

            public void onFinish() {
                //倒计时结束，移除倒计时，开始游戏
                startGame();
            }
        };

        //初始化游戏计时
        gameTimer = new CountDownTimer(game, updateGameTimerInterval) {

            public void onTick(long millisUntilFinished) {

                //更新倒计时文字
                updateGameCountDownText(""+millisUntilFinished / 1000);

                //更新进度条
                progress.setProgress((int)(game-millisUntilFinished));

            }

            public void onFinish() {
                updateGameCountDownText("\u2714");
                main.musicService.playerStop();
                comboNum = 0;
//                main.removeFragment(main.gamingFragment);
//                main.addFragment(main.previewFragment);
                main.replaceFragment(main.previewFragment);
                main.getFragmentManager().beginTransaction().detach(main.previewFragment).attach(main.previewFragment).commit();
            }
        };

        // 初始化表情掉落计时器
        emojiTimer = new CountDownTimer(game, emojiInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                int i = (int)Math.floor(Math.random() * 4.0);
                int j = (int)Math.floor(Math.random() * (float)positionNum);
                dropEmoji(i,j);
            }

            @Override
            public void onFinish() {

            }
        };

        // 开始游戏倒计时
        countDownTimer.start();
    }

    /**
     * 游戏正式开始，每隔interval更新界面
     */
    public void startGame() {
        //中线、进度条可见、返回按钮不可见
        rec.setVisibility(View.VISIBLE);
        progress.setVisibility(View.VISIBLE);
        countdown.setVisibility(View.INVISIBLE);
        backBtn.setVisibility(View.INVISIBLE);
        combo.setVisibility(View.VISIBLE);
        addComboNum();

        //游戏倒计时开始
        gameTimer.start();
        emojiTimer.start();
        main.musicService.playerStart(this.musicIndex);
    }

    /**
     * 生成特定emoji
     * @param index
     * @return
     */
    private ImageView createEmoji(int index){
        ImageView emoji = new ImageView(main);
        int id = getResources().getIdentifier( "emoji" + (index+1), "drawable", getActivity().getPackageName());
        emoji.setImageResource(id);
        Log.d(TAG, "createEmoji: "+ emoji.toString());
        emoji.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        gamingLayout.addView(emoji);
        return emoji;
    }

    /**
     * 更新游戏倒计时按钮
     * @param str
     */
    public void updateGameCountDownText(String str){
        tv_gamecountdown.setText(str);
        tv_gamecountdown.startAnimation(fadeIn);
    }

    /**
     * 更新开始前倒计时剩余时间
     * @param str
     */
    public void updateCountDown(String str){
        countdown.setText(str);
        countdown.startAnimation(scaleAnim);
    }

    /**
     * 增加combo数
     */
    public void addComboNum(){
        combo.setText(this.comboNum++ +" Combo");
    }

    /**
     * 在屏幕上特定位置加入特定的emoji下落效果
     * @param emojiIndex emoji编号：0-3
     * @param positionIndex 位置编号
     */
    public void dropEmoji(int emojiIndex, int positionIndex){
        // get emoji
        ImageView image = createEmoji(emojiIndex);
        // set horizontal position
        image.setX( deviceWidth / (positionNum+1) * (positionIndex));

        // new animatorSet
        ObjectAnimator anim = ObjectAnimator.ofFloat(image, "y", -80.f, deviceHeight + 150.f);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(emojiDuration);
        set.play(anim);
        set.setTarget(image);
        set.start();

        ((ObjectAnimator)set.getChildAnimations().get(0)).addUpdateListener(new FallAnimatorUpdateListener(image.getContext(), image, main));
    }


}
