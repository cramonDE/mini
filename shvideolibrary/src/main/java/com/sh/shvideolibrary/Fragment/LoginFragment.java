package com.sh.shvideolibrary.Fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.sh.shvideolibrary.R;
import com.sh.shvideolibrary.VideoInputActivity;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    ImageButton loginBtn;

    VideoInputActivity main;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        //Bind view
        ButterKnife.bind(this, view);
        //Set main activity
        main = ((VideoInputActivity)getActivity());
        loginBtn = (ImageButton) view.findViewById(R.id.ibt_login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("login button", "onClick: ");
                loginSuccess();
            }
        });
        return view;
    }

    private void loginSuccess(){
        Log.d("log", "loginSuccess: ");
        main.removeFragment(main.loginFragment);
        main.addFragment(main.readyFragment);
    }
}
