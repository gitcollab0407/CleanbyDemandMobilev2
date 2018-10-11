package com.ignis.cleanbydemandmobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class Application2Fragment extends Fragment implements SingleUploadBroadcastReceiver.Delegate{


    public Application2Fragment() {
        // Required empty public constructor
    }
    private final SingleUploadBroadcastReceiver uploadReceiver =
            new SingleUploadBroadcastReceiver();

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private static final String UPLOAD_URL = "http://cleanbydemand.com/php/m_function.php";


    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_application2, container, false);
        ButterKnife.bind(this, view);

        progressDialog = new ProgressDialog(getActivity());
        return view;
    }

    @OnClick(R.id.join)
    public void join(View view) {

        uploadMultipart();

    }

    @Override
    public void onResume() {
        super.onResume();
        uploadReceiver.register(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        uploadReceiver.unregister(getActivity());
    }

    public void uploadMultipart() {

        String path = PublicVariables.a_cv_path;
        String path1 = PublicVariables.a_barangay_path;
        String path2 = PublicVariables.a_nbi_path;
        String path3 = PublicVariables.a_gov_id_path;
        String path4 = PublicVariables.a_photo_path;

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();
            uploadReceiver.setDelegate(this);
            uploadReceiver.setUploadID(uploadId);

            //Creating a multi part request
            new MultipartUploadRequest(getActivity(), uploadId, UPLOAD_URL)
                    .addFileToUpload(path, "cv") //Adding file
                    .addFileToUpload(path1, "b_c") //Adding file
                    .addFileToUpload(path2, "nbi") //Adding file
                    .addFileToUpload(path3, "g_i") //Adding file
                    .addFileToUpload(path4, "photo") //Adding file

                    .addParameter("id", "18")

                    .addParameter("fname",PublicVariables.a_firstname)
                    .addParameter("lname",PublicVariables.a_lastname)
                    .addParameter("email",PublicVariables.a_email)
                    .addParameter("password",PublicVariables.a_password)
                    .addParameter("cp_number",PublicVariables.a_contact)

                    .addParameter("per_add",PublicVariables.a_perm_address)

                    .addParameter("pre_add",PublicVariables.a_pres_address)
                    .addParameter("date",PublicVariables.a_date_of_birth)
                    .addParameter("age",PublicVariables.a_age)
                    .addParameter("c_stat", PublicVariables.a_civil)
                    .addParameter("height",PublicVariables.a_height)
                    .addParameter("weight",PublicVariables.a_weight)
                    .addParameter("name_spouse",PublicVariables.a_spouse)
                    .addParameter("child",PublicVariables.a_no_children)
                    .addParameter("children",PublicVariables.a_name_children)
                    .addParameter("in_name",PublicVariables.a_em_name)
                    .addParameter("in_number",PublicVariables.a_em_contact)
                    .addParameter("lang_wri",PublicVariables.a_language)
                    .addParameter("e_h",PublicVariables.a_emp_history)


                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)

                    .startUpload();//Starting the upload


        } catch(Exception exc) {
             Toast.makeText(getActivity(), exc.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    @OnClick(R.id.back)
    public void back(View view) {
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        Application1Fragment application1Fragment = new Application1Fragment();
        fragmentTransaction.replace(R.id.fragment_container, application1Fragment, null);
        fragmentTransaction.addToBackStack(null).commit();
    }

    @Override
    public void onProgress(int progress) {

    }

    @Override
    public void onProgress(long uploadedBytes, long totalBytes) {
        progressDialog.setMessage("Creating Profile...");
        progressDialog.show();
    }

    @Override
    public void onError(Exception exception) {
        progressDialog.dismiss();
        Toast.makeText(getActivity(), "Sorry, Please try again later.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCompleted(int serverResponseCode, byte[] serverResponseBody) {
        progressDialog.dismiss();

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        Application3Fragment application3Fragment = new Application3Fragment();
        fragmentTransaction.replace(R.id.fragment_container, application3Fragment, null);
        fragmentTransaction.addToBackStack(null).commit();
    }

    @Override
    public void onCancelled() {

    }
}
