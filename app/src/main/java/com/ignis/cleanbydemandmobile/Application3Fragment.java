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


import butterknife.ButterKnife;
import butterknife.OnClick;


public class Application3Fragment extends Fragment {


    public Application3Fragment() {
        // Required empty public constructor
    }
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_application3, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

/*
    @OnClick(R.id.join)
    public void join(View view){

        String username = PublicVariables.a_username;
        String email = PublicVariables.a_email;
        String contact = PublicVariables.a_contact;
        String address = PublicVariables.a_address;

        */
/*
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        Application1Fragment application1Fragment = new Application1Fragment();
        fragmentTransaction.replace(R.id.fragment_container,application1Fragment, null);
        fragmentTransaction.addToBackStack(null).commit();
*//*

    }
*/


}
