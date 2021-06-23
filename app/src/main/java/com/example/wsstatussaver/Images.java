package com.example.wsstatussaver;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.wsstatussaver.Adapters.ImageAdapter;
import com.example.wsstatussaver.Models.StatusModel;
import com.example.wsstatussaver.Utils.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class Images extends Fragment {

    RecyclerView imgRecyclerview;
    ArrayList<StatusModel> imageArrayList;
    LinearLayout linearLayout;
    Handler handler = new Handler();

    public Images() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_images, container, false);
       return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imgRecyclerview=view.findViewById(R.id.imageRecyclerView);
        linearLayout=view.findViewById(R.id.linearlayout);

        imgRecyclerview.setHasFixedSize(true);
        imageArrayList = new ArrayList<>();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
        imgRecyclerview.setLayoutManager(gridLayoutManager);

        getImageStatus();
    }

    private void getImageStatus(){
        if(Constants.STATUS_DIRECTORY.exists()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    File[] statusFiles = Constants.STATUS_DIRECTORY.listFiles();
                    if (statusFiles!=null && statusFiles.length>0){
                        Arrays.sort(statusFiles);
                        for (final File statusFile:statusFiles){
                            StatusModel statusModel = new StatusModel(statusFile,statusFile.getName(),statusFile.getAbsolutePath());
                            statusModel.setThumbnail(getThumbnail(statusModel));
                            if (!statusModel.isVideo()){
                                imageArrayList.add(statusModel);
                            }
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                ImageAdapter imageAdapter = new ImageAdapter(imageArrayList,getContext(),Images.this);
                                imgRecyclerview.setAdapter(imageAdapter);
                                imageAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                    else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),"No Any Statues Available",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }).start();
        }
    }

    private Bitmap getThumbnail(StatusModel statusModel) {
        if (statusModel.isVideo()){
            return ThumbnailUtils.createVideoThumbnail(statusModel.getFile().getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
        }
        else {
            return ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(statusModel.getFile().getAbsolutePath()),Constants.THUMBSIZE,Constants.THUMBSIZE);
        }
    }

    public void saveImage(StatusModel statusModel) throws IOException {
        File file = new File(Constants.APP_DIR);
        if (!file.exists()){
            file.mkdir();
        }
        File destFile = new File(file+File.separator + statusModel.getTitle());
        if (destFile.exists()){
            destFile.delete();
        }

        copyFile(statusModel.getFile(),destFile);
        Toast.makeText(getContext(),"Saved Successful",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(destFile));
        getActivity().sendBroadcast(intent);
    }

    private void copyFile(File file, File destFile) throws IOException {
        if (!destFile.getParentFile().exists()){
            destFile.getParentFile().mkdirs();
        }
        if (!destFile.exists()){
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        source = new FileInputStream(file).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        destination.transferFrom(source,0,source.size());

        source.close();
        destination.close();
    }

    public void shareImage(StatusModel statusModel) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/jpg");
        intent.putExtra(Intent.EXTRA_STREAM,Uri.parse(new File(statusModel.getPath()).toString()));
        startActivity(intent);
        //startActivity(Intent.createChooser(intent, getResources().getText(R.string.send_to)));
    }
}
