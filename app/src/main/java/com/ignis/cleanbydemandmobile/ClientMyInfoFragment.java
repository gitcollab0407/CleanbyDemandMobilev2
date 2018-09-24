package com.ignis.cleanbydemandmobile;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ClientMyInfoFragment extends Fragment {

    public ClientMyInfoFragment() {
        // Required empty public constructor
    }

    SharedPreferences sharedPreferences;

    @BindView(R.id.h_profile)
    CircleImageView h_profile;

    @BindView(R.id.changepic)
    TextView changepic;

    @BindView(R.id.h_username)
    TextView h_username;

    @BindView(R.id.h_email)
    TextView h_email;

    @BindView(R.id.h_contact)
    TextView h_contact;

    View view;

    @BindView(R.id.upload)
    Button upload;


    String Path;


    private static final String UPLOAD_URL = "http://cleanbydemand.com/php/m_function.php";
    private static final int IMAGE_REQUEST_CODE = 3;
    private static final int STORAGE_PERMISSION_CODE = 123;

    private Bitmap bitmap;
    private Uri filePath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_client_my_info, container, false);
        ButterKnife.bind(this, view);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        requestStoragePermission();

        try {
            h_username.setText(sharedPreferences.getString("username", "").toString().trim());
            h_email.setText("Email: " + sharedPreferences.getString("email", "").toString());
            h_contact.setText("Contact Number: " + sharedPreferences.getString("contact", "").toString());

/*
            Picasso.with(getContext())
                    .load(sharedPreferences.getString("profile", "").toString())
                    .into(h_profile);*/

            new DownLoadImageTask(h_profile).execute(sharedPreferences.getString("profile", "").toString());

        } catch(Exception e) {
        }

        return view;
    }

    @OnClick(R.id.changepic)
    public void setChangepic(View view) {


        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), IMAGE_REQUEST_CODE);


        upload.setVisibility(View.VISIBLE);
        // BackGround upload = new BackGround();
        // upload.execute();


    }

    @OnClick(R.id.upload)
    public void upload(View view) {
        uploadMultipart();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                Path = "Path: ".concat(getPath(filePath));
                h_profile.setImageBitmap(bitmap);

            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadMultipart() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //getting the actual path of the image
        String path = getPath(filePath);

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(getActivity(), uploadId, UPLOAD_URL)
                    .addFileToUpload(path, "image") //Adding file
                    .addParameter("id", "13") //Adding text parameter to the request
                    .addParameter("username", sharedPreferences.getString("email", "").toString()) //Adding text parameter to the request
                    .addParameter("user_id", sharedPreferences.getString("id", "").toString()) //Adding text parameter to the request
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload
            ((ClientMainActivityFragment) getActivity()).h_profile1.setImageBitmap(bitmap);

        } catch(Exception exc) {
            Toast.makeText(getActivity(), exc.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    public String getPath(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[] { document_id }, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

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


    private class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {
        CircleImageView imageView;

        public DownLoadImageTask(CircleImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try {
                InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                logo = BitmapFactory.decodeStream(is);
            } catch(Exception e) { // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }


        protected void onPostExecute(Bitmap result) {
            h_profile.setImageBitmap(result);
        }
    }
}
