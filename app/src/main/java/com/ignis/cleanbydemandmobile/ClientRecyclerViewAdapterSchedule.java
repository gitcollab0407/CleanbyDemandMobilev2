package com.ignis.cleanbydemandmobile;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

class ClientRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    public TextView a_service;
    public TextView a_date;
    public TextView a_time;
    public TextView a_status;

    public CircleImageView a_profile;

     ScheduleItemClickListener scheduleItemClickListener;

    public ClientRecyclerViewHolder(View itemView) {
        super(itemView);
        a_service = (TextView) itemView.findViewById(R.id.a_service);
        a_date = (TextView) itemView.findViewById(R.id.a_date);
        a_time = (TextView) itemView.findViewById(R.id.a_time);
        a_status = (TextView) itemView.findViewById(R.id.a_status);
        a_profile = (CircleImageView) itemView.findViewById(R.id.h_profile);



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
    String name;

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

        String[] value = listData.get(position).split("_-/");
        // Toast.makeText(context, ""+ value.length, Toast.LENGTH_SHORT).show();

        holder.a_service.setText(value[2].trim());
        holder.a_date.setText(value[6].trim());
        holder.a_time.setText(value[7].trim());
        holder.a_status.setText(value[9].trim());

        if(value[9].contains("Cancelled")){
            holder.a_status.setTextColor(Color.parseColor("#f23d3d"));
        }

        if(value[9].contains("Confirmed")){
            holder.a_status.setText("Accepted");
        }

        try {
            if (value[2].trim().contains("Deluxe Cleaning")){
                holder.a_profile.setImageResource(R.drawable.i_deluxe);
            }else if(value[2].trim().contains("Premium Cleaning")){
                holder.a_profile.setImageResource(R.drawable.i_premium);
            }else if(value[2].trim().contains("Yaya for a day")){
                holder.a_profile.setImageResource(R.drawable.i_yaya);
            }

        } catch(Exception e) {

        }

        holder.setItemClickListener(new ScheduleItemClickListener() {
            @Override
            public void OnClick(View view, int position, boolean isLongClick) {
                try {
                    android.support.v7.app.AlertDialog.Builder mBuilder = new android.support.v7.app.AlertDialog.Builder(context);
                    View mView = LayoutInflater.from(context).inflate(R.layout.dialog_client_schedule_info, null);
                    TextView cancel = (TextView) mView.findViewById(R.id.cancelbooking);
                    TextView confirmation = (TextView) mView.findViewById(R.id.confirmation);

                    TextView d_date = (TextView) mView.findViewById(R.id.d_date);
                    TextView d_message_content = (TextView) mView.findViewById(R.id.d_message_content);
                    TextView d_address_content = (TextView) mView.findViewById(R.id.d_address_content);
                    TextView d_cleaner = (TextView) mView.findViewById(R.id.d_cleaner);
                    TextView d_time = (TextView) mView.findViewById(R.id.d_time);
                    TextView d_payment = (TextView) mView.findViewById(R.id.d_payment);
                    TextView d_service = (TextView) mView.findViewById(R.id.d_service);
                    TextView d_price = (TextView) mView.findViewById(R.id.d_price);
                    final TextView reasoncontent = (TextView) mView.findViewById(R.id.reasoncontent);

                    LinearLayout a_profile = (LinearLayout) mView.findViewById(R.id.servicebg);
                    LinearLayout a_profile2 = (LinearLayout) mView.findViewById(R.id.servicebg2);

                    final LinearLayout height = (LinearLayout) mView.findViewById(R.id.height);

                    final LinearLayout first_section = (LinearLayout) mView.findViewById(R.id.first_section);
                    final LinearLayout second_section = (LinearLayout) mView.findViewById(R.id.second_section);

                    String[] value = listData.get(position).split("_-/");

                    final String transaction_id = value[0];
                    String bldg_info = value[1];
                    String type_clean = value[2];
                    String cleaners = value[3];
                    String hours = value[4];
                    String price = value[5];
                    String date_time = value[6];
                    String time = value[7];
                    String remarks = value[8];
                    String transaction_status = value[9];
                    String payment_status = value[10];
                    String location = value[11];
                    String cleaner = value[12];
                    String method = value[13];

                    d_message_content.setText(remarks);
                    d_address_content.setText(location);
                    d_time.setText(" "+time);
                    d_payment.setText(" "+method);
                    d_cleaner.setText(" = "+cleaners);
                    d_service.setText(type_clean +" ("+hours+"Hours)");
                    d_date.setText(" "+date_time);
                    d_price.setText("Price: ₱ " + price);

                    if(method.contains("CASH")) {
                        d_payment.setCompoundDrawablesWithIntrinsicBounds(R.drawable.d_cash, 0, 0, 0);
                    }else{
                        d_payment.setCompoundDrawablesWithIntrinsicBounds(R.drawable.d_credit_card, 0, 0, 0);

                    }

                    try {
                        if (value[2].trim().contains("Deluxe Cleaning")){
                            a_profile.setBackgroundResource(R.drawable.d_deluxe);
                            a_profile2.setBackgroundResource(R.drawable.d_deluxe);
                        }else if(value[2].trim().contains("Premium Cleaning")){
                            a_profile.setBackgroundResource(R.drawable.d_premium);
                            a_profile2.setBackgroundResource(R.drawable.d_premium);
                        }else if(value[2].trim().contains("Yaya for a day")){
                            a_profile.setBackgroundResource(R.drawable.d_yaya);
                            a_profile2.setBackgroundResource(R.drawable.d_yaya);
                        }

                    } catch(Exception e) {

                    }

                    mBuilder.setView(mView);
                    final android.support.v7.app.AlertDialog dialog = mBuilder.create();
                    dialog.show();

                    d_message_content.setMovementMethod(new ScrollingMovementMethod());
                    d_address_content.setMovementMethod(new ScrollingMovementMethod());



                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            first_section.setVisibility(View.INVISIBLE);
                            second_section.setVisibility(View.VISIBLE);
                            ViewGroup.LayoutParams params = height.getLayoutParams();
                            params.height = 100;
                            height.setLayoutParams(params);


                        }
                    });

                    confirmation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(!reasoncontent.getText().toString().trim().equals("")) {
                                BackGround bg =new BackGround();
                                bg.execute(transaction_id,""+reasoncontent.getText());

                                hidenavbar();
                                dialog.hide();

                             /*   ProgressDialog progressDialog = new ProgressDialog(context);
                                progressDialog.setMessage("Cancelling...");
                                progressDialog.show();*/

                            }else{
                                Toast.makeText(context, "Please provide your reason for cancelling your booking", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            hidenavbar();
                        }
                    });

                } catch(Exception e) {
                }
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

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String trans_id = params[0];
            String rfc = params[1];
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://cleanbydemand.com/php/m_function.php");
                String urlParams = "id=" + 7 + "&trans_id=" + trans_id + "&rfc=" + rfc;

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

          /*  ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Cancelling...");
            progressDialog.dismiss();
*/
            Toast.makeText(context, "Cancellation has been processed", Toast.LENGTH_SHORT).show();

            FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            ClientScheduleFragment clientScheduleFragment = new ClientScheduleFragment();
            fragmentTransaction.replace(R.id.fragment_container, clientScheduleFragment, null);
            fragmentTransaction.commit();

        }
    }

}