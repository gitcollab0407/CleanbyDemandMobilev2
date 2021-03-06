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

    @BindView(R.id.emergency) EditText emergency;
    @BindView(R.id.phone) EditText phone;
    @BindView(R.id.language) EditText language;
    @BindView(R.id.emp_history) EditText emp_history;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apply_as_cleaner_fragment4, container, false);

        ButterKnife.bind(this, view);

        try {
            emergency.setText(PublicVariables.a_em_name);
            phone.setText(PublicVariables.a_em_contact);
            language.setText(PublicVariables.a_language);
            emp_history.setText( PublicVariables.a_emp_history);

        }catch(Exception e){}

        return view;
    }

    @OnClick(R.id.next)
    public void next(View view){
        try {
            if (!emergency.getText().toString().isEmpty() && !phone.getText().toString().isEmpty()

                        && !language.getText().toString().isEmpty()
                        && !emp_history.getText().toString().isEmpty()) {


                PublicVariables.a_em_name = emergency.getText().toString();
                PublicVariables.a_em_contact = phone.getText().toString();
                PublicVariables.a_language = language.getText().toString();
                PublicVariables.a_emp_history = emp_history.getText().toString();

                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();

                ApplyAsCleanerFragment5 ApplyAsCleanerFragment5 = new ApplyAsCleanerFragment5();
                fragmentTransaction.replace(R.id.fragment_container, ApplyAsCleanerFragment5, null);
                fragmentTransaction.commit();

            } else {
                Toast.makeText(getActivity(), "Please fill-up the information first", Toast.LENGTH_SHORT).show();
            }

        } catch(Exception e) {
        }
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
