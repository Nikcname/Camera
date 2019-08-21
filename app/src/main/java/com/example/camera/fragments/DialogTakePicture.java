package com.example.camera.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.camera.R;
import com.example.camera.holders.AllElement;

public class DialogTakePicture extends DialogFragment {

    private FragmentTwoPicture pictureTwo;
    private FragmentOnePicture pictureOne;
    private byte[] bytesOfPicture;
    private FragmentThreePicture pictureThree;
    private AllElement element = new AllElement();
    private CallbackDialogPicture callbackDialogPicture;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_take_picture, container, false);

        Long tsLong = System.currentTimeMillis()/1000;
        element.setName(tsLong.toString());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        if (pictureOne == null){
            pictureOne = new FragmentOnePicture();
        }
        pictureOne.setListener(new FragmentOnePicture.CallbackFragmentOnePicture() {
            @Override
            public void byteArray(byte[] bytes) {
                callFragmentTwo(bytes);
            }
        });
        transaction.add(R.id.main_frame_picture, pictureOne);

        transaction.commit();

    }

    private void callFragmentTwo(byte[] bytes){

        pictureTwo = FragmentTwoPicture.newInstance(bytes);
        bytesOfPicture = bytes;

        pictureTwo.setListener(new FragmentTwoPicture.CallbackFragmentTwoPicture() {
            @Override
            public void backPressed() {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.main_frame_picture, pictureOne);
                transaction.commit();
            }

            @Override
            public void nextPressed() {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

                pictureThree = new FragmentThreePicture();
                pictureThree.setListener(new FragmentThreePicture.CallBackFragmentThreePicture() {
                    @Override
                    public void lonlat(double lon, double lat) {
                        element.setBytes(bytesOfPicture);
                        element.setLon(lon);
                        element.setLat(lat);
                        callbackDialogPicture.passElement(element);
                        dismiss();
                    }
                });
                transaction.replace(R.id.main_frame_picture, pictureThree);
                transaction.commit();
            }
        });

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.main_frame_picture, pictureTwo);

        transaction.commit();

    }

    public interface CallbackDialogPicture{
        void passElement(AllElement element);
    }

    public void setListener(CallbackDialogPicture callbackDialogPicture){
        this.callbackDialogPicture = callbackDialogPicture;
    }
}
