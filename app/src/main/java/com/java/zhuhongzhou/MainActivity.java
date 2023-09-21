package com.java.zhuhongzhou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.java.zhuhongzhou.database.DatabaseHelper;
import com.java.zhuhongzhou.ui.favorites.FavoritesFragment;
import com.java.zhuhongzhou.ui.history.HistoryFragment;
import com.java.zhuhongzhou.ui.news.NewsCategory;
import com.java.zhuhongzhou.ui.search.SearchFragment;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NewsCategory.OnDataPass {
    private BottomNavigationView bottomNavigationView;
    /**
     * 四大主界面之间传参所需参数
     * */
    public ArrayList<String>chosenCategory=new ArrayList<>();
    /**
     *传参之一 ：将NewsCategory中的数据传输到MainActivity
     */
    @Override
    public void onDataPass(ArrayList<String> chosenCategory) {
        this.chosenCategory=chosenCategory;
    }

    /**
     * Mainactivity的onCreate方法
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * 导航分页，进入四大主界面
         * 四大主界面都用fragment实现
         */
        bottomNavigationView = findViewById(R.id.contents);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment;
                if(item.getItemId()==R.id.news) {
                    /**
                     * 传参之一：Mainactivity将上一次NewsCategory传来的参数封装在Bundle中，以此构造新的NewsCategory，从而恢复原先状态
                     * */
                    selectedFragment=new NewsCategory();
                    Bundle bundle=new Bundle();
                    bundle.putStringArrayList("category",chosenCategory);
                    selectedFragment.setArguments(bundle);
                }
                else if(item.getItemId()==R.id.search) selectedFragment=new SearchFragment();
                else if(item.getItemId()==R.id.favorites) selectedFragment=new FavoritesFragment();
                else if(item.getItemId()==R.id.history) selectedFragment=new HistoryFragment();
                else selectedFragment=null;
                if (!Objects.isNull(selectedFragment)) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, selectedFragment)
                            .addToBackStack(null)
                            .commit();
                }
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.news);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (!Objects.isNull(DatabaseHelper.getInstance(this))) {
            DatabaseHelper.getInstance(this).clearData();
        }
    }

}

//TODO 依赖库  null特判  super
//TODO 加说明：1.依赖安卓回退键；2.日期的左闭右开；3.搜索日期的英文"-"
