package com.ignis.cleanbydemandmobile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

              /*  if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                    overridePendingTransition(R.anim.fadein_activity, R.anim.fadeout_activity);
                } else {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    overridePendingTransition(R.anim.fadein_activity, R.anim.fadeout_activity);
                }*/

                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                Login_SignupHomeFragment login_signupHomeFragment = new Login_SignupHomeFragment();
                fragmentTransaction.replace(R.id.fragment_container,login_signupHomeFragment, null);
                fragmentTransaction.addToBackStack(null).commit();
            }
        },SPLASH_TIME_OUT);


        return  view;
    }

}
