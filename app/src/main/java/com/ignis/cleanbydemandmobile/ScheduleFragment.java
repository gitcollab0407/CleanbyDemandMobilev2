package com.ignis.cleanbydemandmobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScheduleFragment extends Fragment {

    private RecyclerView.LayoutManager layoutManager;

    private List<String> listdata = new ArrayList<>();
    SharedPreferences sharedPreferences;

    RecyclerView recyclerView;
    @BindView(R.id.no_schedule)
    RelativeLayout no_schedule;
    @BindView(R.id.yes_schedule)
    RecyclerView yes_schedule;

    View view;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_schedule, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.yes_schedule);
        recyclerView.setHasFixedSize(true);
        ButterKnife.bind(this, view);

        BackGround booknow = new BackGround();
        booknow.execute();

        return view;
    }

    @OnClick(R.id.clean)
    public void clean(View view) {


        Intent i = new Intent(getContext(), CleanerMapActivity.class);
        startActivity(i);
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
                String urlParams = "id=" + 8 + "&user_id=" + id;

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

            try {
                JSONArray jsonArray = new JSONArray(s);
                int count = jsonArray.length();

                Toast.makeText(getActivity(), "" + s, Toast.LENGTH_SHORT).show();

                for (int v = 0; v < count; v++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(v);
                    listdata.add(jsonObject.getString("transaction_id") + "_-/" +
                                         jsonObject.getString("bldg_info") + "_-/" +
                                         jsonObject.getString("type_clean") + "_-/" +
                                         jsonObject.getString("cleaners") + "_-/" +
                                         jsonObject.getString("hours") + "_-/" +
                                         jsonObject.getString("price") + "_-/" +
                                         jsonObject.getString("date_time") + "_-/" +
                                         jsonObject.getString("time") + "_-/" +
                                         jsonObject.getString("remarks") + "_-/" +
                                         jsonObject.getString("transaction_status") + "_-/" +
                                         jsonObject.getString("payment_status") + "_-/" +
                                         jsonObject.getString("location") + "_-/" +
                                         jsonObject.getString("cleaner") + "_-/"+
                                         jsonObject.getString("payment_method") + " ");

                }

                if (count > 0) {
                    layoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(layoutManager);
                    RecyclerViewAdapterSchedule adapter = new RecyclerViewAdapterSchedule(listdata, getActivity());
                    recyclerView.setAdapter(adapter);

                    no_schedule.setVisibility(view.INVISIBLE);
                    yes_schedule.setVisibility(view.VISIBLE);

                }else{
                    no_schedule.setVisibility(view.VISIBLE);
                    yes_schedule.setVisibility(view.INVISIBLE);
                }


            } catch(JSONException er) {

            }


        }
    }


}
