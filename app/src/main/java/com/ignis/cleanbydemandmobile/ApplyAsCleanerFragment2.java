package com.ignis.cleanbydemandmobile;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ApplyAsCleanerFragment2 extends Fragment implements DatePickerDialog.OnDateSetListener{


    public ApplyAsCleanerFragment2() {
        // Required empty public constructor
    }

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    int day, month, year;
    int dayFinal, monthFinal, yearFinal;


    @BindView(R.id.dateofbirth) TextView dateofbirth;
    @BindView(R.id.weight) EditText weight;
    @BindView(R.id.height) EditText height;
    @BindView(R.id.perm_address) EditText perm_address;
    @BindView(R.id.pres_address) EditText pres_address;

    DatePickerDialog datePickerDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apply_as_cleaner_fragment2, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.next)
    public void next(View view){
        try {
            if(!dateofbirth.getText().toString().isEmpty() && !perm_address.getText().toString().isEmpty()
                       && !height.getText().toString().isEmpty() && !weight.getText().toString().isEmpty()
                       && !pres_address.getText().toString().isEmpty()) {

                PublicVariables.a_date_of_birth = dateofbirth.getText().toString();
                PublicVariables.a_weight = weight.getText().toString();
                PublicVariables.a_perm_address = perm_address.getText().toString();
                PublicVariables.a_height = height.getText().toString();
                PublicVariables.a_pres_address = pres_address.getText().toString();

                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();

                ApplyAsCleanerFragment3 ApplyAsCleanerFragment3 = new ApplyAsCleanerFragment3();
                fragmentTransaction.replace(R.id.fragment_container, ApplyAsCleanerFragment3, null);
                fragmentTransaction.commit();

            }else {
                Toast.makeText(getActivity(), "Please fill-up the information first", Toast.LENGTH_SHORT).show();
            }

        } catch(Exception e) {
        }

    }

    @OnClick(R.id.back)
    public void back(View view){

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        ApplyAsCleanerFragment applyAsCleanerFragment = new ApplyAsCleanerFragment();
        fragmentTransaction.replace(R.id.fragment_container, applyAsCleanerFragment, null);
        fragmentTransaction.addToBackStack(null).commit();

    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        yearFinal = i;
        monthFinal = i1 + 1;
        dayFinal = i2;

        dateofbirth.setText(yearFinal + "-" + monthFinal + "-" + dayFinal);

    }

    @OnClick(R.id.dateofbirth)
    public void datepicker(View view) {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getActivity()
                , this, year, month, day);
        datePickerDialog.show();

    }
}
