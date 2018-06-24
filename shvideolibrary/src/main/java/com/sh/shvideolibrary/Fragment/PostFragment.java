package com.sh.shvideolibrary.Fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sh.shvideolibrary.R;
import com.sh.shvideolibrary.VideoInputActivity;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment {


    VideoInputActivity main;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        //Bind view
        ButterKnife.bind(this, view);
        //Set main activity
        main = ((VideoInputActivity)getActivity());
        Button post = (Button) view.findViewById(R.id.btn_post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post();
            }
        });

        Button save_local = (Button) view.findViewById(R.id.btn_save_local);
        save_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveLocal();
            }
        });

        Button save_draft = (Button) view.findViewById(R.id.btn_save_draft);
        save_draft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDraft();
            }
        });


        return view;
    }

    public void post(){
//        main.removeFragment(main.postFragment);
//        main.addFragment(main.followFragment);
        main.replaceFragment(main.followFragment);
    }

    /**
     * 存本地，跳到homepage
     */
    public void saveLocal(){
        main.replaceFragment(main.homeFragment, 0);
//        main.homeFragment.homeTab.setTabIndex(0, true);

    }

    /**
     * 存为草稿，跳到homepage
     */
    public void saveDraft(){
        main.replaceFragment(main.homeFragment, 2);
//        main.homeFragment.homeTab.setTabIndex(2,true);

    }

}
