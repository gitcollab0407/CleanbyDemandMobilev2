package com.ignis.cleanbydemandmobile;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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


public class ApplyAsCleanerFragment extends Fragment {


    public ApplyAsCleanerFragment() {
        // Required empty public constructor
    }

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @BindView(R.id.firstname)
    EditText firstname;
    @BindView(R.id.lastname)
    EditText lastname;
    @BindView(R.id.contact)
    EditText contact;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;

    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apply_as_cleaner, container, false);

        ButterKnife.bind(this, view);

        progressDialog = new ProgressDialog(getActivity());

        try {
            firstname.setText(PublicVariables.a_firstname);
            lastname.setText(PublicVariables.a_lastname);
            email.setText(PublicVariables.a_email);
            contact.setText( PublicVariables.a_contact);
            password.setText(PublicVariables.a_password);

        }catch(Exception e){}

        return view;
    }

    @OnClick(R.id.next)
    public void next(View view) {

        if(!firstname.getText().toString().isEmpty() && !email.getText().toString().isEmpty()
                   && !contact.getText().toString().isEmpty() && !lastname.getText().toString().isEmpty()
                   && !password.getText().toString().isEmpty()) {

       BackGround signin = new BackGround();
        signin.execute(email.getText().toString());

            progressDialog.setMessage("Authenticating...");
            progressDialog.show();

        }else {
            Toast.makeText(getActivity(), "Please fill-up the information first", Toast.LENGTH_SHORT).show();
        }



    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String email = params[0];
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://cleanbydemand.com/php/m_function.php");
                String urlParams = "id=" + 17 + "&email=" + email;

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
            progressDialog.dismiss();

            if(!s.contains("Username already exists")){
                try {


                        PublicVariables.a_firstname = firstname.getText().toString();
                        PublicVariables.a_lastname = lastname.getText().toString();
                        PublicVariables.a_email = email.getText().toString();
                        PublicVariables.a_contact = contact.getText().toString();
                        PublicVariables.a_password = password.getText().toString();

                        fragmentManager = getFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();

                        ApplyAsCleanerFragment2 ApplyAsCleanerFragment2 = new ApplyAsCleanerFragment2();
                        fragmentTransaction.replace(R.id.fragment_container, ApplyAsCleanerFragment2, null);
                        fragmentTransaction.commit();



                } catch(Exception e) {
                }


            }else{
                Toast.makeText(getActivity(), ""+s, Toast.LENGTH_SHORT).show();
            }


        }

    }

    @OnClick(R.id.back)
    public void back(View view) {

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        Login_SignupHomeFragment login_signupHomeFragment = new Login_SignupHomeFragment();
        fragmentTransaction.replace(R.id.fragment_container, login_signupHomeFragment, null);
        fragmentTransaction.addToBackStack(null).commit();
    }

}
