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

class RecyclerViewHolder1 extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    public TextView a_username;
    public TextView a_service;
    public TextView a_date;
    public TextView a_time;

    private HistoryItemClickListener historyItemClickListener;

    public RecyclerViewHolder1(View itemView) {
        super(itemView);
        a_username = (TextView) itemView.findViewById(R.id.a_username);
        a_service = (TextView) itemView.findViewById(R.id.a_service);
        a_date = (TextView) itemView.findViewById(R.id.a_date);
        a_time = (TextView) itemView.findViewById(R.id.a_time);

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

    }

    public void setItemClickListener(HistoryItemClickListener HistoryItemClickListener) {
        this.historyItemClickListener = HistoryItemClickListener;

    }

    @Override
    public void onClick(View view) {
        historyItemClickListener.OnClick(view, getAdapterPosition(), false);
    }

    @Override
    public boolean onLongClick(View view) {

        historyItemClickListener.OnClick(view, getAdapterPosition(), true);
        return true;
    }
}

public class RecyclerViewAdapterHistory extends RecyclerView.Adapter<RecyclerViewHolder1> {

    private List<String> listData = new ArrayList<>();
    private Context context;

    public RecyclerViewAdapterHistory(List<String> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.recyclerview_hisoty, parent, false);
        return new RecyclerViewHolder1(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder1 holder, int position) {
      /*  String[] separated = listData.get(position).split("|");

        String username = separated[0];

        String service = separated[1];

        String date = separated[2];

        String time = separated[3];

        holder. a_username.setText(username);

        holder. a_service.setText(service);

        holder. a_date.setText(date);

        holder. a_time.setText(time);*/
        holder.a_username.setText(listData.get(position));

        holder.setItemClickListener(new HistoryItemClickListener() {
            @Override
            public void OnClick(View view, int position, boolean isLongClick) {
           /*     if(isLongClick) {
                    Toast.makeText(context, "long click" + listData.get(position), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "click" + listData.get(position), Toast.LENGTH_SHORT).show();

                }*/

                try {
                    android.support.v7.app.AlertDialog.Builder mBuilder = new android.support.v7.app.AlertDialog.Builder(context);
                    View mView =  LayoutInflater.from(context).inflate(R.layout.dialog_history_info, null);
                    TextView messagecontent = (TextView) mView.findViewById(R.id.d_message_content);
                    TextView addresscontent = (TextView) mView.findViewById(R.id.d_address_content);
                    mBuilder.setView(mView);
                    final android.support.v7.app.AlertDialog dialog = mBuilder.create();
                    dialog.show();

                    messagecontent.setMovementMethod(new ScrollingMovementMethod());
                    addresscontent.setMovementMethod(new ScrollingMovementMethod());

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