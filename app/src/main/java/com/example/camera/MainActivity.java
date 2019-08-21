package com.example.camera;

import android.Manifest;
import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.camera.adapters.AllAdapter;
import com.example.camera.database.AppDatabase;
import com.example.camera.fragments.DialogTakePicture;
import com.example.camera.holders.AllElement;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private List<AllElement> allElements = new ArrayList<>();
    private FloatingActionButton floatingActionButton;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private Activity activity;
    private static final String IMAGE_DIRECTORY = "/CustomImage";
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_all_main);
        layoutManager = new LinearLayoutManager(this);
        adapter = new AllAdapter(allElements);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        activity = this;
        floatingActionButton = findViewById(R.id.floatingActionButton);

        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "my_phtos_two")
                .allowMainThreadQueries()
                .build();

        allElements.addAll(database.allElementsDao().getAll());

        ((AllAdapter) adapter).setListener(new AllAdapter.CallbackAllAdapter() {
            @Override
            public void openMap(String name) {

                Log.d("sss", name);
                AllElement element = database.allElementsDao().findByName(name);

                Uri gmmIntentUri = Uri.parse("geo:" + element.getLat()
                        + "," +
                        element.getLon());

                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()){

                    DialogTakePicture dialogTakePicture = new DialogTakePicture();
                    dialogTakePicture.setListener(new DialogTakePicture.CallbackDialogPicture() {
                        @Override
                        public void passElement(AllElement element) {
                            saveAllData(element);
                        }
                    });
                    dialogTakePicture.show(getSupportFragmentManager(), "capturing");

                } else {
                    requestPermission();
                }
            }
        });

    }

    private void saveAllData(AllElement element){

        database.allElementsDao().insertAll(element);
        allElements.add(element);
        adapter.notifyDataSetChanged();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes = element.getBytes();
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);
        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);

        ActivityCompat.requestPermissions(activity, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        if (!wallpaperDirectory.exists()) {
            Log.d("dirrrrrr", "" + wallpaperDirectory.mkdirs());
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, element.getName() + ".jpg");
            f.createNewFile();   //give read write permission
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(byteArrayOutputStream.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    private boolean checkPermission(){
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
    }
}
