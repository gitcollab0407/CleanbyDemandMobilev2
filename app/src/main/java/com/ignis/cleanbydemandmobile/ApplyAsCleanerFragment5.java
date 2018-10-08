package com.ignis.cleanbydemandmobile;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ApplyAsCleanerFragment5 extends Fragment {


    public ApplyAsCleanerFragment5() {
        // Required empty public constructor
    }

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @BindView(R.id.biodata)
    TextView biodata;
    @BindView(R.id.barangay)
    TextView barangay;
    @BindView(R.id.nbi)
    TextView nbi;
    @BindView(R.id.gov_id)
    TextView gov_id;
    @BindView(R.id.photo)
    TextView photo;

    private static final int PICK_FILE_REQUEST = 1;
    private static final String TAG = Login_SignupActivity.class.getSimpleName();
    private String selectedFilePath;
    private static final int STORAGE_PERMISSION_CODE = 123;
    String click;

    ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apply_as_cleaner_fragment5, container, false);

        ButterKnife.bind(this, view);

        requestStoragePermission();

        return view;
    }

    @OnClick(R.id.biodata)
    public void biodata(View view) {
click = "biodata";
        showFileChooser();

    }

    @OnClick(R.id.barangay)
    public void barangay(View view) {
        click = "barangay";
        showFileChooser();


    }

    @OnClick(R.id.nbi)
    public void nbi(View view) {
        click = "nbi";
        showFileChooser();
    }
    @OnClick(R.id.gov_id)
    public void gov_id(View view) {
        click = "gov_id";
        showFileChooser();
    }

    @OnClick(R.id.photo)
    public void photo(View view) {
        click = "photo";
        showFileChooser();
    }

    private void showFileChooser() {
        Intent intent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
        //sets the select file to all types of files
        intent.setType("*/*");
        //allows to select data and return it
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //starts new activity to select file and return data
        startActivityForResult(Intent.createChooser(intent,"Choose File to Upload.."),PICK_FILE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == getActivity().RESULT_OK) {
                if (requestCode == PICK_FILE_REQUEST) {
                    if (data == null) {
                        //no data present
                        return;
                    }
                    Uri selectedFileUri = data.getData();
                    selectedFilePath = FilePath.getPath(getActivity(), selectedFileUri);
                    Log.i(TAG, "Selected File Path:" + selectedFilePath);

                    if (selectedFilePath != null && !selectedFilePath.equals("")) {
                        if(click.contains("biodata")) {
                            biodata.setText(selectedFilePath);

                        }else if(click.contains("barangay")) {
                            barangay.setText(selectedFilePath);

                        }else if(click.contains("nbi")) {
                            nbi.setText(selectedFilePath);

                        }else if(click.contains("gov_id")) {
                            gov_id.setText(selectedFilePath);

                        }else if(click.contains("photo")) {
                            photo.setText(selectedFilePath);
                        }

                    } else {
                        Toast.makeText(getActivity(), "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        catch(Exception e){
            Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
        }
    }

    //-------------------------------------------------------------------------
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(getActivity(), "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(getActivity(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @OnClick(R.id.next)
    public void next(View view) {
        try {
            if (!biodata.getText().toString().isEmpty() && !barangay.getText().toString().isEmpty()
                        && !nbi.getText().toString().isEmpty() && !gov_id.getText().toString().isEmpty()
                        && !photo.getText().toString().isEmpty()) {

                PublicVariables.a_cv_path = biodata.getText().toString();
                PublicVariables.a_barangay_path = barangay.getText().toString();
                PublicVariables.a_nbi_path = nbi.getText().toString();
                PublicVariables.a_gov_id_path = gov_id.getText().toString();
                PublicVariables.a_photo_path = photo.getText().toString();

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
                        fragmentTransaction.replace(R.id.fragment_container, application1Fragment, null);
                        fragmentTransaction.commit();

                    }
                });

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                    }
                });
            } else {
                Toast.makeText(getActivity(), "Please provide all the document first", Toast.LENGTH_SHORT).show();
            }

        } catch(Exception e) {
        }
    }

    @OnClick(R.id.back)
    public void back(View view) {

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        ApplyAsCleanerFragment4 applyAsCleanerFragment4 = new ApplyAsCleanerFragment4();
        fragmentTransaction.replace(R.id.fragment_container, applyAsCleanerFragment4, null);
        fragmentTransaction.addToBackStack(null).commit();

    }

}
