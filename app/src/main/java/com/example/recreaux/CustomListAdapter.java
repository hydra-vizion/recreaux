package com.example.recreaux;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.List;

public class CustomListAdapter extends ArrayAdapter<SearchContent> {

    private Context mContext;
    private int id;
    //private List<String> items , descr ;
    private List<SearchContent> sc;

    public CustomListAdapter(Context context, int textViewResourceId , List<SearchContent> sct)
    {
        super(context, textViewResourceId, sct);
        mContext = context;
        id = textViewResourceId;
        sc = sct;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View v, ViewGroup parent)
    {
        View mView = v ;
        if(mView == null){
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }

        TextView text = (TextView) mView.findViewById(R.id.customListTV);
        TextView text2 = (TextView) mView.findViewById(R.id.customListTV2);
        ImageView img = (ImageView) mView.findViewById(R.id.imageDP);

        if(sc.get(position) != null )
        {
            text.setTextColor(Color.BLACK);
            text.setText(sc.get(position).name);
            text2.setText(sc.get(position).desc);
            img.setImageBitmap(getImage(Base64.getDecoder().decode(sc.get(position).imageUrl)));
            //text.setBackgroundColor(Color.WHITE);
        }


        return mView;
    }
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

}