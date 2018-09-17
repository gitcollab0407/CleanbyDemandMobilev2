package com.ignis.cleanbydemandmobile;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Login_SignupHomeFragment extends Fragment {


    public Login_SignupHomeFragment() {
        // Required empty public constructor
    }

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @BindView(R.id.email) EditText email;
    @BindView(R.id.password) EditText password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_login__signup_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.signup)
    public void signup(View view){

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        SignupFragment signupFragment = new SignupFragment();
        fragmentTransaction.replace(R.id.fragment_container,signupFragment, null);
        fragmentTransaction.addToBackStack(null).commit();
    }

    @OnClick(R.id.signin)
    public void signin(View view){
        Toast.makeText(getActivity(), "asdasdasdasd", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.applyascleaner)
    public void applyascleaner(View view){

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        ApplyAsCleanerFragment applyAsCleanerFragment = new ApplyAsCleanerFragment();
        fragmentTransaction.replace(R.id.fragment_container,applyAsCleanerFragment, null);
        fragmentTransaction.addToBackStack(null).commit();
    }


}
