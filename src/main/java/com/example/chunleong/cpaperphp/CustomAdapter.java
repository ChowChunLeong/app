package com.example.chunleong.cpaperphp;

import android.widget.SimpleAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomAdapter extends SimpleAdapter {

    private Context mContext = null;
    public LayoutInflater inflater;
    public CustomAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        mContext = context;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        try{
            if(convertView==null)
                vi = inflater.inflate(R.layout.cust_list_comp, null);

            HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
            TextView tvrestname = vi.findViewById(R.id.textViewload);
            TextView tvphone = vi.findViewById(R.id.textView2load);
            TextView tvadd = vi.findViewById(R.id.textView3load);

            String dname = (String) data.get("idload");//hilang
            String demail =(String) data.get("nmload");
            String dphone =(String) data.get("emload");

            tvrestname.setText(dname);
            tvphone.setText(demail);
            tvadd.setText(dphone);


        }catch (IndexOutOfBoundsException e){

        }

        return vi;
    }
}

