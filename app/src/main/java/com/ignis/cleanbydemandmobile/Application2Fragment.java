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


public class Application2Fragment extends Fragment {


    public Application2Fragment() {
        // Required empty public constructor
    }

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_application2, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.join)
    public void join(View view){

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        Application3Fragment application3Fragment = new Application3Fragment();
        fragmentTransaction.replace(R.id.fragment_container,application3Fragment, null);
        fragmentTransaction.addToBackStack(null).commit();

    }

    @OnClick(R.id.back)
    public void back(View view){
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        Application1Fragment application1Fragment = new Application1Fragment();
        fragmentTransaction.replace(R.id.fragment_container,application1Fragment, null);
        fragmentTransaction.addToBackStack(null).commit();
    }

}
