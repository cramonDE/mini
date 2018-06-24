package com.sh.shvideolibrary.Fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sh.shvideolibrary.R;
import com.sh.shvideolibrary.VideoInputActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

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

        return view;
    }

    public void post(){
        main.removeFragment(main.postFragment);
        main.addFragment(main.followFragment);
//        main.replaceFragment(main.followFragment);
    }

}
