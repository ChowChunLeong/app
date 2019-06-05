package com.example.chunleong.cpaperphp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class listOfUser extends AppCompatActivity {
    ListView lvrest;
    ArrayList<HashMap<String, String>> userlist;
    ConferencePaper cp[];
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_user);
        email= getIntent().getStringExtra("userid");
        Log.d("hahalistofuser",email);
        lvrest = findViewById(R.id.listviewUser);

        loadCompany();
        lvrest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cpid = cp[position].cdid;
                String cpname = cp[position].name;
                String cpemail = cp[position].email;

                String url = "https://ccl000.000webhostapp.com/conferencepaper/PDF/" + cpid + ".pdf";
                /*
                .setText(cpid);
                ed3.setText(fname);
                ed4.setText(fprice);
                for (int i=0; i< sp1.getCount();i++){
                    if(sp1.getItemAtPosition(i).equals(floc)){
                        sp1.setSelection(i);
                    }
                }*/
                Log.d("haha", cpid);
                Log.d("haha", cpname);
                Log.d("haha", cpemail);
                /*
                Intent intent = new Intent(getBaseContext(),viewcpPDF.class);
                intent.putExtra("URL", url);
                startActivity(intent);*/
                manageSubmission(cpid, cpname, cpemail, url);
            }
        });
    }

    private void manageSubmission(final String cpid, final String cpname, final String cpemail, final String url) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.view_dlt_update, null);
        dialogBuilder.setView(dialogView);
        final RadioGroup radioGroup = (RadioGroup) dialogView.findViewById(R.id.radioGroup);
        final RadioButton radioButtonDelete = (RadioButton) dialogView.findViewById(R.id.radioBtnDelete);
        final RadioButton radioButtonUpdate = (RadioButton) dialogView.findViewById(R.id.radioBtnUpdate);
        final EditText radioBtnUpdateEditTextCPName = (EditText) dialogView.findViewById(R.id.radioBtnUpdateEditTextCPName);
        final RadioButton radioButtonView = (RadioButton) dialogView.findViewById(R.id.radioBtnViewPDf);
        final Button buttonSubmitStatus = (Button) dialogView.findViewById(R.id.buttonSubmit);

        final EditText conferenceName = (EditText) dialogView.findViewById(R.id.radioBtnUpdateEditTextCPName);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.radioBtnUpdate) {
                    radioBtnUpdateEditTextCPName.setVisibility(View.VISIBLE);
                } else {
                    radioBtnUpdateEditTextCPName.setVisibility(View.INVISIBLE);
                }
            }
        });
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonSubmitStatus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String name456;

                if (radioButtonDelete.isChecked()) {
                    deleteCP(cpid);
                    deletePDF(cpid);
                } else if (radioButtonUpdate.isChecked()) {
                    name456 = conferenceName.getText().toString().trim();
                    updateCPInfo(cpid,name456,cpemail,url);
                } else if (radioButtonView.isChecked()) {
                    Intent intent = new Intent(getBaseContext(), viewcpPDF.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);
                } else {
                    Toast.makeText(listOfUser.this, "you haven't select a function", Toast.LENGTH_SHORT).show();
                    return;
                }
                b.dismiss();
            }
        });
    }
    private void deleteCP(final String cpid) {
        class DeleteCP extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("cpid",cpid);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest("https://ccl000.000webhostapp.com/conferencepaper/php/deleteCP.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("success")){
                    Toast.makeText(listOfUser.this, "Delete Success",
                            Toast.LENGTH_LONG).show();
                    loadCompany();
                }else{
                    Toast.makeText(listOfUser.this, "Delete Failed",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
        try {
            DeleteCP  dcp = new DeleteCP ();
            dcp.execute();
        }catch(Exception e){}
    }
    private void deletePDF(final String cpid) {
        class DeletePDF extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("cpid",cpid);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest("https://ccl000.000webhostapp.com/conferencepaper/php/deletePDF.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("Deleted")){
                    Toast.makeText(listOfUser.this, "Delete Success",
                            Toast.LENGTH_LONG).show();
                    loadCompany();
                }else{
                    Toast.makeText(listOfUser.this, "Delete Failed",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
        try {
            DeletePDF  dPDF = new DeletePDF ();
            dPDF.execute();
        }catch(Exception e){}
    }
    private void updateCPInfo(final String cpid, final String cpname, final String cpemail, final String url) {
        class UpdateCP extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("cpid",cpid);
                hashMap.put("cpname",cpname);
                hashMap.put("cpemail",cpemail);
                hashMap.put("url",url);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest("https://ccl000.000webhostapp.com/conferencepaper/php/updateCP.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("success")){
                    Toast.makeText(listOfUser.this, "Update success",
                            Toast.LENGTH_LONG).show();
                    loadCompany();
                }else{
                    Toast.makeText(listOfUser.this, "Update Failed",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
        try {
            UpdateCP ucp = new UpdateCP();
            ucp.execute();
        }catch(Exception e){}
    }

    private void loadCompany() {
        class LoadRestaurant extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                //Log.e("haha",loc);
                //hashMap.put("location",loc);
                hashMap.put("email",email);
                RequestHandler rh = new RequestHandler();
                userlist = new ArrayList<>();
                String s = rh.sendPostRequest
                        ("http://ccl000.000webhostapp.com/conferencepaper/php/load_user.php", hashMap);

                Log.e("haha", s);
                return s;

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                // Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
                userlist.clear();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray restarray = jsonObject.getJSONArray("cps");
                    Log.e("haha124", jsonObject.toString());
                    cp = new ConferencePaper[restarray.length()];

                    for (int i = 0; i < restarray.length(); i++) {
                        Log.d("haha1234", "ajjasjsjjsjs");
                        JSONObject c = restarray.getJSONObject(i);

                        String idload = c.getString("cpid");
                        String nmload = c.getString("cpname");
                        String emload = c.getString("cpemail");
                        Log.d("haha1214", idload);
                        cp[i] = new ConferencePaper(idload, nmload, emload);
                        HashMap<String, String> restlisthash = new HashMap<>();
                        restlisthash.put("idload", idload);
                        restlisthash.put("nmload", nmload);
                        restlisthash.put("emload", emload);


                        userlist.add(restlisthash);

                    }
                } catch (final JSONException e) {
                    Log.e("JSONERROR", e.toString());
                }

                ListAdapter adapter = new CustomAdapter(
                        listOfUser.this,
                        userlist,
                        R.layout.cust_list_comp,
                        new String[]{"idload", "nmload", "emload"},
                        new int[]{R.id.textViewload, R.id.textView2load, R.id.textView3load});

                lvrest.setAdapter(adapter);
            }

        }
        LoadRestaurant loadRestaurant = new LoadRestaurant();
        Log.d("haha", "hihi");
        loadRestaurant.execute();

    }
}

