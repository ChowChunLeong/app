package com.example.chunleong.cpaperphp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;


public class submitConferencePaper extends AppCompatActivity {

    Uri path;
    String cpemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_conference_paper);
        cpemail= getIntent().getStringExtra("userid");
        Button browse = (Button) findViewById(R.id.browse);
        Button upload = (Button) findViewById(R.id.upload);

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select PDF"), 1);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText cped = (EditText)findViewById(R.id.edCPname);
                final String cpname = cped.getText().toString();
                insertCP(cpname,cpemail);
                (new Upload(submitConferencePaper.this, path)).execute();

            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                path = result.getData();

            }
        }
    }
    private void insertCP(final String cpname, final String cpemail) {
        class InsertCP extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("cpname",cpname);
                hashMap.put("cpemail",cpemail);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest("http://ccl000.000webhostapp.com/conferencepaper/php/insertPdfInfo.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                String[] sarray = s.split("\\s*,\\s*");
                String success = sarray[0];

                if (success.equals("success")){
                    Log.d("haha","success upload info pdf");

                }else{

                }
            }
        }
        try {
            InsertCP icp = new InsertCP();
            icp.execute();
        }catch(Exception e){}
    }

}

class Upload extends AsyncTask<Void, Void, Void> {
    private ProgressDialog pd;
    private Context c;
    private Uri path;

    public Upload(Context c, Uri path) {
        this.c = c;
        this.path = path;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = ProgressDialog.show(c, "Uploading", "Please Wait");
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        pd.dismiss();
    }

    @Override
    protected Void doInBackground(Void... params) {
        String url_path = "http://ccl000.000webhostapp.com/conferencepaper/php/upload.php";
        HttpURLConnection conn = null;

        int maxBufferSize = 1024;
        try {
            URL url = new URL(url_path);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setChunkedStreamingMode(1024);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data");

            OutputStream outputStream = conn.getOutputStream();
            InputStream inputStream = c.getContentResolver().openInputStream(path);

            int bytesAvailable = inputStream.available();
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];

            int bytesRead;
            while ((bytesRead = inputStream.read(buffer, 0, bufferSize)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            inputStream.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                Log.i("result", line);
            }
            reader.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

}
