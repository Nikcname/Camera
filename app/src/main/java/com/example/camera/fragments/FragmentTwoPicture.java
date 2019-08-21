package com.example.camera.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.camera.R;

public class FragmentTwoPicture extends Fragment {

    private ImageView imageView;
    private Button buttonNext;
    private Button buttonBack;
    private CallbackFragmentTwoPicture callbackFragmentTwoPicture;

    public static FragmentTwoPicture newInstance(byte[] bytes) {

        FragmentTwoPicture fragment = new FragmentTwoPicture();
        Bundle args = new Bundle();
        args.putByteArray("bytes", bytes);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_two_picture, container, false);

        byte[] bytes = getArguments().getByteArray("bytes");
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imageView = view.findViewById(R.id.img);
        imageView.setImageBitmap(bitmap);

        buttonBack =view.findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callbackFragmentTwoPicture.backPressed();
            }
        });

        buttonNext = view.findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callbackFragmentTwoPicture.nextPressed();
            }
        });

        return view;
    }

    public interface CallbackFragmentTwoPicture{
        void backPressed();
        void nextPressed();
    }

    public void setListener(CallbackFragmentTwoPicture callbackFragmentTwoPicture){
        this.callbackFragmentTwoPicture = callbackFragmentTwoPicture;
    }
}
