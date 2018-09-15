package com.ignis.cleanbydemandmobile;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

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


    @BindView(R.id.datepickercontent) TextView datepickercontent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        ButterKnife.bind(this, view);
        return view;
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
}
