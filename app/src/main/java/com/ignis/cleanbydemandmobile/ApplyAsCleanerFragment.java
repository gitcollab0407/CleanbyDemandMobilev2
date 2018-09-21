package com.ignis.cleanbydemandmobile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ApplyAsCleanerFragment extends Fragment {


    public ApplyAsCleanerFragment() {
        // Required empty public constructor
    }

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @BindView(R.id.username) EditText username;
    @BindView(R.id.contact) EditText contact;
    @BindView(R.id.email) EditText email;
    @BindView(R.id.address) EditText address;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apply_as_cleaner, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.next)
    public void next(View view){
        try {
            PublicVariables.a_username = username.getText().toString();
            PublicVariables.a_email = email.getText().toString();
            PublicVariables.a_contact = contact.getText().toString();
            PublicVariables.a_address = address.getText().toString();


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

                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    Application1Fragment application1Fragment = new Application1Fragment();
                    fragmentTransaction.replace(R.id.fragment_container,application1Fragment, null);
                    fragmentTransaction.commit();

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

}
