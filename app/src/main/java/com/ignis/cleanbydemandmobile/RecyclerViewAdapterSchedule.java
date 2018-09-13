package com.ignis.cleanbydemandmobile;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    public TextView a_username;
    public TextView a_service;
    public TextView a_date;
    public TextView a_time;

    private ScheduleItemClickListener scheduleItemClickListener;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        a_username = (TextView) itemView.findViewById(R.id.a_username);
        a_service = (TextView) itemView.findViewById(R.id.a_service);
        a_date = (TextView) itemView.findViewById(R.id.a_date);
        a_time = (TextView) itemView.findViewById(R.id.a_time);

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

    }

    public void setItemClickListener(ScheduleItemClickListener scheduleItemClickListener) {
        this.scheduleItemClickListener = scheduleItemClickListener;

    }

    @Override
    public void onClick(View view) {
        scheduleItemClickListener.OnClick(view, getAdapterPosition(), false);
    }

    @Override
    public boolean onLongClick(View view) {

        scheduleItemClickListener.OnClick(view, getAdapterPosition(), true);
        return true;
    }
}

public class RecyclerViewAdapterSchedule extends RecyclerView.Adapter<RecyclerViewHolder> {

    private List<String> listData = new ArrayList<>();
    private Context context;

    public RecyclerViewAdapterSchedule(List<String> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.recyclerview_schedule, parent, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder. a_username.setText(listData.get(position));

        holder.setItemClickListener(new ScheduleItemClickListener() {
            @Override
            public void OnClick(View view, int position, boolean isLongClick) {
                if(isLongClick) {
                    Toast.makeText(context, "long click" + listData.get(position), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "click" + listData.get(position), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}