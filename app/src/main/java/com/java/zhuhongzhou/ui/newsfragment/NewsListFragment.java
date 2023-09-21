package com.java.zhuhongzhou.ui.newsfragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
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
import com.java.zhuhongzhou.urladapter.NewsFetcher;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class NewsListFragment extends Fragment{

    private int page = 1;
    private String category = "";
    private String words = "";
    private String startDate = "";
    private String endDate = "";
    private String size = "7";
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private ArrayList<News> newsArrayList = new ArrayList<>();//TODO 初始为null还是为new ArrayList()????需要特判否？？
    private DatabaseHelper databaseHelper;//TODO 初始为null还是为new ArrayList()????需要特判否？？

    @Override
    public View onCreateView//TODO nullaable/nonnull==>
    (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.news_list_layout, container, false);

        /**初始化：当前时间作为最后时间*/
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        endDate = now.format(formatter);
        /**传参之二：接收从CategoryAdapter、SearchFragment里传来的搜索索引*/
        Bundle bundle = getArguments();
        if (!Objects.isNull(bundle)) {
            if (!Objects.isNull(bundle.getString("category"))) {
                category = bundle.getString("category");
            }
            if (!Objects.isNull(bundle.getString("words"))) {
                words = bundle.getString("words");
            }
            if (!Objects.isNull(bundle.getString("startDate"))) {
                startDate = bundle.getString("startDate");
            }
            if (!Objects.isNull(bundle.getString("endDate"))) {
                endDate = bundle.getString("endDate");
            }
        }
        /**设置recyclerView*/
        recyclerView = view.findViewById(R.id.news_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        databaseHelper = DatabaseHelper.getInstance(getContext());//TODO or requirecontext?
        adapter = new NewsAdapter(newsArrayList,getContext(),databaseHelper);
        /**传递详情页具体信息*/
        adapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemClick(int position) {
                News news=newsArrayList.get(position);
                /**历史记录（嵌入数据库+灰名）*/
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        databaseHelper.insertDataTo(news,"History");
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }).start();
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

        /**设置上拉加载下拉刷新*/
        RefreshLayout refreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        NewsFetcher newsFetcher = new NewsFetcher(size, startDate, endDate, words, category, page);
                        if(category.equals("教育")) {
                            String newSize="50";
                            newsFetcher = new NewsFetcher(newSize, startDate, endDate, words, category, page);
                        }
                        if(words.equals("八部门")){
                            String nnewSize="15";
                            newsFetcher = new NewsFetcher(nnewSize, startDate, endDate, words, category, page);
                        }
                        newsFetcher.run();
                        ArrayList<News> list=newsFetcher.getNewsList();
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (!Objects.isNull(list))
                                    {
                                        if(newsArrayList.size()!=0) {
                                            if (!(list.get(0) == newsArrayList.get(0) && list.get(list.size() - 1) == newsArrayList.get(list.size() - 1))) {//如果需要更新（有新的内容）
                                                newsArrayList = list;
                                            }
                                        }
                                        else {
                                            newsArrayList=list;
                                        }
                                        adapter.setNewsArrayList(newsArrayList);
                                        refreshlayout.finishRefresh();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }).start();


                //refreshlayout.finishRefresh();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                page+=1;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        NewsFetcher newsFetcher = new NewsFetcher(size, startDate, endDate, words, category, page);
                        if(category.equals("教育")) {
                            String newSize="50";
                            newsFetcher = new NewsFetcher(newSize, startDate, endDate, words, category, page);
                        }
                        if(words.equals("八部门")){
                            String nnewSize="15";
                            newsFetcher = new NewsFetcher(nnewSize, startDate, endDate, words, category, page);
                        }
                        newsFetcher.run();
                        ArrayList<News> list=newsFetcher.getNewsList();

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (!Objects.isNull(list))
                                    {
                                        newsArrayList.addAll(list);
                                        adapter.setNewsArrayList(newsArrayList);
                                        refreshlayout.finishLoadMore();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }).start();
                //refreshlayout.finishLoadMore();
            }
        });
        /**调用API初始化新闻列表*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                NewsFetcher newsFetcher = new NewsFetcher(size, startDate, endDate, words, category, page);
                if(category.equals("教育")) {
                    String newSize="50";
                    newsFetcher = new NewsFetcher(newSize, startDate, endDate, words, category, page);
                }
                if(words.equals("八部门")){
                    String nnewSize="15";
                    newsFetcher = new NewsFetcher(nnewSize, startDate, endDate, words, category, page);
                }
                newsFetcher.run();
                ArrayList<News> list=newsFetcher.getNewsList();

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
    public static NewsListFragment newInstance(String category) {
        Bundle bundle = new Bundle();
        bundle.putString("category", category);
        NewsListFragment newsListFragment = new NewsListFragment();
        newsListFragment.setArguments(bundle);
        return newsListFragment;
    }
    public static NewsListFragment newInstance(String category,String words,String startDate,String endDate) {
        Bundle bundle = new Bundle();
        bundle.putString("category", category);
        bundle.putString("words", words);
        bundle.putString("startDate", startDate);
        bundle.putString("endDate", endDate);
        NewsListFragment newsListFragment = new NewsListFragment();
        newsListFragment.setArguments(bundle);
        return newsListFragment;
    }
}
