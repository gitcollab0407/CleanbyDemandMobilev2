package com.ignis.cleanbydemandmobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.method.ScrollingMovementMethod;
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


public class SignupFragment extends Fragment {


    public SignupFragment() {
        // Required empty public constructor
    }

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @BindView(R.id.firstname) EditText firstname;
    @BindView(R.id.lastname) EditText lastname;
    @BindView(R.id.contact) EditText contact;
    @BindView(R.id.email) EditText email;
    @BindView(R.id.password) EditText password;
    SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        ButterKnife.bind(this, view);
        return  view;
    }

    @OnClick(R.id.signup)
    public void signup(View view){
        try {
            android.support.v7.app.AlertDialog.Builder mBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity());
            View mView = getLayoutInflater().inflate(R.layout.dialog_tems_and_condition, null);

            TextView messagecontent = (TextView) mView.findViewById(R.id.d_message_content);
            TextView accept = (TextView) mView.findViewById(R.id.accept);

            mBuilder.setView(mView);
            final android.support.v7.app.AlertDialog dialog = mBuilder.create();
            dialog.show();

            messagecontent.setMovementMethod(new ScrollingMovementMethod());

            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.hide();

                    if(firstname.getText().toString() !="" && lastname.getText().toString() != ""
                            && contact.getText().toString() != "" && email.getText().toString() !=""
                            && password.getText().toString() != "") {

                        BackGround signup = new BackGround();
                        signup.execute(firstname.getText().toString() + "," + lastname.getText().toString(), email.getText().toString(), password.getText().toString(), email.getText().toString(), contact.getText().toString());
//comment moto
                        progressDialog.setMessage("Signing up");
                        progressDialog.show();

                    }
                }
            });

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                }
            });

        } catch(Exception e) {
        }
    }

    @OnClick(R.id.back)
    public void back(View view){
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        Login_SignupHomeFragment login_signupHomeFragment = new Login_SignupHomeFragment();
        fragmentTransaction.replace(R.id.fragment_container,login_signupHomeFragment, null);
        fragmentTransaction.addToBackStack(null).commit();
    }

    //function from server

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String name = params[0];
            String username = params[1];
            String password = params[2];
            String email = params[3];
            String cp_number = params[4];
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://cleanbydemand.com/php/m_function.php");
                String urlParams = "id=" + 1 + "&name=" + name + "&username=" + username + "&password=" + password
                        + "&email=" + email + "&cp_number=" + cp_number+ "&role=" + "user";

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
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }
        @Override
        protected void onPostExecute(String s) {
            String err = null;
            progressDialog.dismiss();
            if(!s.contains("Username already exist")) {


                Intent i = new Intent(getActivity().getBaseContext(), Login_SignupActivity.class);
                startActivity(i);

                getActivity().finish();

            }else{
                Toast.makeText(getActivity(),s.trim(),Toast.LENGTH_LONG).show();
            }


        }
    }

}
