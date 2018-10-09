package com.ignis.cleanbydemandmobile;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;


public class BookingFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public BookingFragment() {
        // Required empty public constructor
    }

    int day, month, year, hour, minute;
    int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @BindView(R.id.datepickercontent)
    TextView datepickercontent;
    @BindView(R.id.locationcontent)
    TextView locationcontent;
    @BindView(R.id.messagecontent)
    TextView messagecontent;
    @BindView(R.id.cleanercontent)
    TextView cleanercontent;
    @BindView(R.id.service)
    TextView service;

    @BindView(R.id.cash)
    Button cash;

    @BindView(R.id.card)
    Button card;

    String set_date, set_time, set_address, set_coordinates, set_message, set_cleaner, set_service, payment;
    DatePickerDialog datePickerDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        ButterKnife.bind(this, view);

        try {
            set_service = PublicVariables.B_service;
            set_coordinates = PublicVariables.B_coordinates;
            set_address = PublicVariables.B_address;
            set_date = PublicVariables.B_date;
            set_time = PublicVariables.B_time;
            set_message = PublicVariables.B_message;
            set_cleaner = PublicVariables.B_cleaner;

            if (set_service == "Deluxe Cleaning") {
                PublicVariables.B_price = "400";
            } else if (set_service == "Premium Cleaning") {
                PublicVariables.B_price = "800";
            } else if (set_service == "Yaya for a day") {
                PublicVariables.B_price = "1600";
            }

            service.setText(set_service);
            locationcontent.setText(set_address);
            datepickercontent.setText(set_date + " " + set_time);
            messagecontent.setText(set_message);
            cleanercontent.setText(set_cleaner);
            messagecontent.setMovementMethod(new ScrollingMovementMethod());
            locationcontent.setMovementMethod(new ScrollingMovementMethod());

        } catch(Exception ex) {
        }


        return view;
    }

    @OnFocusChange(R.id.messagecontent)
    public void setMessagecontent(View view) {
        PublicVariables.B_message = messagecontent.getText().toString();
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

        datepickercontent.setText(monthFinal + "-" + dayFinal + "-" + yearFinal + " " + aTime);


        PublicVariables.B_date = yearFinal + "-" + monthFinal + "-" + dayFinal;
        PublicVariables.B_time = aTime;

    }

    @OnClick(R.id.datepicker)
    public void datepicker(View view) {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getActivity()
                , this, year, month, day + 1);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1));
        datePickerDialog.show();

    }


    @OnClick(R.id.location)
    public void location(View view) {
        try {

            ((ClientMainActivityFragment) getActivity()).action_title.setText("Location");
        } catch(Exception ex) {
        }
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        GetLocationFragment getLocationFragment = new GetLocationFragment();
        fragmentTransaction.replace(R.id.fragment_container, getLocationFragment, null);
        fragmentTransaction.addToBackStack(null).commit();

    }

    @OnClick(R.id.increase)
    public void increase(View view) {
        cleanercontent.setText("" + (Integer.parseInt(cleanercontent.getText().toString()) + 1));
        PublicVariables.B_cleaner = cleanercontent.getText().toString();

    }

    @OnClick(R.id.decrease)
    public void decrease(View view) {
        if (Integer.parseInt(cleanercontent.getText().toString()) > 1) {
            cleanercontent.setText("" + (Integer.parseInt(cleanercontent.getText().toString()) - 1));
            PublicVariables.B_cleaner = cleanercontent.getText().toString();

        }
    }

    @OnClick(R.id.cash)
    public void cash(View view) {
        payment = "cash";
        PublicVariables.B_payment = "CASH";
        cash.setBackgroundResource(R.drawable.notes);
        card.setBackgroundResource(R.drawable.cardb);
    }

    @OnClick(R.id.card)
    public void card(View view) {
        payment = "card";
        PublicVariables.B_payment = "DRAGON PAY";
        cash.setBackgroundResource(R.drawable.notesb);
        card.setBackgroundResource(R.drawable.card);

    }

    @OnClick(R.id.casht)
    public void casht(View view) {
        payment = "cash";
        PublicVariables.B_payment = "CASH";
        cash.setBackgroundResource(R.drawable.notes);
        card.setBackgroundResource(R.drawable.cardb);
    }

    @OnClick(R.id.cardt)
    public void cardt(View view) {
        payment = "card";
        PublicVariables.B_payment = "DRAGON PAY";
        cash.setBackgroundResource(R.drawable.notesb);
        card.setBackgroundResource(R.drawable.card);

    }

    @OnClick(R.id.next)
    public void next(View view) {

        try {
            if (!datepickercontent.getText().toString().isEmpty()
                        && !locationcontent.getText().toString().isEmpty()
                        && !payment.isEmpty()) {
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();

                PublicVariables.B_price = ("" + (Integer.parseInt(PublicVariables.B_price) * Integer.parseInt(PublicVariables.B_cleaner)));

                ((ClientMainActivityFragment) getActivity()).action_title.setText("Booking Summary");

                PaymentProcessFragment paymentProcessFragment = new PaymentProcessFragment();
                fragmentTransaction.replace(R.id.fragment_container, paymentProcessFragment, null);
                fragmentTransaction.addToBackStack(null).commit();

            }
        } catch(Exception ex) {
            if (!datepickercontent.getText().toString().isEmpty()
                        && !locationcontent.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please select payment method", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(), "Please complete booking details", Toast.LENGTH_SHORT).show();

            }
        }

    }

}
