package com.java.zhuhongzhou.urladapter;

import android.util.Log;

import androidx.annotation.Nullable;

import com.java.zhuhongzhou.datatypes.News;

import java.util.ArrayList;
import java.util.Objects;

public class NewsFetcher{
    private GetUrl getUrl;
    private ArrayList<News> newsArrayList;
    public NewsFetcher(){
        getUrl=new GetUrl();
    }
    public NewsFetcher(String size, String startDate, String endDate, String words, String categories,int page){
        getUrl=new GetUrl(size, startDate,endDate, words,categories,page);
    }
    public void setUrlInterface(String size, String startDate, String endDate, String words, String categories,int page){
        getUrl.setSize(size);
        getUrl.setStartDate(startDate);
        getUrl.setEndDate(endDate);
        getUrl.setWords(words);
        getUrl.setCategories(categories);
        getUrl.setPage(page);
    }
    public void run(){
        getUrl.sendRequest();
        String json=getUrl.getResponseJson();
        JsonParser jsonParser=new JsonParser();
        ArrayList<News> ret=jsonParser.stringToNews(json);
        if(Objects.isNull(ret))
        {
            newsArrayList=null;
        }//TODO null处理
        else newsArrayList=ret;
    }
    public ArrayList<News> getNewsList() {
        return newsArrayList;
    }
}