package com.example.chunleong.cpaperphp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class viewcpPDF extends AppCompatActivity {
    private PDFView pdfview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewcp_pdf);
        pdfview = (PDFView)findViewById(R.id.pdfview);
        String url= getIntent().getStringExtra("URL");
        new RetrivePDFStream().execute(url);
    }
    class RetrivePDFStream extends AsyncTask<String,Void ,InputStream> {

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try{
                URL url= new URL (strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            }catch (IOException e){
                return null;
            }
            return inputStream;
        }
        protected  void onPostExecute(InputStream inputStream){
            pdfview.fromStream(inputStream).load();
        }
    }
}
