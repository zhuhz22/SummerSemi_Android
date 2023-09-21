package com.java.zhuhongzhou.urladapter;

import android.util.Log;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetUrl {
    private String size = "7";
    private String startDate = "";
    private String endDate = "";
    private String words = "";
    private String categories = "";
    private String responseJson;
    private int page=1;
    public GetUrl() {
    }

    public GetUrl(String size, String startDate, String endDate, String words, String categories,int page) {
        this.size = size;
        this.startDate = startDate;
        this.endDate = endDate;
        this.words = words;
        this.categories = categories;
        this.page=page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public int getPage() {
        return page;
    }

    public String getSize() {
        return size;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getWords() {
        return words;
    }

    public String getCategories() {
        return categories;
    }


    public String getmyUrl() {

        String myurl = "https://api2.newsminer.net/svc/news/queryNewsList?size=" + size + "&startDate=" + startDate + "&endDate=" + endDate
                + "&words=" + words + "&categories=" + categories+"&page="+page;
        Log.d("getUrl", myurl);
        return myurl;
    }

    public void sendRequest() {

        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().get().url(getmyUrl()).build();
            Call call = client.newCall(request);
            Response response = call.execute();
            responseJson = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public String getResponseJson() {
        return responseJson;
    }

}
//TODO 是否必要在所有函数上都加入异常处理以防崩溃？
//TODO 务必处理：特判list/string==null，避免空指针异常，切切！
//TODO 务必处理：防止fragment等等空指针异常