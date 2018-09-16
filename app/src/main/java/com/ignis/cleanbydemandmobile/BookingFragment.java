package com.ignis.cleanbydemandmobile;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BookingFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public BookingFragment() {
        // Required empty public constructor
    }

    int day, month, year, hour, minute;
    int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @BindView(R.id.datepickercontent) TextView datepickercontent;
    @BindView(R.id.locationcontent) TextView locationcontent;
    @BindView(R.id.messagecontent) TextView messagecontent;
    @BindView(R.id.cleanercontent) TextView cleanercontent;
    @BindView(R.id.service) TextView service;


    String set_address, set_coordinates, set_service;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        ButterKnife.bind(this, view);

        try {
            Bundle arguments = getArguments();
            set_address = arguments.getString("address");
            set_coordinates = arguments.getString("coordinates");
            set_service = arguments.getString("service");
            service.setText(set_service);
            locationcontent.setText(set_address);
        }catch(Exception ex){}
        Log.d("fragment_booking", "");

        return view;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        yearFinal = i;
        monthFinal = i1 + 1;
        dayFinal = i2;

        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this,
                hour, minute, android.text.format.DateFormat.is24HourFormat(getActivity()));

        timePickerDialog.show();

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        hourFinal = i;
        minuteFinal = i1;

        String timeSet = "";
        if (i > 12) {
            i -= 12;
            timeSet = "PM";
        } else if (i == 0) {
            i += 12;
            timeSet = "AM";
        } else if (i == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        String minutes = "";
        if (i1 < 10)
            minutes = "0" + i1;
        else
            minutes = String.valueOf(i1);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(i).append(':')
                               .append(minutes).append(" ").append(timeSet).toString();

        datepickercontent.setText(monthFinal + "/" + dayFinal + "/" + yearFinal + " " + aTime);

    }

    @OnClick(R.id.datepicker)
    public void datepicker(View view) {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity()
                , this, year, month, day);
        datePickerDialog.show();

    }

    @OnClick(R.id.location)
    public void location(View view) {
        try {

            ((ClientMainActivityFragment) getActivity()).action_title.setText("Location");
        }catch(Exception ex){
            Toast.makeText(getActivity(), ""+ex, Toast.LENGTH_SHORT).show();
        }
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        GetLocationFragment getLocationFragment = new GetLocationFragment();
        fragmentTransaction.replace(R.id.fragment_container,getLocationFragment, null);
        fragmentTransaction.addToBackStack(null).commit();

    }

    @OnClick(R.id.increase)
    public void increase(View view) {
      cleanercontent.setText(""+(Integer.parseInt(cleanercontent.getText().toString())+1));

    }

    @OnClick(R.id.decrease)
    public void decrease(View view) {
        if(Integer.parseInt(cleanercontent.getText().toString()) >1) {
            cleanercontent.setText(""+(Integer.parseInt(cleanercontent.getText().toString()) - 1));
        }
    }

    @OnClick(R.id.cash)
    public void cash(View view) {
        Toast.makeText(getActivity(), "cash", Toast.LENGTH_SHORT).show();
    }



    @OnClick(R.id.card)
    public void card(View view) {
        Toast.makeText(getActivity(), "credit card", Toast.LENGTH_SHORT).show();
    }


}
