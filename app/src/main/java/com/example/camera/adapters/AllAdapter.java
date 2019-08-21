package com.example.camera.adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.camera.R;
import com.example.camera.holders.AllElement;

import java.util.List;

public class AllAdapter extends RecyclerView.Adapter<AllAdapter.AllViewHolder> {

    private List<AllElement> allElements;
    private CallbackAllAdapter callbackAllAdapter;
    private View view;

    public AllAdapter(List<AllElement> allElements){
        this.allElements = allElements;
    }

    @NonNull
    @Override
    public AllViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.element_all_main, viewGroup, false);

        return new AllViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllViewHolder allViewHolder, int i) {

        AllElement element = allElements.get(i);

        String photoPath = Environment.getExternalStorageDirectory() + "/CustomImage/"
                + element.getName() + ".jpg";

        Bitmap bitmap = BitmapFactory.decodeFile(photoPath);

        allViewHolder.textViewName.setText(element.getName());
        allViewHolder.textViewLat.setText(String.valueOf(element.getLat()));
        allViewHolder.textViewLon.setText(String.valueOf(element.getLon()));

        Glide.with(view).load(bitmap).into(allViewHolder.imageViewPhoto);

    }

    @Override
    public int getItemCount() {
        return allElements.size();
    }

    public class AllViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewName;
        TextView textViewLat;
        TextView textViewLon;
        ImageView imageViewPhoto;

        public AllViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name_element_all_main);
            textViewLat = itemView.findViewById(R.id.text_view_lat_element_all_main);
            textViewLon = itemView.findViewById(R.id.text_view_lon_element_all_main);
            imageViewPhoto = itemView.findViewById(R.id.image_view_element_all_main);

            itemView.setClickable(true);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            callbackAllAdapter.openMap(allElements.get(getAdapterPosition()).getName());
        }
    }

    public interface CallbackAllAdapter{
        void openMap(String name);
    }

    public void setListener(CallbackAllAdapter callbackAllAdapter){
        this.callbackAllAdapter = callbackAllAdapter;
    }
}
