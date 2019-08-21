package com.example.camera.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.camera.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class FragmentThreePicture extends Fragment {

    private TextView textViewLo;
    private TextView textViewLa;
    private Button buttonGetLocation;
    private Activity activity;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private double longitude;
    private double latitude;
    private CallBackFragmentThreePicture callBackFragmentThreePicture;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_three_picture, container, false);

        textViewLa = view.findViewById(R.id.text_view_la);
        textViewLo = view.findViewById(R.id.text_view_lo);
        buttonGetLocation = view.findViewById(R.id.button_get_location);

        activity = getActivity();

        ActivityCompat.requestPermissions(activity, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        buttonGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (buttonGetLocation.getText().equals("Save")){
                    callBackFragmentThreePicture.lonlat(longitude, latitude);
                } else {
                    if (ActivityCompat.checkSelfPermission(
                            activity,
                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            &&
                            ActivityCompat.checkSelfPermission(activity,
                                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    {
                        return;
                    }

                    fusedLocationProviderClient.getLastLocation().addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            longitude = location.getLongitude();
                            latitude = location.getLatitude();
                            textViewLa.setText(String.valueOf(longitude));
                            textViewLo.setText(String.valueOf(latitude));
                            buttonGetLocation.setText("Save");
                        }
                    });
                }
            }
        });
    }

    public interface CallBackFragmentThreePicture{
        void lonlat(double lon, double lat);
    }

    public void setListener(CallBackFragmentThreePicture callBackFragmentThreePicture){
        this.callBackFragmentThreePicture = callBackFragmentThreePicture;
    }

}
