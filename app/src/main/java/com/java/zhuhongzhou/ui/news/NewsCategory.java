package com.java.zhuhongzhou.ui.news;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.java.zhuhongzhou.R;

import java.util.ArrayList;

public class NewsCategory extends Fragment {

    public ArrayList<String> chosenCategory=new ArrayList<>();
    /**
     *传参之一 ：将NewsCategory中的数据传输到MainActivity
     */
    public interface OnDataPass {
        void onDataPass(ArrayList<String> chosenCategory);
    }
    private OnDataPass dataPasser;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDataPass) {
            dataPasser = (OnDataPass) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnDataPassListener");
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_layout, container, false);

        /**传参之二 ：接收从CategoryChoice/MainActivity传来的chosenCategory*/
        Bundle categ=getArguments();
        if(categ!=null) {
            chosenCategory = categ.getStringArrayList("category");
        }
        /**传参之一 ：将chosenCategory传给Mainactivity*/
        dataPasser.onDataPass(chosenCategory);
        /**设置Tablayout*/
        TabLayout tabLayout = view.findViewById(R.id.tab_category);
        ViewPager2 viewPager= view.findViewById(R.id.view_pager_category);
        CategoryAdapter categoryAdapter=new CategoryAdapter(this,chosenCategory);
        categoryAdapter.setNumOfCategoryies(chosenCategory.size()+1);
        viewPager.setAdapter(categoryAdapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if(position==0) tab.setText("全部");
            else tab.setText(chosenCategory.get(position-1));
        }).attach();

        TabLayout.Tab tab = tabLayout.getTabAt(0); //默认“全部”
        tab.select();


        /**切换至CategoryChoice*/
        Bundle bundle=new Bundle();
        /**传参之二：向CategoryChoice传递chosenCategory*/
        bundle.putStringArrayList("category",chosenCategory);
        CategoryChoice categoryChoice=new CategoryChoice();
        categoryChoice.setArguments(bundle);
        CheckBox checkBox=view.findViewById(R.id.category_choice);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer,categoryChoice, null)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
        return view;
    }


}
