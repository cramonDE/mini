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

public class PreviewFragment extends Fragment {
    VideoInputActivity main;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_preview, container, false);
        //Bind view
        ButterKnife.bind(this, view);
        //Set main activity
        main = ((VideoInputActivity)getActivity());
        final Button cancel = (Button) view.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

        final Button post = (Button) view.findViewById(R.id.btn_post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post();
            }
        });

        return view;
    }

    public void cancel(){
//        main.removeFragment(main.previewFragment);
//        main.addFragment(main.readyFragment);
        main.replaceFragment(main.readyFragment);
    }

    public void post(){
//        main.removeFragment(main.previewFragment);
//        main.addFragment(main.postFragment);
        main.replaceFragment(main.postFragment);
    }
}
