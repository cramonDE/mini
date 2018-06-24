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
public class FollowFragment extends Fragment {


    VideoInputActivity main;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_follow, container, false);
        //Bind view
        ButterKnife.bind(this, view);
        //Set main activity
        main = ((VideoInputActivity)getActivity());
        Button button = (Button) view.findViewById(R.id.btn_home);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home();
            }
        });

        return view;
    }

    public void home(){
//        main.removeFragment(main.followFragment);
//        main.addFragment(main.homeFragment);
        main.replaceFragment(main.homeFragment);
        main.getFragmentManager().beginTransaction().detach(main.homeFragment).attach(main.homeFragment).commit();
    }
}
