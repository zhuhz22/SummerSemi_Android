package com.java.zhuhongzhou.ui.newsfragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.java.zhuhongzhou.R;
import com.java.zhuhongzhou.database.DatabaseHelper;
import com.java.zhuhongzhou.datatypes.News;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * 绑定新闻卡片，并根据传入的新闻ArrayList来实例化所有新闻卡片
 * */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder>{
    private ArrayList<News> newsArrayList=new ArrayList<>();
    public Context context;
    public DatabaseHelper databaseHelper;
    /**点击卡片，进入详情页*/
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    /**ViewHolder*/
    public class NewsViewHolder extends RecyclerView.ViewHolder {
        public TextView title, timeAndPublisher;
        public ImageView image;
        public VideoView videoView;


        public NewsViewHolder(View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.news_title);
            timeAndPublisher=itemView.findViewById(R.id.time_and_publisher);
            image=itemView.findViewById(R.id.image);
            videoView=itemView.findViewById(R.id.lst_video);
            /**设置点击跳转*/
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }


    public NewsAdapter(ArrayList<News> newsArrayList,Context context,DatabaseHelper databaseHelper){
        this.newsArrayList=newsArrayList;
        this.context=context;
        this.databaseHelper=databaseHelper;
    }
    public NewsAdapter(){}

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.news_list_card, parent, false);
        return new NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder,  int position) {
        News news=newsArrayList.get(position);
        holder.title.setText(news.getTitle());
        holder.timeAndPublisher.setText(news.getPublisher()+"  "+news.getPublishTime());
        /**历史记录灰名*/
        ArrayList<String> titleList=databaseHelper.getTitleListOfTable("History");
        if(titleList.contains(news.getTitle()))
        {
            holder.title.setTextColor(Color.GRAY);
            holder.timeAndPublisher.setTextColor(Color.GRAY);
        }
        else{//防止更新后题目虽变而字体仍灰
            holder.title.setTextColor(Color.BLACK);
            holder.timeAndPublisher.setTextColor(Color.BLACK);
        }
        /**加载图片*/
        ArrayList<String> imageList=news.getImageList() ;
        if(!imageList.isEmpty()&&!Objects.isNull(imageList)){
            String firstImage=imageList.get(0);
            if(!Objects.isNull(firstImage)) {
                    Glide.with(holder.image.getContext()).clear(holder.image);
                    holder.image.setImageDrawable(null);//清空，防止回收机制捣乱
                    RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);
                    Glide.with(holder.image.getContext())
                            .load(firstImage)
                            .apply(requestOptions)
                            .addListener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }
                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    holder.image.setVisibility(View.VISIBLE);
                                    return false;
                                }
                            })
                            .override(288,128)
                            .fitCenter()
                            .into(holder.image);
            }
        }
        /**加载视频*/
        String videoUrl=news.getVideo();
        if (!Objects.isNull(videoUrl) && !videoUrl.equals(""))
        {
            holder.videoView.setVisibility(View.VISIBLE);
            holder.videoView.setVideoPath(videoUrl);
            MediaController mediaController = new MediaController(context);
            mediaController.setAnchorView(holder.videoView);
            holder.videoView.setMediaController(mediaController);
            holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    holder.videoView.requestFocus();
                    holder.videoView.start();
                }
            });
            holder.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    holder.videoView.seekTo(0);
                    holder.videoView.start();
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return newsArrayList.size();
    }

   @Override
    public int getItemViewType(int position) {//TODO 这样会降低性能，那应该怎样避免各条目之间图片错乱问题？
        return position;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setNewsArrayList(ArrayList<News> newNewsArrayList) {
       this.newsArrayList = newNewsArrayList;
       notifyDataSetChanged();
    }
}
