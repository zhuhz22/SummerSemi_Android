package com.java.zhuhongzhou.datatypes;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class News {
    //TODO 是否需要初始化这些成员变量以防null？
    //todo 预防为null（万一网络接口出错，gson构造的出现null）
    public String image;
    public String publishTime;
    public String video;
    public String title;
    public String content;
    public String publisher;
    public String category;
    public News(){}
    public News(String title,String publishTime,String publisher,String content,String category,String image,String video){
        this.image=image;
        this.video=video;
        this.title=title;
        this.publishTime=publishTime;
        this.publisher=publisher;
        this.content=content;
        this.category=category;
    }

    public String getContent() {
        return content;
    }

    public String getCategory() {
        return category;
    }

    public String getImage() {
        return image;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public String getTitle() {
        return title;
    }

    public String getVideo() {
        return video;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public ArrayList<String> getImageList() {
        if (!Objects.equals(this.image, "[]") && !Objects.equals(this.image, "") && !Objects.isNull(this.image)) {//TODO 全体字符串改！=为equals;检查预处理
            String rawImageList = this.image.substring(1, this.image.length() - 1);
            String[] imageList = rawImageList.split(",");
            return new ArrayList<String>(Arrays.asList(imageList));
        }
        return new ArrayList<String>();//TODO or null？
    }
}