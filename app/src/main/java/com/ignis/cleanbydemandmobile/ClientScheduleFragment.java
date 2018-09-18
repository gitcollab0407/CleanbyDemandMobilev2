package com.ignis.cleanbydemandmobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClientScheduleFragment extends Fragment {

    private RecyclerView.LayoutManager layoutManager;
    private List<String> listdata = new ArrayList<>();

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;


    public ClientScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_schedule, container, false);

        ButterKnife.bind(this, view);
        setupList();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.client_recyclerview_schedule);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        ClientRecyclerViewAdapterSchedule adapter = new ClientRecyclerViewAdapterSchedule(listdata, getActivity());
        recyclerView.setAdapter(adapter);
        return view;
    }

    @OnClick(R.id.clean)
    public void clean(View view){

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        ClientCleanMyHouseFragment clientCleanMyHouseFragment = new ClientCleanMyHouseFragment();
        fragmentTransaction.replace(R.id.fragment_container,clientCleanMyHouseFragment, null);
        fragmentTransaction.addToBackStack(null).commit();

    }


    private void setupList() {
        for (int i = 1; i <= 10; i++) {
            //listdata.add("kindred" + i + "|" + "service" + "|" +"Sep,03,2018" +"|" + "10:30 AM");
            listdata.add("kindred" + i);
        }
    }


}
