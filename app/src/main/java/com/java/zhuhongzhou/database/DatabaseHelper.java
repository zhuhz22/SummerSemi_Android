package com.java.zhuhongzhou.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.java.zhuhongzhou.datatypes.News;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper instance;
    private static final String DATABASE_NAME = "NewsStorage";
    private static final int DATABASE_VERSION = 1;
    /**单例模式，构造函数为private*/
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    /**获得单例*/
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public synchronized void onCreate(SQLiteDatabase db) {
        String createHistory="CREATE TABLE History(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Title TEXT ,Time TEXT ,Content TEXT,Publisher TEXT,Image TEXT,Video TEXT,Category TEXT)";
        String createFavorite="CREATE TABLE Favorite(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Title TEXT ,Time TEXT ,Content TEXT,Publisher TEXT,Image TEXT,Video TEXT,Category TEXT )";
        db.execSQL(createHistory);
        db.execSQL(createFavorite);
    }

    @Override
    public synchronized void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //按：此处本应该处理数据库升级的数据迁移，但大作业apk一旦生产则实际上不会再次迭代了，因此省略不写
    }

    /**向tableName加入条目*/
    public synchronized void insertDataTo(News news,String tableName) {

        String title=news.getTitle();
        String publishTime= news.getPublishTime();
        String publisher=news.getPublisher();
        String content=news.getContent();
        String category=news.getCategory();
        String image=news.getImage();
        String video=news.getVideo();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(tableName, null, "Title = ?",new String[]{title},null,null,null);
        if(cursor.moveToFirst()) {//只有当没有存储这条新闻时才存储，避免历史记录重复存储
            String[] titleArr = {title};
            db.delete(tableName, "Title = ?", titleArr);
        }   // 删除旧有的！
        ContentValues values = new ContentValues();
        values.put("Title", title);
        values.put("Time", publishTime);
        values.put("Content", content);
        values.put("Publisher",publisher);
        values.put("Category",category);
        values.put("Image",image);
        values.put("Video",video);
        db.insert(tableName, null, values);
        cursor.close();
        db.close();
    }
    /**根据title删除tableName中的条目*/
    public synchronized void deleteDataOf(String title,String tableName) {
        SQLiteDatabase db = getWritableDatabase();
        String[] titleArr = {title};
        db.delete(tableName, "Title = ?", titleArr);
        db.close();
    }
    /**提取所有tableName中的新闻标题，用于灰名和两大界面主键索引引*/
    @SuppressLint("Range")
    public synchronized ArrayList<String> getTitleListOfTable(String tableName) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> titleList = new ArrayList<>();
        String[]titleArr= {"Title"};
        Cursor cursor = db.query(tableName, titleArr, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                titleList.add(cursor.getString(cursor.getColumnIndex("Title")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        if(!titleList.isEmpty()&&!Objects.isNull(titleList)) Collections.reverse(titleList);
        return titleList;
    }
    /**根据标题获得新闻*/
    @SuppressLint("Range")
    private synchronized News getNewsByTitle(String tableName, String title) {
        SQLiteDatabase db = getReadableDatabase();
        String[] titleValue= {title};
        Cursor cursor = db.query(tableName, null, "Title = ?",titleValue,null,null,null);
        News news = new News();
        if (cursor.moveToFirst()) {
            String publishTime = cursor.getString(cursor.getColumnIndex("Time"));
            String content=cursor.getString(cursor.getColumnIndex("Content"));
            String publisher=cursor.getString(cursor.getColumnIndex("Publisher"));
            String category =cursor.getString(cursor.getColumnIndex("Category"));
            String image=cursor.getString(cursor.getColumnIndex("Image"));
            String video=cursor.getString(cursor.getColumnIndex("Video"));
            news=new News(title,publishTime,publisher,content,category,image,video);

        }
        cursor.close();
        db.close();
        return news;
    }
    public synchronized ArrayList<News> getNewsListByTitleList(String tableName,ArrayList<String> titleList){
        ArrayList<News> newslist=new ArrayList<>();
        for(String title:titleList){
            newslist.add(getNewsByTitle(tableName,title));
        }
        return newslist;
    }
    /**清空数据库*/
    public synchronized void clearData() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("History", null, null);
        db.delete("Favorite", null, null);
        db.close();
    }
}