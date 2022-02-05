package com.example.recreaux;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.recreaux.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

public class EventUserListAdapter extends ArrayAdapter<String> {
    private DatabaseReference ref;
    private boolean buttonshow;
    public EventUserListAdapter(Activity context, int resource, List<String> list,boolean buttonshow) {
        super(context, resource, list);
        this.buttonshow = buttonshow;
    }


    @NonNull
    @Override

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ref = FirebaseDatabase.getInstance().getReference();
        String index = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_record_view_event, parent, false);
        }
        Button btn_UserRecordViewEventShare;
        btn_UserRecordViewEventShare = (Button) convertView.findViewById(R.id.btn_UserRecordViewEventShare);
        btn_UserRecordViewEventShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Shared to "+index, Toast.LENGTH_SHORT).show();
            }
        });
        if(buttonshow){
            btn_UserRecordViewEventShare.setVisibility(View.VISIBLE);
            btn_UserRecordViewEventShare.setClickable(true);
        }
        else{
            btn_UserRecordViewEventShare.setVisibility(View.GONE);
            btn_UserRecordViewEventShare.setClickable(false);
        }

        TextView tv_UserRecordViewEventNickName, tv_UserRecordViewEventFullName;
        ImageView iv_UserRecordViewEventImage;
        tv_UserRecordViewEventNickName = (TextView) convertView.findViewById(R.id.tv_UserRecordViewEventNickName);
        tv_UserRecordViewEventFullName = (TextView) convertView.findViewById(R.id.tv_UserRecordViewEventFullName);
        iv_UserRecordViewEventImage = (ImageView) convertView.findViewById(R.id.iv_UserRecordViewEventImage);



        ref.child("Users").child(String.valueOf(index)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isComplete() && task.isSuccessful()){
                    DataSnapshot snap= task.getResult();
                    if (snap.exists()){
                        try{
                            String s = snap.child("UserImage").getValue().toString();
                            iv_UserRecordViewEventImage.setImageBitmap(getImage(Base64.getDecoder().decode(s)));
                        } catch (Exception e) {
                            iv_UserRecordViewEventImage.setBackgroundColor(Color.BLUE);
                            e.printStackTrace();
                        }
                        String fullname=snap.child("Name").getValue().toString();
                        String nickname=snap.child("Username").getValue().toString();
                        tv_UserRecordViewEventNickName.setText(nickname);
                        tv_UserRecordViewEventFullName.setText(fullname);
                    }
                }
            }
        });
        return convertView;
    }

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
