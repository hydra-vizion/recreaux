package com.example.recreaux;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MyArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;


    public MyArrayAdapter(Context context, int textViewResourceId, String[] values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.custom_list, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.customListTV);

        textView.setText(values[position]);

        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                //Toast.makeText(MyArrayAdapter.this,"Data inserted succesfully",Toast.LENGTH_LONG).show();

            }
        });


        return rowView;
    }
}