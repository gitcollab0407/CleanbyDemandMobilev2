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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_process, container, false);
        ButterKnife.bind(this, view);

        try {
            Bundle arguments = getArguments();
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
                                     + "\n" + set_cleaner + "\n" + set_payment + "\n" + set_service + "\n"+ set_price);


        } catch(Exception ex) {
        }

        return view;
    }


    @OnClick(R.id.book)
    public void book(View view) {
        PaymentProcessFragment.BackGround booknow = new PaymentProcessFragment.BackGround();
        booknow.execute(set_date, set_time, set_address, set_coordinates, set_message, set_cleaner, set_service, set_payment);
//comme

    }


    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String id = sharedPreferences.getString("id", "");

            String set_date_time1 = params[0],
                    set_address1 = params[1],
                    set_coordinates1 = params[2],
                    set_message1 = params[3],
                    set_cleaner1 = params[4],
                    set_service1 = params[5],
                    set_payment1 = params[6];


            String data = "";
            int tmp;

            try {
                URL url = new URL("http://cleanbydemand.com/php/m_function.php");
                String urlParams = "id=" + 3 + "&user_id=" + id + "&bldg_info=" + set_address + "&type_clean=" + set_service
                                           + "&cleaners=" + set_cleaner + "&hours=" + set_time + "&price=" + set_price
                                           + "&date_time=" + set_cleaner + "&hours_from=" + set_cleaner + "&remarks=" + set_cleaner
                                           + "&p_method=" + set_cleaner + "&p_method=" + set_cleaner;

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
