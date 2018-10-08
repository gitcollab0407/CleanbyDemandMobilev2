package com.ignis.cleanbydemandmobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apply_as_cleaner, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.next)
    public void next(View view) {
        if(!firstname.getText().toString().isEmpty() && !email.getText().toString().isEmpty()
                   && !contact.getText().toString().isEmpty() && !lastname.getText().toString().isEmpty()
                   && !password.getText().toString().isEmpty()) {

            fragmentManager = getFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();

            ApplyAsCleanerFragment2 applyAsCleanerFragment2 = new ApplyAsCleanerFragment2();
            fragmentTransaction.replace(R.id.fragment_container, applyAsCleanerFragment2, null);
            fragmentTransaction.addToBackStack(null).commit();

        }else {
            Toast.makeText(getActivity(), "Please fill-up the information first", Toast.LENGTH_SHORT).show();
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
