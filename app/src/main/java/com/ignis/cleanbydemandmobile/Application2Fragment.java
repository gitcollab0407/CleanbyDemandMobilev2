package com.ignis.cleanbydemandmobile;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class Application2Fragment extends Fragment {


    public Application2Fragment() {
        // Required empty public constructor
    }

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_application2, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.join)
    public void join(View view) {

        String username = PublicVariables.a_username;
        String email = PublicVariables.a_email;
        String contact = PublicVariables.a_contact;
        String address = PublicVariables.a_address;

        BackGround signup = new BackGround();
        signup.execute(username, email, contact, address);


    }

    @OnClick(R.id.back)
    public void back(View view) {
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        Application1Fragment application1Fragment = new Application1Fragment();
        fragmentTransaction.replace(R.id.fragment_container, application1Fragment, null);
        fragmentTransaction.addToBackStack(null).commit();
    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            String email = params[1];
            String contact = params[2];
            String address = params[3];
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://cleanbydemand.com/php/m_function.php");
                String urlParams = "id=" + 1 + "&name=" + username + "&email=" + email + "&contact=" + contact
                                           + "&address=" + address;

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

            fragmentManager = getFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            Application3Fragment application3Fragment = new Application3Fragment();
            fragmentTransaction.replace(R.id.fragment_container, application3Fragment, null);
            fragmentTransaction.addToBackStack(null).commit();


        }
    }

}
