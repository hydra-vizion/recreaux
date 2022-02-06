package com.example.recreaux;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
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
import androidx.annotation.RequiresApi;

import com.example.recreaux.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

public class EventUserListAdapter extends ArrayAdapter<String> {
    private DatabaseReference ref;
    private DatabaseReference refNotification;
    private boolean buttonshow;
    private int eventid;
    String currentuserid;
    String currentusername;
    String userimage;


    public EventUserListAdapter(Activity context, int resource, List<String> list,boolean buttonshow,int eventid) {
        super(context, resource, list);
        this.buttonshow = buttonshow;
        this.eventid = eventid;
    }


    @NonNull
    @Override

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //currentuserid ="OmnyApXKCAOdnll4Dc1RKoLLSKH3";
        currentuserid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference();
        String index = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_record_view_event, parent, false);
        }
        ref.child("Users").child(currentuserid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isComplete()&&task.isSuccessful()){
                    currentusername=task.getResult().child("Username").getValue().toString();
                }
            }
        });
        Button btn_UserRecordViewEventShare;
        btn_UserRecordViewEventShare = (Button) convertView.findViewById(R.id.btn_UserRecordViewEventShare);
        btn_UserRecordViewEventShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.child("Users").child(currentuserid).child("UserImage").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isComplete() && task.isSuccessful()){
                            userimage = task.getResult().getValue().toString();
                            if(userimage==null){
                                Bitmap image = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
                                image.eraseColor(Color.BLACK);
                                userimage=Base64.getEncoder().encodeToString(getBytes(image));
                            }
                            refNotification= FirebaseDatabase.getInstance().getReference("Notifications").child(index);
                            refNotification.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    int TotalNotification= (int)task.getResult().getChildrenCount();
                                    TotalNotification++;
                                    refNotification.child("Noti_"+String.valueOf(TotalNotification)).child("Message").setValue(currentusername+" shared an event with you!");
                                    refNotification.child("Noti_"+String.valueOf(TotalNotification)).child("Seen").setValue(false);
                                    refNotification.child("Noti_"+String.valueOf(TotalNotification)).child("UserImage").setValue(userimage);
                                    refNotification.child("Noti_"+String.valueOf(TotalNotification)).child("Event ID").setValue(eventid);
                                }
                            });
                        }
                    }
                });
                //Toast.makeText(getContext(),"Shared to "+index, Toast.LENGTH_SHORT).show();
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
            @RequiresApi(api = Build.VERSION_CODES.O)
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