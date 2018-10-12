package com.ignis.cleanbydemandmobile;

import android.app.ProgressDialog;
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


public class Login_SignupHomeFragment extends Fragment {


    public Login_SignupHomeFragment() {
        // Required empty public constructor
    }

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;

    String role;


    private ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login__signup_home, container, false);
        ButterKnife.bind(this, view);

        progressDialog = new ProgressDialog(getActivity());


        return view;
    }

    @OnClick(R.id.signup)
    public void signup(View view) {

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        SignupFragment signupFragment = new SignupFragment();
        fragmentTransaction.replace(R.id.fragment_container, signupFragment, null);
        fragmentTransaction.addToBackStack(null).commit();
    }

    @OnClick(R.id.signin)
    public void signin(View view) {
        BackGround signin = new BackGround();
        signin.execute(email.getText().toString(), password.getText().toString());
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
    }

    @OnClick(R.id.applyascleaner)
    public void applyascleaner(View view) {

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        ApplyAsCleanerFragment applyAsCleanerFragment = new ApplyAsCleanerFragment();
        fragmentTransaction.replace(R.id.fragment_container, applyAsCleanerFragment, null);
        fragmentTransaction.addToBackStack(null).commit();
    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            String password = params[1];
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://cleanbydemand.com/php/m_function.php");
                String urlParams = "id=" + 2 + "&username=" + username + "&password=" + password;

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
            if (!s.contains("Invalid username or password")) {
               // Toast.makeText(getActivity(), "" + s, Toast.LENGTH_SHORT).show();
                String[] value = s.split(",");

                if (value[6].equals("user")) {


                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    try {
                        editor.putString("username", value[0] + " " + value[1]);
                        editor.putString("email", value[2]);
                        editor.putString("contact", value[5]);
                        editor.putString("id", value[7]);
                        editor.putString("role", value[6]);
                        editor.putString("profile", "http://cleanbydemand.com/php/profile/"+value[8]);

                        editor.putString("address", value[9]);
                        editor.putString("rating", value[10]);

                    } catch(Exception e) {
                    }


                    editor.commit();

                    Intent i = new Intent(getActivity().getBaseContext(), ClientMainActivityFragment.class);
                    startActivity(i);
                    getActivity().finish();


                } else if (value[6].equals("cleaner")) {

                    if(value[11].trim().equals("Activate")) {

                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        try {
                            editor.putString("username", value[0] + " " + value[1]);
                            editor.putString("email", value[2]);
                            editor.putString("contact", value[5]);
                            editor.putString("id", value[7]);
                            editor.putString("role", value[6]);
                            editor.putString("profile", "http://cleanbydemand.com/php/profile/"+value[8]);

                            editor.putString("address", value[9]);
                            editor.putString("rating", value[10]);

                        } catch(Exception e) {
                        }


                        editor.commit();

                        Intent i = new Intent(getActivity().getBaseContext(), CleanerMapActivity.class);
                        startActivity(i);
                        getActivity().finish();


                    }else{
                        fragmentManager = getFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        Application3Fragment application3Fragment = new Application3Fragment();
                        fragmentTransaction.replace(R.id.fragment_container, application3Fragment, null);
                        fragmentTransaction.addToBackStack(null).commit();
                    }

                }


            } else {
                Toast.makeText(getActivity(), s.trim(), Toast.LENGTH_LONG).show();
            }

        }
    }

}
