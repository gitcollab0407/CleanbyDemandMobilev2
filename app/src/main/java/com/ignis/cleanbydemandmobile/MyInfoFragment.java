package com.ignis.cleanbydemandmobile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyInfoFragment extends Fragment {

    public MyInfoFragment() {
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
    @BindView(R.id.h_address)
    TextView h_address;
    @BindView(R.id.MyRating)
    RatingBar MyRating;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_info, container, false);
        ButterKnife.bind(this, view);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        try {
            h_username.setText(sharedPreferences.getString("username", "").toString().trim());
            h_email.setText("Email: " + sharedPreferences.getString("email", "").toString());
            h_contact.setText("Contact Number: " + sharedPreferences.getString("contact", "").toString());
            h_address.setText("Address: " + sharedPreferences.getString("address", "").toString());

            double rate = Double.parseDouble(sharedPreferences.getString("rating", "").trim());
            int finalrate = (int) rate;
            MyRating.setRating(finalrate);





          /*  Picasso.with(getActivity())
                    .load(sharedPreferences.getString("profile", ""))
                    .into(h_profile);*/

        } catch(Exception e) {
        }

        return view;
    }

    @OnClick(R.id.changepic)
    public void setChangepic(View view) {


    }


}
