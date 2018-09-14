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

class ClientRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    public TextView a_username;
    public TextView a_service;
    public TextView a_date;
    public TextView a_time;
    public TextView a_status;

    private ScheduleItemClickListener scheduleItemClickListener;

    public ClientRecyclerViewHolder(View itemView) {
        super(itemView);
        a_username = (TextView) itemView.findViewById(R.id.a_username);
        a_service = (TextView) itemView.findViewById(R.id.a_service);
        a_date = (TextView) itemView.findViewById(R.id.a_date);
        a_time = (TextView) itemView.findViewById(R.id.a_time);
        a_status = (TextView) itemView.findViewById(R.id.a_time);

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

public class ClientRecyclerViewAdapterSchedule extends RecyclerView.Adapter<ClientRecyclerViewHolder> {

    private List<String> listData = new ArrayList<>();
    private Context context;

    public ClientRecyclerViewAdapterSchedule(List<String> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @Override
    public ClientRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.clientrecyclerview_schedule, parent, false);
        return new ClientRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientRecyclerViewHolder holder, int position) {

      holder.a_username.setText(listData.get(position));

        holder.setItemClickListener(new ScheduleItemClickListener() {
            @Override
            public void OnClick(View view, int position, boolean isLongClick) {
                try {
                    android.support.v7.app.AlertDialog.Builder mBuilder = new android.support.v7.app.AlertDialog.Builder(context);
                    View mView =  LayoutInflater.from(context).inflate(R.layout.dialog_client_schedule_info, null);
                    TextView call = (TextView) mView.findViewById(R.id.callcleaner);
                    TextView cancel = (TextView) mView.findViewById(R.id.cancelbooking);
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
                            Toast.makeText(context, "call", Toast.LENGTH_SHORT).show();
                            hidenavbar();
                            dialog.hide();
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(context, "cancel", Toast.LENGTH_SHORT).show();
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