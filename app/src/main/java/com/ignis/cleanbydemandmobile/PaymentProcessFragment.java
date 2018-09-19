package com.ignis.cleanbydemandmobile;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PaymentProcessFragment extends Fragment {


    public PaymentProcessFragment() {
        // Required empty public constructor
    }

    SharedPreferences sharedPreferences;

    @BindView(R.id.contents)
    TextView contents;

    String set_date, set_time, set_address, set_coordinates, set_message, set_cleaner, set_service, set_payment, set_price;

    String cleaninghours;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_process, container, false);
        ButterKnife.bind(this, view);

        try {
            set_service = PublicVariables.B_service;
            set_coordinates = PublicVariables.B_coordinates;
            set_address = PublicVariables.B_address;
            set_date = PublicVariables.B_date;
            set_time = PublicVariables.B_time;
            set_message = PublicVariables.B_message;
            set_cleaner = PublicVariables.B_cleaner;
            set_payment = PublicVariables.B_payment;
            set_price = PublicVariables.B_price;

            contents.setText(set_date + "\n" + set_time + "\n" + set_address + "\n" + set_coordinates + "\n" + set_message
                                     + "\n" + set_cleaner + "\n" + set_payment + "\n" + set_service + "\n" + set_price);


        } catch(Exception ex) {
        }

        return view;
    }


    @OnClick(R.id.book)
    public void book(View view) {
        PaymentProcessFragment.BackGround booknow = new PaymentProcessFragment.BackGround();
        booknow.execute();

    }


    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String id = sharedPreferences.getString("id", "");

            String data = "";
            int tmp;

            if (set_service == "Deluxe Cleaning") {
                cleaninghours = "2";
            } else if (set_service == "Premium Cleaning") {
                cleaninghours = "4";
            } else if (set_service == "Yaya for a day") {
                cleaninghours = "8";
            }

            try {
                URL url = new URL("http://cleanbydemand.com/php/m_function.php");
                String urlParams = "id=" + 3 + "&user_id=" + id + "&bldg_info=" + "Condo/House" + "&type_clean=" + set_service
                                           + "&cleaners=" + set_cleaner + "&hours=" + cleaninghours + "&price=" + set_price
                                           + "&date_time=" + set_date + "&hours_from=" + set_time + "&remarks=" + set_message
                                           + "&location=" + set_address + "&coordinate=" + set_coordinates + "&p_method=" + set_payment;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
                }

                is.close();
                httpURLConnection.disconnect();

                return data;
            } catch(MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch(IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            String err = null;

            Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();

        }
    }


}
