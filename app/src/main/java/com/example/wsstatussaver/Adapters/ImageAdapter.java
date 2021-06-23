package com.example.wsstatussaver.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wsstatussaver.Images;
import com.example.wsstatussaver.Models.StatusModel;
import com.example.wsstatussaver.R;

import java.io.IOException;
import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewholder> {

    private final ArrayList<StatusModel> arrayList;
    Context context;
    Images imagesfragment;

    public ImageAdapter(ArrayList<StatusModel> arrayList, Context context,Images imagesfragment) {
        this.arrayList = arrayList;
        this.context = context;
        this.imagesfragment = imagesfragment;
    }

    @NonNull
    @Override



    public ImageViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
        return new ImageViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewholder holder, final int position) {
        StatusModel statusmodel = arrayList.get(position);
        holder.imageview.setImageBitmap(statusmodel.getThumbnail());
    }

    @Override
    public int getItemCount() {

        return arrayList.size();
    }

    public class ImageViewholder extends RecyclerView.ViewHolder {

        private ImageView imageview;
        private ImageButton imageButton,imageButton2;

        public ImageViewholder(@NonNull View itemView) {
            super(itemView);
            imageview=itemView.findViewById(R.id.imageView);
            imageButton=itemView.findViewById(R.id.imageButton);
            imageButton2=itemView.findViewById(R.id.imageButton2);

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StatusModel statusModel = arrayList.get(getAdapterPosition());
                    if (statusModel != null){
                        try {
                            imagesfragment.saveImage(statusModel);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            imageButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StatusModel statusModel = arrayList.get(getAdapterPosition());
                    if (statusModel != null){
                        imagesfragment.shareImage(statusModel);
                    }
                }
            });
        }
    }
}
