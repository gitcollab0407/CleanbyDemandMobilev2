package com.ignis.cleanbydemandmobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashFragment extends Fragment {

    public SplashFragment() {
        // Required empty public constructor
    }

    private Animation myFadeInAnimation, myFadeOutAnimation;
    private static int SPLASH_TIME_OUT = 3000;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @BindView(R.id.splashimage) ImageView splashimage;

    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_splash, container, false);


        ButterKnife.bind(this, view);

        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        myFadeInAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fadein);
        myFadeOutAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fadeout);

        splashimage.startAnimation(myFadeInAnimation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String id = sharedPreferences.getString("id", "");
                String role = sharedPreferences.getString("role", "");


                if(id.equals("") && role.equals("")){
                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    Login_SignupHomeFragment login_signupHomeFragment = new Login_SignupHomeFragment();
                    fragmentTransaction.replace(R.id.fragment_container,login_signupHomeFragment, null);
                    fragmentTransaction.commit();
                }else{

                    if (role.equals("user")){
                        Intent i = new Intent(getActivity().getBaseContext(), ClientMainActivityFragment.class);
                        startActivity(i);
                        getActivity().finish();

                    }else if (role.equals("cleaner")){
                        Intent i = new Intent(getActivity().getBaseContext(), CleanerMapActivity.class);
                        startActivity(i);
                        getActivity().finish();
                    }
                }

            }
        },SPLASH_TIME_OUT);


        return  view;
    }

}
