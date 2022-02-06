package com.example.recreaux;

import static com.example.recreaux.hamburger_nav.redirectActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanQRCode extends AppCompatActivity implements View.OnClickListener {

    Button btn_QRScanner_Scan;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode);


        btn_QRScanner_Scan = findViewById(R.id.btn_QRScanner_Scan);
        btn_QRScanner_Scan.setOnClickListener(this);

        drawerLayout = findViewById(R.id.drawer_layout);

    }

    @Override
    public void onClick(View v) {
        scanCode();
    }

    private void scanCode(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result != null){
            if(result.getContents() != null ){

                //Intent intent = new Intent(ScanQRCode.this, my_events_event.class);
                //startActivity(intent);

                FirebaseDatabase.getInstance().getReference().child("Friends").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(result.getContents()).setValue(true);
                FirebaseDatabase.getInstance().getReference().child("Friends").child(result.getContents()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);

                Intent intent = new Intent(ScanQRCode.this,OtherProfile.class);
                intent.putExtra("id",result.getContents());
                startActivity(intent);


                /*
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(result.getContents());
                builder.setTitle("Scanning Result");
                builder.setPositiveButton("Scan Again", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scanCode();

                    }
                }).setNegativeButton("Finish", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

                */
            }
            else{
                Toast.makeText(this,"No Results",Toast.LENGTH_LONG).show();
            }
        }

        else{
            super.onActivityResult(requestCode,resultCode,data);
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        //Close drawer
        //hamburger_nav.closeDrawer(drawerLayout);
    }

    public void ClickMenu(View view){redirectActivity(this,hamburger_nav.class);}

}