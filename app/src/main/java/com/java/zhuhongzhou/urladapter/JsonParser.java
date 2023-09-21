package com.java.zhuhongzhou.urladapter;

import com.google.gson.Gson;
import com.java.zhuhongzhou.datatypes.News;
import com.java.zhuhongzhou.datatypes.NewsList;

import java.util.ArrayList;
import java.util.Objects;

public class JsonParser {
    private NewsList parse(String newslist){
        if(!Objects.isNull(newslist)){//TODO null判定系列==>
        Gson gson=new Gson();
        return gson.fromJson(newslist, NewsList.class);
        }
        else return null;//TODO 要不要返回一个new NewsList()?
    }
    private ArrayList<News> newsProvider(NewsList newsList){
        if(!Objects.isNull(newsList)){
            return newsList.data;
        }//TODO 判断！=null合不合适？
        else return new ArrayList<>();//TODO null合不合适？
    }
    public ArrayList<News> stringToNews(String newsList){
        return newsProvider(parse(newsList));
    }
}
//TODO 全部修改 ！=null 为 Ojects.isnull() =====>  务必！！！！