package com.example.camera.fragments;

import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.camera.R;

public class FragmentOnePicture extends Fragment {

    private Camera mCamera;
    private CameraPreview mPreview;
    private Camera.PictureCallback mPicture;
    private Button capture;
    private Button switchCamera;
    private Context mContext;
    private LinearLayout cameraPreview;
    private boolean cameraFront = false;
    private CallbackFragmentOnePicture callbackFragmentOnePicture;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_one_picture, container, false);

        mContext = getContext();
        mCamera = Camera.open();
        mCamera.setDisplayOrientation(0);
        cameraPreview = view.findViewById(R.id.cPreview);
        mPreview = new CameraPreview(mContext, mCamera);
        cameraPreview.addView(mPreview);
        mCamera.startPreview();
        capture = view.findViewById(R.id.btnCam);
        switchCamera = view.findViewById(R.id.btnSwitch);

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCamera.takePicture(null, null, getPictureCallback());
            }
        });

        switchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Camera.getNumberOfCameras() > 1){
                    releaseCamera();
                    chooseCamera();
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private int findFrontFacingCamera(){

        int cameraId = 1;

        for (int i = 0; i < Camera.getNumberOfCameras(); i++){

            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);

            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
                cameraId = i;
                cameraFront = true;
                break;
            }
        }

        return cameraId;
    }

    private int findBackFacingCamera(){

        int cameraId = -1;

        for (int i = 0; i < Camera.getNumberOfCameras(); i++){

            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);

            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK){
                cameraId = i;
                cameraFront = false;
                break;
            }
        }

        return cameraId;

    }

    @Override
    public void onResume() {
        super.onResume();

        if (mCamera == null){
            mCamera = Camera.open();
            mCamera.setDisplayOrientation(0);
            mPicture = getPictureCallback();
            mPreview.refreshCamera(mCamera);
            Log.d("sss", "null");
        } else {
            Log.d("sss", "not null");
        }
    }

    public void chooseCamera(){

        if (cameraFront){
            int cameraId = findBackFacingCamera();
            if (cameraId >= 0){
                mCamera = Camera.open(cameraId);
                mCamera.setDisplayOrientation(0);
                mPicture = getPictureCallback();
                mPreview.refreshCamera(mCamera);
            }
        } else {
            int cameraId = findFrontFacingCamera();
            if (cameraId >= 0){
                mCamera = Camera.open(cameraId);
                mCamera.setDisplayOrientation(0);
                mPicture = getPictureCallback();
                mPreview.refreshCamera(mCamera);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseCamera();
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Camera.PictureCallback getPictureCallback(){
        Camera.PictureCallback picture = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {

                callbackFragmentOnePicture.byteArray(bytes);

            }
        };
        return picture;
    }

    public interface CallbackFragmentOnePicture{
        void byteArray(byte[] bytes);
    }

    public void setListener(CallbackFragmentOnePicture callbackFragmentOnePicture){
        this.callbackFragmentOnePicture = callbackFragmentOnePicture;
    }

}
