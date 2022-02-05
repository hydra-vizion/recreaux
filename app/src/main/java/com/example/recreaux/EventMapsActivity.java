package com.example.recreaux;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.recreaux.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class EventMapsActivity  extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    String placeaddress;
    double placelongitude,placelatitude;
    String parentevent;
    Button btn_EventMapsConfirm;
    int width,height;


    // creating a variable
    // for search view.
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_maps);


        // initializing our search view.
        searchView = findViewById(R.id.sv_EventMapsSearch);
        btn_EventMapsConfirm = findViewById(R.id.btn_EventMapsConfirm);
        // Obtain the SupportMapFragment and get notified
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if(getIntent().getExtras() != null) {
            parentevent = (String) getIntent().getExtras().getString("parent");
            if(parentevent.equalsIgnoreCase("create")){
                placeaddress=null;
                width=getIntent().getExtras().getInt("width");
                height=getIntent().getExtras().getInt("height");
                System.out.println(parentevent);
            }
            else if(parentevent.equalsIgnoreCase("edit")){
                placeaddress=null;
                width=getIntent().getExtras().getInt("width");
                height=getIntent().getExtras().getInt("height");
                System.out.println(parentevent);
            }
            else if(parentevent.equalsIgnoreCase("viewonly")){
                searchView.setVisibility(View.GONE);
                placelatitude = getIntent().getDoubleExtra("latitude",0);
                placelongitude = getIntent().getDoubleExtra("longitude",0);
                placeaddress = getIntent().getExtras().getString("address");
                btn_EventMapsConfirm.setText("Back");
            }
        }


        // adding on query listener for our search view.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // on below line we are getting the
                // location name from search view.
                mMap.clear();
                String location = searchView.getQuery().toString();

                // below line is to create a list of address
                // where we will store the list of all address.
                List<Address> addressList = null;

                // checking if the entered location is null or not.
                if (location != null || !location.equals("")) {
                    // on below line we are creating and initializing a geo coder.
                    Geocoder geocoder = new Geocoder(EventMapsActivity.this);
                    try {
                        // on below line we are getting location from the
                        // location name and adding that location to address list.
                        addressList = geocoder.getFromLocationName(location, 1);
                        //System.out.println(addressList.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // on below line we are getting the location
                    // from our list a first position.
                    if(addressList!=null && addressList.size()!=0){
                        Address address = addressList.get(0);
                        placeaddress = address.getAddressLine(0);
                        placelatitude = address.getLatitude();
                        placelongitude = address.getLongitude();

                        // on below line we are creating a variable for our location
                        // where we will add our locations latitude and longitude.

                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        // on below line we are adding marker to that position.
                        mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        // below line is to animate camera to that position.
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

                    }
                    else{
                        Toast.makeText(EventMapsActivity.this,"Place Not Found",Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        btn_EventMapsConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(parentevent.equalsIgnoreCase("viewonly")){
                    finish();
                }
                else{
                    if(placeaddress==null){
                        finish();
                    }else{
                        LatLng latLng2 = new LatLng(placelatitude,placelongitude);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng2));
                        // below line is to animate camera to that position.
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng2, 16));
                        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                            @Override
                            public void onMapLoaded() {
                                // Make a snapshot when map's done loading

                                mMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
                                    @Override
                                    public void onSnapshotReady(Bitmap bitmap) {
                                        Bitmap bmp = scaleCenterCrop(bitmap,height,width);

                                        Intent intent = new Intent();
                                        intent.putExtra("address", placeaddress);
                                        intent.putExtra("latitude", placelatitude);
                                        intent.putExtra("longitude", placelongitude);
                                        intent.putExtra("mappreview", getBytes(bmp));
                                        setResult(RESULT_OK, intent);
                                        Toast.makeText(EventMapsActivity.this,"Location Selected",Toast.LENGTH_SHORT).show();
                                        finish();

                                    }
                                });
                            }
                        });

                    }
                }
            }
        });
        // at last we calling our map fragment to update.
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(parentevent.equalsIgnoreCase("viewonly")){
            putmarker();
        }
    }

    public void putmarker(){
        LatLng latLng = new LatLng(placelatitude, placelongitude);
        // on below line we are adding marker to that position.
        mMap.addMarker(new MarkerOptions().position(latLng).title("Event Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        // below line is to animate camera to that position.
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
    }


    public Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        // Compute the scaling factors to fit the new height and width, respectively.
        // To cover the final image, the final scaling will be the bigger
        // of these two.
        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.max(xScale, yScale);

        // Now get the size of the source bitmap when scaled
        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;

        // Let's find out the upper left coordinates if the scaled bitmap
        // should be centered in the new size give by the parameters
        float left = (newWidth - scaledWidth) / 2;
        float top = (newHeight - scaledHeight) / 2;

        // The target rectangle for the new, scaled version of the source bitmap will now
        // be
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

        // Finally, we create a new bitmap of the specified size and draw our new,
        // scaled bitmap onto it.
        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
        Canvas canvas = new Canvas(dest);
        canvas.drawBitmap(source, null, targetRect, null);

        return dest;
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