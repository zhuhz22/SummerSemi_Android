package com.java.zhuhongzhou.ui.news;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.java.zhuhongzhou.datatypes.News;
import com.java.zhuhongzhou.ui.newsfragment.NewsListFragment;

import java.util.ArrayList;

public class CategoryAdapter extends FragmentStateAdapter {
    private int numOfCategoryies;
    private ArrayList<String>chosenCategory=new ArrayList<>();
    public CategoryAdapter(Fragment fragment,ArrayList<String>chosenCategory) {
        super(fragment);
        numOfCategoryies=1;
        this.chosenCategory=chosenCategory;
    }

    public void setNumOfCategoryies(int numOfCategoryies) {
        this.numOfCategoryies = numOfCategoryies;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return NewsListFragment.newInstance("");
            default:
                return NewsListFragment.newInstance(chosenCategory.get(position-1));
        }

    }

    @Override
    public int getItemCount() {
        return numOfCategoryies;
    }
}
