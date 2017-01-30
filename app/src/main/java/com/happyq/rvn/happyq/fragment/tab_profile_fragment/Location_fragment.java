package com.happyq.rvn.happyq.fragment.tab_profile_fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.happyq.rvn.happyq.R;

import static android.Manifest.*;

public class Location_fragment extends SupportMapFragment implements OnMapReadyCallback
         {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private GoogleMap mMap;
    private Marker marker;

    public Location_fragment() {
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d("MyMap", "onResume");
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {

        if (mMap == null) {

            Log.d("MyMap", "setUpMapIfNeeded");

            getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("MyMap", "onMapReady");
        //mMap = googleMap;
        mMap= configActivityMaps(googleMap);
        setUpMap();
    }

    private void setUpMap() {

        mMap.setMyLocationEnabled(true);
        //mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        //mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);


        mMap.setMyLocationEnabled(true);
        //mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        //mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        try {
            final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showAlertDialogGps();
            }else{
                Location loc = manager.getLastKnownLocation(manager.getBestProvider(new Criteria(), false));
                Double a = loc.getLatitude();
                Double b = loc.getLongitude();
                CameraUpdate myCam = CameraUpdateFactory.newLatLngZoom(new LatLng(loc.getLatitude(), loc.getLongitude()), 12);
                mMap.animateCamera(myCam);
                mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
            }
        }catch (Exception e){}


    }
             public static GoogleMap configActivityMaps(GoogleMap googleMap) {
                 // set map type
                 googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                 // Enable / Disable zooming controls
                 googleMap.getUiSettings().setZoomControlsEnabled(true);

                 // Enable / Disable Compass icon
                 googleMap.getUiSettings().setCompassEnabled(true);
                 // Enable / Disable Rotate gesture
                 googleMap.getUiSettings().setRotateGesturesEnabled(true);
                 // Enable / Disable zooming functionality
                 googleMap.getUiSettings().setZoomGesturesEnabled(true);

                 googleMap.getUiSettings().setScrollGesturesEnabled(true);
                 googleMap.getUiSettings().setMapToolbarEnabled(true);

                 return googleMap;
             }


             private void showAlertDialogGps() {
                 final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                 builder.setMessage(R.string.dialog_content_gps);
                 builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int id) {
                         startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                     }
                 });
                 builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int id) {
                         dialog.cancel();
                     }
                 });
                 final AlertDialog alert = builder.create();
                 alert.show();
             }
}