package com.ignis.cleanbydemandmobile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ApplyAsCleanerFragment4 extends Fragment {


    public ApplyAsCleanerFragment4() {
        // Required empty public constructor
    }

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

  /*  @BindView(R.id.firstname) EditText firstname;
    @BindView(R.id.lastname) EditText lastname;
    @BindView(R.id.contact) EditText contact;
    @BindView(R.id.email) EditText email;
    @BindView(R.id.password) EditText password;*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apply_as_cleaner_fragment4, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.next)
    public void next(View view){
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        ApplyAsCleanerFragment5 applyAsCleanerFragment5 = new ApplyAsCleanerFragment5();
        fragmentTransaction.replace(R.id.fragment_container, applyAsCleanerFragment5, null);
        fragmentTransaction.addToBackStack(null).commit();
    }

    @OnClick(R.id.back)
    public void back(View view){

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        ApplyAsCleanerFragment3 applyAsCleanerFragment3 = new ApplyAsCleanerFragment3();
        fragmentTransaction.replace(R.id.fragment_container, applyAsCleanerFragment3, null);
        fragmentTransaction.addToBackStack(null).commit();

    }

}
