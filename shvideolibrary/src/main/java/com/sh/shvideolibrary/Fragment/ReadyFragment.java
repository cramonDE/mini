package com.sh.shvideolibrary.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sh.shvideolibrary.R;
import com.sh.shvideolibrary.VideoInputActivity;

import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

public class ReadyFragment extends Fragment {
    VideoInputActivity main;

    ImageButton startBtn;
    RelativeLayout music;
    TextView musicTV;

    String musicChosen = "音乐";
    private Animation scaleAnim;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ready, container, false);
        //Bind view
        ButterKnife.bind(this, view);
        //Set main activity
        main = ((VideoInputActivity)getActivity());
        startBtn = (ImageButton) view.findViewById(R.id.ibt_start);
        music = (RelativeLayout) view.findViewById(R.id.music);
        musicTV = (TextView) view.findViewById(R.id.tv_music);
        //set music name
        musicTV.setText(musicChosen);
        musicTV.setSelected(true);
        Log.d(TAG, "onCreateView: readyFragment");
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame();
            }
        });
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectMusic();
            }
        });
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("reusme", "onResume: ");
    }

    /**
     * 开始游戏
     */
    public void startGame(){
        scaleAnim = AnimationUtils.loadAnimation(getView().getContext(), R.anim.scale);
        startBtn.startAnimation(scaleAnim);
        main.replaceFragment(main.gamingFragment);
    }

    /**
     * 选择音乐
     */
    public void selectMusic(){
        main.addFragment(main.musicFragment);
    }
}
