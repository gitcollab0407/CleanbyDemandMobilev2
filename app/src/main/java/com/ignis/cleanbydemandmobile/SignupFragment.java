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


public class SignupFragment extends Fragment {


    public SignupFragment() {
        // Required empty public constructor
    }

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @BindView(R.id.username) EditText username;
    @BindView(R.id.contact) EditText contact;
    @BindView(R.id.email) EditText email;
    @BindView(R.id.password) EditText password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        ButterKnife.bind(this, view);
        return  view;
    }

    @OnClick(R.id.signup)
    public void signup(View view){
        Toast.makeText(getActivity(), "signup", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.back)
    public void back(View view){
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        Login_SignupHomeFragment login_signupHomeFragment = new Login_SignupHomeFragment();
        fragmentTransaction.replace(R.id.fragment_container,login_signupHomeFragment, null);
        fragmentTransaction.addToBackStack(null).commit();
    }

}
