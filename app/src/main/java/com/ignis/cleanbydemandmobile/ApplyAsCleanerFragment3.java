package com.ignis.cleanbydemandmobile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ApplyAsCleanerFragment3 extends Fragment {


    public ApplyAsCleanerFragment3() {
        // Required empty public constructor
    }

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

/*    @BindView(R.id.firstname) EditText firstname;
    @BindView(R.id.lastname) EditText lastname;
    @BindView(R.id.contact) EditText contact;
    @BindView(R.id.email) EditText email;*/
    @BindView(R.id.civil) Spinner spinner_civil;
    @BindView(R.id.sex) Spinner spinner_sex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apply_as_cleaner_fragment3, container, false);

        ButterKnife.bind(this, view);


        final String[] civillist = getResources().getStringArray(R.array.civil);
        final ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(
                getActivity(),android.R.layout.simple_spinner_item,civillist){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_civil.setAdapter(spinnerArrayAdapter1);

        final String[] sexlist = getResources().getStringArray(R.array.sex);
        final ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(
                getActivity(),android.R.layout.simple_spinner_item,sexlist){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_sex.setAdapter(spinnerArrayAdapter2);

        return view;
    }

    @OnClick(R.id.next)
    public void next(View view){
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        ApplyAsCleanerFragment4 applyAsCleanerFragment4 = new ApplyAsCleanerFragment4();
        fragmentTransaction.replace(R.id.fragment_container, applyAsCleanerFragment4, null);
        fragmentTransaction.addToBackStack(null).commit();
    }

    @OnClick(R.id.back)
    public void back(View view){

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        ApplyAsCleanerFragment2 applyAsCleanerFragment2 = new ApplyAsCleanerFragment2();
        fragmentTransaction.replace(R.id.fragment_container, applyAsCleanerFragment2, null);
        fragmentTransaction.addToBackStack(null).commit();

    }

}
