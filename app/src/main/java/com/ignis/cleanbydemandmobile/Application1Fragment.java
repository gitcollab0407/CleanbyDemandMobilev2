package com.ignis.cleanbydemandmobile;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Application1Fragment extends Fragment {

    public Application1Fragment() {
        // Required empty public constructor
    }

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @BindView(R.id.info) TextView info;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_application1, container, false);

        ButterKnife.bind(this, view);

        info.setMovementMethod(new ScrollingMovementMethod());
        return view;
    }

    @OnClick(R.id.next)
    public void next(View view){

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        Application2Fragment application2Fragment = new Application2Fragment();
        fragmentTransaction.replace(R.id.fragment_container,application2Fragment, null);
        fragmentTransaction.addToBackStack(null).commit();

    }

    @OnClick(R.id.back)
    public void back(View view){
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        ApplyAsCleanerFragment applyAsCleanerFragment = new ApplyAsCleanerFragment();
        fragmentTransaction.replace(R.id.fragment_container,applyAsCleanerFragment, null);
        fragmentTransaction.addToBackStack(null).commit();
    }

}
