package com.ignis.cleanbydemandmobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ClientMyInfoFragment extends Fragment {

    public ClientMyInfoFragment() {
        // Required empty public constructor
    }

    SharedPreferences sharedPreferences;

    @BindView(R.id.h_profile)
    CircleImageView h_profile;
    @BindView(R.id.changepic)
    TextView changepic;
    @BindView(R.id.h_username)
    TextView h_username;
    @BindView(R.id.h_email)
    TextView h_email;
    @BindView(R.id.h_contact)
    TextView h_contact;

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_client_my_info, container, false);

        //BackGround booknow = new BackGround();
        // booknow.execute();
        return view;
    }
    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String id = sharedPreferences.getString("id", "");

            String data = "";
            int tmp;


            try {
                URL url = new URL("http://cleanbydemand.com/php/m_function.php");
                String urlParams = "id=" + 4 + "&user_id=" + id;

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
            // Toast.makeText(getActivity(), "" + s, Toast.LENGTH_SHORT).show();

            try {
                JSONArray jsonArray = new JSONArray(s);
                int count = jsonArray.length();


                JSONObject jsonObject = jsonArray.getJSONObject(0);
                h_username.setText(jsonObject.getString("bldg_info"));
                h_email.setText(jsonObject.getString("bldg_info"));
                h_contact.setText(jsonObject.getString("bldg_info"));

                Picasso.with(getActivity())
                        .load("http://www.vaultads.com/wp-content/uploads/2011/03/google-adsense.jpg")
                        .into(h_profile);

            } catch(JSONException er) {

            }


        }
    }
}
