package com.sh.shvideolibrary.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sh.shvideolibrary.R;
import com.sh.shvideolibrary.VideoInputActivity;

import butterknife.ButterKnife;

public class PreviewFragment extends Fragment {
    VideoInputActivity main;
    public TextView score_tv;
    public TextView combo_tv;
    public TextView catch_tv;
    public TextView percent_tv;

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

        score_tv = (TextView)view.findViewById(R.id.tv_catch);
        combo_tv = (TextView)view.findViewById(R.id.tv_combo);
        catch_tv = (TextView)view.findViewById(R.id.tv_catch);
        percent_tv = (TextView)view.findViewById(R.id.tv_percent);

        return view;
    }

    public void cancel(){
//        main.removeFragment(main.previewFragment);
//        main.addFragment(main.readyFragment);
        main.replaceFragment(main.readyFragment);
        main.getFragmentManager().beginTransaction().detach(main.readyFragment).attach(main.readyFragment).commit();
    }

    public void post(){
//        main.removeFragment(main.previewFragment);
//        main.addFragment(main.postFragment);
        main.replaceFragment(main.postFragment);
        main.getFragmentManager().beginTransaction().detach(main.postFragment).attach(main.postFragment).commit();
    }
}
