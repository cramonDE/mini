package com.sh.shvideolibrary.Fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.sh.shvideolibrary.R;
import com.sh.shvideolibrary.VideoInputActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class MusicFragment extends Fragment {


    View view;
    VideoInputActivity main;

    ImageButton close;
    LinearLayout music_1;
    NavigationTabStrip musicTab;
    ScrollView music_scroll;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_music, container, false);
        close = (ImageButton) view.findViewById(R.id.ibt_close);
        musicTab = (NavigationTabStrip) view.findViewById(R.id.music_tab);
        music_1 = (LinearLayout) view.findViewById(R.id.music_1);
        music_scroll = (ScrollView) view.findViewById(R.id.music_scroll);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeMusic();
            }
        });

        //Bind view
        ButterKnife.bind(this, view);

        init();
        Log.d(TAG, "onCreateView: music");
        return view;
    }

    /**
     * 初始化音乐页
     */
    public void init(){
        //Set main activity
        main = (VideoInputActivity)getActivity();

        //tab默认选择第一个
        musicTab.setTabIndex(0, true);

        //tab call back
        musicTab.setOnTabStripSelectedIndexListener(new NavigationTabStrip.OnTabStripSelectedIndexListener() {
            @Override
            public void onStartTabSelected(String title, int index) {
                //热门音乐页显示音乐，我的收藏显示空白
                if(index==0){
                    music_scroll.setVisibility(View.VISIBLE);
                }else{
                    music_scroll.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onEndTabSelected(String title, int index) {

            }
        });
    }

    public void closeMusic(){
        main.removeFragment(main.musicFragment);
    }

    public void chooseMusic(View v){
        String tag = v.getTag().toString();

        //update music
        main.readyFragment.musicChosen = tag;
        main.replaceFragment(main.readyFragment);
    }
}
