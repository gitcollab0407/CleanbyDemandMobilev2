package com.ignis.cleanbydemandmobile;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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

class RecyclerViewHolder1 extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    public TextView a_service;
    public TextView a_date;
    public TextView a_time;
    public TextView a_status;

    public CircleImageView a_profile;
    private HistoryItemClickListener historyItemClickListener;

    public RecyclerViewHolder1(View itemView) {
        super(itemView);
        a_service = (TextView) itemView.findViewById(R.id.a_service);
        a_date = (TextView) itemView.findViewById(R.id.a_date);
        a_time = (TextView) itemView.findViewById(R.id.a_time);
        a_status = (TextView) itemView.findViewById(R.id.a_status);

        a_profile = (CircleImageView) itemView.findViewById(R.id.h_profile);

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
    String name;

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

        String[] value = listData.get(position).split("_-/");
         //Toast.makeText(context, ""+ value.length, Toast.LENGTH_SHORT).show();

        holder.a_service.setText(value[2].trim());
        holder.a_date.setText(value[6].trim());
        holder.a_time.setText(value[7].trim());
        holder.a_status.setText(value[9].trim());

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

            holder.setItemClickListener(new HistoryItemClickListener() {
            @Override
            public void OnClick(final View view, int position, boolean isLongClick) {
           /*     if(isLongClick) {
                    Toast.makeText(context, "long click" + listData.get(position), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "click" + listData.get(position), Toast.LENGTH_SHORT).show();

                }*/

                try {
                    android.support.v7.app.AlertDialog.Builder mBuilder = new android.support.v7.app.AlertDialog.Builder(context);
                    View mView =  LayoutInflater.from(context).inflate(R.layout.dialog_history_info, null);
                    TextView d_date = (TextView) mView.findViewById(R.id.d_date);

                    TextView d_message_content = (TextView) mView.findViewById(R.id.d_message_content);
                    TextView d_address_content = (TextView) mView.findViewById(R.id.d_address_content);
                    TextView d_cleaner = (TextView) mView.findViewById(R.id.d_cleaner);
                    TextView d_time = (TextView) mView.findViewById(R.id.d_time);
                    TextView d_payment = (TextView) mView.findViewById(R.id.d_payment);
                    TextView d_service = (TextView) mView.findViewById(R.id.d_service);
                    TextView d_price = (TextView) mView.findViewById(R.id.d_price);
                    TextView d_status_content = (TextView) mView.findViewById(R.id.d_status_content);
                    RatingBar MyRating = (RatingBar) mView.findViewById(R.id.MyRating);

                    TextView d_title = (TextView) mView.findViewById(R.id.d_title);
                    TextView d_title_content = (TextView) mView.findViewById(R.id.d_title_content);

                    LinearLayout a_profile = (LinearLayout) mView.findViewById(R.id.servicebg);

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
                    int rate = Integer.parseInt(value[14]);

                    d_message_content.setText(remarks);
                    d_address_content.setText(location);
                    d_time.setText(" "+time);
                    d_payment.setText(" "+method);
                    d_cleaner.setText(" = "+cleaners);
                    d_service.setText(type_clean +" ("+hours+"Hours)");
                    d_date.setText(" "+date_time);
                    d_price.setText("Total: ₱ " + price);
                    d_status_content.setText(transaction_status);
                    d_title_content.setText(cleaner);
                    MyRating.setRating(rate);

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mView.getContext());
                    String role = sharedPreferences.getString("role", "");

                    if(method.contains("CASH")) {
                        d_payment.setCompoundDrawablesWithIntrinsicBounds(R.drawable.d_cash, 0, 0, 0);
                    }else{
                        d_payment.setCompoundDrawablesWithIntrinsicBounds(R.drawable.d_credit_card, 0, 0, 0);
                    }

                    if(role.contains("user")){
                        d_title.setText("Cleaners");
                        MyRating.setIsIndicator(false);
                    }else if(role.contains("cleaner")){
                        d_title.setText("Client");
                        MyRating.setIsIndicator(true);
                    }

                    try {
                        if (value[2].trim().contains("Deluxe Cleaning")){
                            a_profile.setBackgroundResource(R.drawable.d_deluxe);
                        }else if(value[2].trim().contains("Premium Cleaning")){
                            a_profile.setBackgroundResource(R.drawable.d_premium);
                        }else if(value[2].trim().contains("Yaya for a day")){
                            a_profile.setBackgroundResource(R.drawable.d_yaya);
                        }

                    } catch(Exception e) {

                    }


                    MyRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                        @Override
                        public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                            BackGround bg=new BackGround();
                            bg.execute(transaction_id,""+ratingBar.getRating());
                            Toast.makeText(view.getContext(), ""+ ratingBar.getRating(), Toast.LENGTH_SHORT).show();

                        }
                    });

                    mBuilder.setView(mView);
                    final android.support.v7.app.AlertDialog dialog = mBuilder.create();
                    dialog.show();

                    d_message_content.setMovementMethod(new ScrollingMovementMethod());
                    d_address_content.setMovementMethod(new ScrollingMovementMethod());
                    d_title_content.setMovementMethod(new ScrollingMovementMethod());

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
            String id=params[0];
            String rate=params[1];
            String data = "";
            int tmp;


            try {
                URL url = new URL("http://cleanbydemand.com/php/m_function.php");
                String urlParams = "id=" + 6 + "&trans_id=" + id+ "&rate=" + rate;

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
            Toast.makeText(context, ""+ s, Toast.LENGTH_SHORT).show();

        }
    }

}