package com.example.infiny.mylocationtrackeradmin.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.infiny.mylocationtrackeradmin.Helpers.SessionManager;
import com.example.infiny.mylocationtrackeradmin.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;

import static com.example.infiny.mylocationtrackeradmin.R.id.mapView;

public class DetailsMapActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton ibtn_back;
    MapView mMapView;
    Button btn_title, btn_previous;
    SessionManager sessionManager;
    private Context mContext;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mContext = this;

        sessionManager=new SessionManager(mContext);
        mMapView = (MapView) findViewById(mapView);
        ibtn_back = (ImageButton) findViewById(R.id.ibtn_back);
        btn_previous = (Button) findViewById(R.id.btn_previous);
        btn_title = (Button) findViewById(R.id.btn_title);
        mMapView.onCreate(savedInstanceState);
//
//

        Bundle bundle=getIntent().getBundleExtra("dataBundle");
        if (bundle!=null)
        {
            btn_title.setText(bundle.getString("data"));
        }

        ibtn_back.setOnClickListener(this);
        btn_title.setOnClickListener(this);
        btn_previous.setOnClickListener(this);
        mMapView.onResume(); // needed to get the map to display immediately
        MapsInitializer.initialize(mContext);
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap= googleMap ;
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                    return;
                }
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mMap.getUiSettings().setMapToolbarEnabled(false);

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(sessionManager.getLocation(), 10);
                mMap.animateCamera(cameraUpdate);
            }
        });




    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.ibtn_back:
                onBackPressed();
                break;
            case R.id.btn_title:
                startActivity(new Intent(DetailsMapActivity.this,AddTargetActivty.class));
                break;
            case R.id.btn_previous:
                startActivity(new Intent(DetailsMapActivity.this,PreviousActivity.class));

                break;

        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
