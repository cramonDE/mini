package com.sh.shvideolibrary.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.sh.shvideolibrary.R;
import com.sh.shvideolibrary.VideoInputActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeFragment extends Fragment {
    VideoInputActivity main;

    NavigationTabStrip homeTab;
    ScrollView home_scroll;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //Bind view
        ButterKnife.bind(this, view);
        homeTab = (NavigationTabStrip) view.findViewById(R.id.home_tab);
        home_scroll = (ScrollView) view.findViewById(R.id.home_scroll);

        init();

        return view;
    }

    public void init(){
        //Set main activity
        main = ((VideoInputActivity)getActivity());

        //tab默认选择第一个
        homeTab.setTabIndex(0, true);

        //tab call back
        homeTab.setOnTabStripSelectedIndexListener(new NavigationTabStrip.OnTabStripSelectedIndexListener() {
            @Override
            public void onStartTabSelected(String title, int index) {
                //作品页显示视频，其他显示空白
                if(index==0){
                    home_scroll.setVisibility(View.VISIBLE);
                }else{
                    home_scroll.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onEndTabSelected(String title, int index) {

            }
        });
    }
}
