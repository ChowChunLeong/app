package com.example.chunleong.cpaperphp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class login extends AppCompatActivity {

    EditText edemail,edpassword;
    Button btnlogin,btnreg;
    SharedPreferences sharedPreferences;
    CheckBox cbrem;
    TextView tvforgot;
    Dialog dialogforgotpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edemail = findViewById(R.id.ETEmail);
        edpassword = findViewById(R.id.ETPAssword);
        btnlogin = findViewById(R.id.LoginBtn);
        btnreg = findViewById(R.id.RegisterBtn);
        cbrem = findViewById(R.id.checkBox);
        tvforgot = findViewById(R.id.tvforgotpassword);

        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, register.class);
                startActivity(intent);
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edemail.getText().toString();
                String pass = edpassword.getText().toString();
                loginUser(email,pass);
            }
        });
        cbrem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbrem.isChecked()){
                    String email = edemail.getText().toString();
                    String pass = edpassword.getText().toString();
                    savePref(email,pass);
                }
            }
        });
        tvforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPasswordDialog();
            }
        });
        loadPref();
    }
    void forgotPasswordDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.forgot_password, null);
        dialogBuilder.setView(dialogView);
        final EditText edemail = dialogView.findViewById(R.id.EditTextemail);
        Button btnsendemail = dialogView.findViewById(R.id.buttonSubmitVR);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        btnsendemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String forgotemail =  edemail.getText().toString();
                sendPassword(forgotemail);
                b.dismiss();
            }
        });

    }
    private void sendPassword(final String forgotemail) {
        class SendPassword extends AsyncTask<Void,String,String>{

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap();
                hashMap.put("email",forgotemail);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest("http://ccl000.000webhostapp.com/conferencepaper/php/verify_email.php",hashMap);
                return s;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equalsIgnoreCase("success")){
                    Toast.makeText(login.this, "Success. Check your email", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(login.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }
        SendPassword sendPassword = new SendPassword();
        sendPassword.execute();
    }

    private void savePref(String e, String p) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", e);
        editor.putString("password", p);
        editor.commit();
        Toast.makeText(this, "Preferences has been saved", Toast.LENGTH_SHORT).show();
    }

    private void loadPref() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String premail = sharedPreferences.getString("email", "");
        String prpass = sharedPreferences.getString("password", "");
        if (premail.length()>0){
            cbrem.setChecked(true);
            edemail.setText(premail);
            edpassword.setText(prpass);
        }
    }

    private void loginUser(final String email, final String pass) {
        class LoginUser extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(login.this,
                        "Login user","...",false,false);
            }
            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("email",email);
                hashMap.put("password",pass);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://ccl000.000webhostapp.com/conferencepaper/php/login.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                Log.d("haha12",s);
                super.onPostExecute(s);
                loading.dismiss();
                if(s.equalsIgnoreCase("success")){
                    Toast.makeText(login.this, s, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(login.this,author.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("userid",email);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    Toast.makeText(login.this, "Login Failed", Toast.LENGTH_LONG).show();
                }
            }
        }
        LoginUser loginUser = new LoginUser();
        loginUser.execute();
    }
}