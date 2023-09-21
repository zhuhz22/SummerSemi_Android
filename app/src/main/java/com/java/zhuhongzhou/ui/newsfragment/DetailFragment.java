package com.java.zhuhongzhou.ui.newsfragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.java.zhuhongzhou.R;
import com.java.zhuhongzhou.database.DatabaseHelper;
import com.java.zhuhongzhou.datatypes.News;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DetailFragment extends Fragment {

    private MediaController mediaController;
    private MediaPlayer mediaPlayer;
    private boolean isPaused = false;
    private Handler handler;
    private boolean isStarred=false;
    private DatabaseHelper databaseHelper;//TODO 初始为null还是为new ArrayList()????需要特判否？？

    private News news;
    private String Title="";
    private String PublishTime="";
    private String Publisher="";
    private String Content="";
    private String Category="";
    private String Image="[]";
    private String Video="";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.detail_fragment, container, false);

        TextView title=view.findViewById(R.id.content_title);
        TextView publisher=view.findViewById(R.id.content_publisher_and_time);
        TextView content=view.findViewById(R.id.content_content);
        ImageView fst=view.findViewById(R.id.content_first_image);
        ImageView sec=view.findViewById(R.id.content_second_image);
        ImageView trd=view.findViewById(R.id.content_third_image);
        ImageView fth=view.findViewById(R.id.content_fourth_image);
        VideoView videoView = view.findViewById(R.id.content_video);

        Bundle bundle=getArguments();

        if (!Objects.isNull(bundle)) {
            /**文字部分*/
            if (!Objects.isNull(bundle.getString("title"))) {
                title.setText(bundle.getString("title"));
                Title=bundle.getString("title");
            }
            if ((!Objects.isNull(bundle.getString("publisher"))) && (!Objects.isNull(bundle.getString("publishTime")))) {
                publisher.setText(bundle.getString("publisher") + " " + bundle.getString("publishTime"));
                Publisher=bundle.getString("publisher");
                PublishTime=bundle.getString("publishTime");
            }
            if (!Objects.isNull(bundle.getString("content"))) {
                content.setText(bundle.getString("content"));
                Content=bundle.getString("content");
            }
            /**图片部分*/
            if (!Objects.isNull(bundle.getStringArrayList("image"))) {
                    ArrayList<String> imageList = bundle.getStringArrayList("image");
                    int len = imageList.size();
                    if (len != 0) {
                            putInImg(imageList.get(0), fst);
                            /*if (len >= 2) {
                                putInImg(imageList.get(1), sec);
                                if (len >= 3) {
                                    putInImg(imageList.get(2), trd);
                                    if (len >= 4) {
                                        putInImg(imageList.get(3), fth);
                                    }
                                }
                            }*/
                }
            }

            if(!Objects.isNull(bundle.getString("image_raw"))){
                Image=bundle.getString("image_raw");
            }
            /**视频部分*/
            if (!Objects.isNull(bundle.getString("video")) && !bundle.getString("video").equals("")) {

                Video=bundle.getString("video");
                videoView.setVisibility(View.VISIBLE);
                videoView.setVideoPath(bundle.getString("video"));
                MediaController mediaController = new MediaController(getContext());
                mediaController.setAnchorView(videoView);
                videoView.setMediaController(mediaController);
                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        videoView.requestFocus();
                        videoView.start();
                    }
                });
                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        videoView.seekTo(0);
                        videoView.start();
                    }
                });
            }

        }
        /**收藏*/
        news=new News(Title,PublishTime,Publisher,Content,Category,Image,Video);
        databaseHelper=DatabaseHelper.getInstance(getContext());//TODO or requirecontext?
        FloatingActionButton favoriteButton=view.findViewById(R.id.favorite_button);
        ArrayList<String> titleList=databaseHelper.getTitleListOfTable("Favorite");
        if(titleList.contains(news.getTitle()))
        {
            isStarred=true;
            favoriteButton.setColorFilter(Color.parseColor("#A39100"));
        }
        else{
            isStarred=false;
            favoriteButton.setColorFilter(Color.parseColor("#000000"));
        }
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStarred) {
                    favoriteButton.setColorFilter(Color.parseColor("#000000"));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            databaseHelper.deleteDataOf(news.getTitle(),"Favorite");
                        }
                    }).start();
                } else {
                    favoriteButton.setColorFilter(Color.parseColor("#A39100"));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            databaseHelper.insertDataTo(news,"Favorite");
                        }
                    }).start();
                }
                isStarred = !isStarred;
            }
        });

        return view;
    }

    /**图片加载w*/
    public void putInImg(String url,ImageView img){
        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(img.getContext())
                .load(url)
                .apply(requestOptions)
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        img.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .override(800,600)
                .fitCenter()
                .into(img);
    }
}
