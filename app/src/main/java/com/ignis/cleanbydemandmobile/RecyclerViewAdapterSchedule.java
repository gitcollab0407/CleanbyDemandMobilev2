package com.ignis.cleanbydemandmobile;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

        String[] value = listData.get(position).split("/");
        holder.a_username.setText(value[0]);
        holder.a_service.setText(value[1]);
        holder.a_date.setText(value[2]);
        holder.a_time.setText(value[3]);
     // holder.a_username.setText(listData.get(position));

        holder.setItemClickListener(new ScheduleItemClickListener() {
            @Override
            public void OnClick(View view, int position, boolean isLongClick) {
                try {
                    android.support.v7.app.AlertDialog.Builder mBuilder = new android.support.v7.app.AlertDialog.Builder(context);
                    View mView =  LayoutInflater.from(context).inflate(R.layout.dialog_schedule_info, null);
                    ImageView call = (ImageView) mView.findViewById(R.id.call);
                    TextView messagecontent = (TextView) mView.findViewById(R.id.d_message_content);
                    TextView addresscontent = (TextView) mView.findViewById(R.id.d_address_content);
                    mBuilder.setView(mView);
                    final android.support.v7.app.AlertDialog dialog = mBuilder.create();
                    dialog.show();

                    messagecontent.setMovementMethod(new ScrollingMovementMethod());
                    addresscontent.setMovementMethod(new ScrollingMovementMethod());

                    call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(context, "asdasd", Toast.LENGTH_SHORT).show();
                            hidenavbar();
                            dialog.hide();
                        }
                    });

                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            hidenavbar();
                        }
                    });

                }catch(Exception e){}
            }
        });
    }
    private void hidenavbar() {

        ((Activity) context).getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }



    @Override
    public int getItemCount() {
        return listData.size();
    }
}