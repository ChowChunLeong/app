package com.example.chunleong.cpaperphp;

import android.app.ProgressDialog;
import android.app.admin.DevicePolicyManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class author extends AppCompatActivity {
    Button make_submission_btn;
    Button manage_cp;
    Button updateprofile;

    final static int PICK_PDF_CODE = 2342;
    //ConferencePaper cp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author);
        make_submission_btn = findViewById(R.id.make_submission_btn1);
        manage_cp = findViewById(R.id.manage_Submission1);
        updateprofile = findViewById(R.id.updateprofile1);
        //listOfU = findViewById(R.id.listOfUser);
        final String email= getIntent().getStringExtra("userid");
        Log.d("haha12",email);
        make_submission_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(author.this,submitConferencePaper.class);
                Bundle bundle = new Bundle();
                bundle.putString("userid",email);
                intent.putExtras(bundle);
                startActivity(intent);
        }
        });

        manage_cp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(author.this,listOfUser.class);
                Bundle bundle = new Bundle();
                bundle.putString("userid",email);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        updateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(author.this,updateProfile.class);
                Bundle bundle = new Bundle();
                bundle.putString("userid",email);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });



        /*
        listOfU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(author.this,listOfUser.class);
                startActivity(intent);
            }
        });*/


    }






}
