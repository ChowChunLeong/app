package com.example.chunleong.cpaperphp;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class updateProfile extends AppCompatActivity {


    private EditText edname,edemail,edphone;
    Button btnUpdate, btnBack;
    private String email, name, phone;
    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        email= getIntent().getStringExtra("userid");
        initialView();
        loadprofile(email);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backAuthor();
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogTakePicture();
            }
        });
    }



    private void initialView() {
        image=findViewById(R.id.registeredImage123);
        edname=findViewById(R.id.editTextUsername123);
        edemail=findViewById(R.id.editTextEmail123);
        edemail.setFocusable(false);
        edphone=findViewById(R.id.editTextPhone123);
        btnUpdate = findViewById(R.id.buttonSubmit123);
        btnBack = findViewById(R.id.buttonCancel123);
    }

    private void loadprofile(final String email123) {
        class LoadProfile extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("email",email123);
                RequestHandler requestHandler = new RequestHandler();
                String s = requestHandler.sendPostRequest("http://ccl000.000webhostapp.com/conferencepaper/php/getprofile.php",hashMap);
                return s;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray data = jsonObject.getJSONArray("profile");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject c = data.getJSONObject(i);
                        name = c.getString("name");
                        email = c.getString("email");
                        phone = c.getString("phone");

                    }

                    edemail.setText(email);
                    edname.setText(name);
                    edphone.setText(phone);

                    Picasso.get()
                            .load("http://ccl000.000webhostapp.com/conferencepaper/profileimages/"+email+".jpg")
                            .networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE)
                            .fit()
                            .error(R.mipmap.ic_profile)
                            .into(image);
                }catch(JSONException e){
                    Toast.makeText(updateProfile.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        }
        LoadProfile loadProfile = new LoadProfile();
        loadProfile.execute();
    }
    public void updateProfile() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage("Are you sure to update?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                       check();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void check() {
        String name, phone,email;
        name=edname.getText().toString();
        phone=edphone.getText().toString();
        email = edemail.getText().toString();
        if (name.equals("")||phone.equals("")){
            Toast.makeText(this, "Please fill in all column", Toast.LENGTH_SHORT).show();
        }else {
            try{
                new Encode_image().execute(getDir(),email+".jpg");
            }catch (Exception ex){
                Toast.makeText(this, "Please upload picture", Toast.LENGTH_SHORT).show();
            }
            updateUser(name,phone,email);
        }

    }

    private void updateUser(final String name, final String phone,final String email) {
        class UpdateUser extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("name",name);
                hashMap.put("phone",phone);
                hashMap.put("email",email);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://ccl000.000webhostapp.com/conferencepaper/php/updateprofile.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equalsIgnoreCase("success")) {

                    Toast.makeText(updateProfile.this, "Update Success", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(updateProfile.this,author.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("userid",email);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }else{
                    Toast.makeText(updateProfile.this, "Update Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }

        UpdateUser updateUser = new UpdateUser();
        updateUser.execute();
    }

    private void dialogTakePicture() {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(this.getResources().getString(R.string.dialogtakepicture));

        alertDialogBuilder
                .setMessage(this.getResources().getString(R.string.dialogtakepicturea))
                .setCancelable(false)
                .setPositiveButton(this.getResources().getString(R.string.yesbutton),new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int id) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(takePictureIntent, 1);
                        }
                    }
                })
                .setNegativeButton(this.getResources().getString(R.string.nobutton),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageBitmap = ThumbnailUtils.extractThumbnail(imageBitmap,400,500);
            image.setImageBitmap(imageBitmap);
            image.buildDrawingCache();
            ContextWrapper cw = new ContextWrapper(this);
            File pictureFileDir = cw.getDir("basic", Context.MODE_PRIVATE);
            if (!pictureFileDir.exists()) {
                pictureFileDir.mkdir();
            }
            Log.e("FILE NAME", "" + pictureFileDir.toString());
            if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
                return;
            }
            FileOutputStream outStream = null;
            String photoFile = "profile.jpg";
            File outFile = new File(pictureFileDir, photoFile);
            try {
                outStream = new FileOutputStream(outFile);
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                outStream.flush();
                outStream.close();
                //hasimage = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }




    public String getDir(){
        ContextWrapper cw = new ContextWrapper(this);
        File pictureFileDir = cw.getDir("basic", Context.MODE_PRIVATE);
        if (!pictureFileDir.exists()) {
            pictureFileDir.mkdir();
        }
        Log.d("GETDIR",pictureFileDir.getAbsolutePath());
        return pictureFileDir.getAbsolutePath()+"/profile.jpg";
    }
    public class Encode_image extends AsyncTask<String,String,Void> {
        private String encoded_string, image_name;
        Bitmap bitmap;

        @Override
        protected Void doInBackground(String... args) {
            String filname = args[0];
            image_name = args[1];
            bitmap = BitmapFactory.decodeFile(filname);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
            byte[] array = stream.toByteArray();
            encoded_string = Base64.encodeToString(array, 0);
            return null;
        }

        @Override
        protected void onPostExecute(Void avoid) {
            makeRequest(encoded_string, image_name);
        }

        private void makeRequest(final String encoded_string, final String image_name) {
            class UploadAll extends AsyncTask<Void, Void, String> {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected String doInBackground(Void... params) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("encoded_string", encoded_string);
                    map.put("image_name", image_name);
                    RequestHandler rh = new RequestHandler();//request server connection
                    String s = rh.sendPostRequest("http://ccl000.000webhostapp.com/conferencepaper/php/replaceimage.php", map);
                    return s;
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if (s.equalsIgnoreCase("Success")) {
                        //insertData();
                         Toast.makeText(updateProfile.this, "Success replace Image", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(updateProfile.this, "Failed Registration", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            UploadAll uploadall = new UploadAll();
            uploadall.execute();
        }
    }
    public void backAuthor(){

        Intent intent = new Intent(updateProfile.this,author.class);
        Bundle bundle = new Bundle();
        bundle.putString("userid",email);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(updateProfile.this,author.class);
        Bundle bundle = new Bundle();
        bundle.putString("userid",email);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

}
