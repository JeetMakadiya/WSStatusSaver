package com.example.wsstatussaver.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wsstatussaver.Models.StatusModel;
import com.example.wsstatussaver.R;
import com.example.wsstatussaver.Videos;

import java.io.IOException;
import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewholder> {

    private final ArrayList<StatusModel> arrayList;
    Context context;
    Videos videosfragments;

    public VideoAdapter(ArrayList<StatusModel> arrayList, Context context,Videos videosfragments) {
        this.arrayList = arrayList;
        this.context = context;
        this.videosfragments = videosfragments;
    }

    @NonNull
    @Override
    public VideoAdapter.VideoViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
        return new VideoAdapter.VideoViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.VideoViewholder holder, int position) {
        /*int resource = list.get(position).getImageresource();
        holder.imageview.setImageResource(resource);*/
        StatusModel statusmodel = arrayList.get(position);
        holder.imageview.setImageBitmap(statusmodel.getThumbnail());
    }

    @Override
    public int getItemCount() {

        return arrayList.size();
    }

    public class VideoViewholder extends RecyclerView.ViewHolder {

        private ImageView imageview;
        private ImageButton imageButton,imageButton2;

        public VideoViewholder(@NonNull View itemView) {
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
                            videosfragments.saveVideo(statusModel);
                        } catch (IOException e) {
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
                        videosfragments.shareVideo(statusModel);
                    }
                }
            });

        }
    }
}
