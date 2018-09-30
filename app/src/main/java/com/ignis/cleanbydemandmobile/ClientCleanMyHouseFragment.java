package com.ignis.cleanbydemandmobile;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import butterknife.ButterKnife;
import butterknife.OnClick;


public class ClientCleanMyHouseFragment extends Fragment {

    public ClientCleanMyHouseFragment() {
        // Required empty public constructor
    }

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_clean_my_house, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.deluxecleaning)
    public void deluxecleaning(View view) {
        try {
            android.support.v7.app.AlertDialog.Builder mBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity());
            View mView = getLayoutInflater().inflate(R.layout.dialog_deluxe_info, null);

            TextView messagecontent = (TextView) mView.findViewById(R.id.d_message_content);
            TextView booknow = (TextView) mView.findViewById(R.id.d_booknow);

            mBuilder.setView(mView);
            final android.support.v7.app.AlertDialog dialog = mBuilder.create();
            dialog.show();

            messagecontent.setMovementMethod(new ScrollingMovementMethod());

            booknow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.hide();
                    hidenavbar();

                    try {

                        ((ClientMainActivityFragment) getActivity()).action_title.setText("Booking");
                    }catch(Exception ex){

                    }
                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();

                    BookingFragment bookingFragment = new BookingFragment();

                    PublicVariables.B_service ="Deluxe Cleaning";
                    PublicVariables.B_price = "400";

                    fragmentTransaction.replace(R.id.fragment_container,bookingFragment, null);
                    fragmentTransaction.addToBackStack(null).commit();
                }
            });

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    hidenavbar();
                }
            });

        } catch(Exception e) {
        }


    }

    @OnClick(R.id.premiumcleaning)
    public void premiumcleaning(View view) {
        try {
            android.support.v7.app.AlertDialog.Builder mBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity());
            View mView = getLayoutInflater().inflate(R.layout.dialog_premium_info, null);

            TextView messagecontent = (TextView) mView.findViewById(R.id.d_message_content);
            TextView booknow = (TextView) mView.findViewById(R.id.d_booknow);

            mBuilder.setView(mView);
            final android.support.v7.app.AlertDialog dialog = mBuilder.create();
            dialog.show();

            messagecontent.setMovementMethod(new ScrollingMovementMethod());

            booknow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.hide();
                    hidenavbar();

                    try {

                        ((ClientMainActivityFragment) getActivity()).action_title.setText("Booking");
                    }catch(Exception ex){

                    }
                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();

                    BookingFragment bookingFragment = new BookingFragment();

                    PublicVariables.B_service ="Premium Cleaning";
                    PublicVariables.B_price = "800";

                    fragmentTransaction.replace(R.id.fragment_container,bookingFragment, null);
                    fragmentTransaction.addToBackStack(null).commit();
                }
            });

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    hidenavbar();
                }
            });

        } catch(Exception e) {
        }


    }

    @OnClick(R.id.yayaforaday)
    public void yayaforaday(View view)  {
        try {
            android.support.v7.app.AlertDialog.Builder mBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity());
            View mView = getLayoutInflater().inflate(R.layout.dialog_yaya_info, null);

            TextView messagecontent = (TextView) mView.findViewById(R.id.d_message_content);
            TextView booknow = (TextView) mView.findViewById(R.id.d_booknow);

            mBuilder.setView(mView);
            final android.support.v7.app.AlertDialog dialog = mBuilder.create();
            dialog.show();

            messagecontent.setMovementMethod(new ScrollingMovementMethod());

            booknow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.hide();
                    hidenavbar();

                    try {

                        ((ClientMainActivityFragment) getActivity()).action_title.setText("Booking");
                    }catch(Exception ex){

                    }
                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();

                    BookingFragment bookingFragment = new BookingFragment();

                    PublicVariables.B_service = "Yaya for a day";
                    PublicVariables.B_price = "1600";

                    fragmentTransaction.replace(R.id.fragment_container,bookingFragment, null);
                    fragmentTransaction.addToBackStack(null).commit();
                }
            });

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    hidenavbar();
                }
            });

        } catch(Exception e) {
        }
        
    }

    private void hidenavbar() {

        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }
}
