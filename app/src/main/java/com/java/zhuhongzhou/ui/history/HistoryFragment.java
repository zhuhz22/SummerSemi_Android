package com.java.zhuhongzhou.ui.history;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.java.zhuhongzhou.R;
import com.java.zhuhongzhou.database.DatabaseHelper;
import com.java.zhuhongzhou.datatypes.News;
import com.java.zhuhongzhou.ui.newsfragment.DetailFragment;
import com.java.zhuhongzhou.ui.newsfragment.NewsAdapter;
import java.util.ArrayList;
import java.util.Objects;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private ArrayList<News> newsArrayList = new ArrayList<>();
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.history_and_favoutire_list, container, false);

        /**设置recyclerView*/
        recyclerView = view.findViewById(R.id.news_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        databaseHelper = DatabaseHelper.getInstance(getContext());
        adapter = new NewsAdapter(newsArrayList,getContext(),databaseHelper);
        /**传递详情页具体信息*/
        adapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemClick(int position) {
                News news=newsArrayList.get(position);
                /**点击进入详情页*/
                DetailFragment detailFragment = new DetailFragment();
                Bundle bundle=new Bundle();
                bundle.putString("publishTime",news.getPublishTime());
                bundle.putString("publisher",news.getPublisher());
                bundle.putString("video",news.getVideo());
                bundle.putString("title",news.getTitle());
                bundle.putString("content",news.getContent());
                bundle.putString("category",news.getCategory());
                bundle.putStringArrayList("image",news.getImageList());
                bundle.putString("image_raw",news.getImage());
                detailFragment.setArguments(bundle);
                try{
                    getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer,detailFragment, null)
                            .addToBackStack(null)
                            .commit();
                }
                catch (Exception e){e.printStackTrace();}
            }
        });
        recyclerView.setAdapter(adapter);

        /**调用数据库初始化新闻列表*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<News> list=databaseHelper.getNewsListByTitleList("History",databaseHelper.getTitleListOfTable("History"));

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (!Objects.isNull(list))
                            {
                                newsArrayList = list;
                                adapter.setNewsArrayList(newsArrayList);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        }).start();
        return view;
    }
}
