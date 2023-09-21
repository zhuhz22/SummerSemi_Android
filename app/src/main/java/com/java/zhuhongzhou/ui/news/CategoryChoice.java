package com.java.zhuhongzhou.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.java.zhuhongzhou.R;

import java.util.ArrayList;

public class CategoryChoice extends Fragment {
    public class Category{
        public String name;
        public Button button;
        Category(){}
        Category(String name,Button button){
            this.name=name;
            this.button=button;
        }
    }
    private GridLayout chosen;
    private GridLayout notChosen;
    public ArrayList<Category> categoryList=new ArrayList<>();
    public ArrayList<String> chosenCategory=new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_choosing, container, false);

        /**传参之二：接收从NewsCategory传来的chosenCategory*/
        Bundle categ=getArguments();
        if(categ!=null)  chosenCategory=categ.getStringArrayList("category");
        /**设置选择category*/
        chosen  = view.findViewById(R.id.chosen);
        notChosen = view.findViewById(R.id.not_chosen);
        categoryList.add(new Category("娱乐",view.findViewById(R.id.entertainment)));
        categoryList.add(new Category("军事",view.findViewById(R.id.military)));
        categoryList.add(new Category("教育",view.findViewById(R.id.education)));
        categoryList.add(new Category("社会",view.findViewById(R.id.society)));
        categoryList.add(new Category("财经",view.findViewById(R.id.economics)));
        categoryList.add(new Category("文化",view.findViewById(R.id.culture)));
        categoryList.add(new Category("科技",view.findViewById(R.id.tech)));
        categoryList.add(new Category("健康",view.findViewById(R.id.health)));
        categoryList.add(new Category("汽车",view.findViewById(R.id.car)));
        categoryList.add(new Category("体育",view.findViewById(R.id.pe)));
        //TODO:担忧：是传引用还是复制？
        for(Category cate:categoryList){
            //初始化
            if(chosenCategory.contains(cate.name)){
                notChosen.removeView(cate.button);
                chosen.addView(cate.button);
            }

            cate.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getParent() == chosen) {
                        chosen.removeView(v);
                        notChosen.addView(v);
                        if(chosenCategory.contains(cate.name))//TODO 检查xml id匹配问题&&这一段的逻辑
                            chosenCategory.remove(cate.name);
                    } else if (v.getParent() == notChosen) {
                        notChosen.removeView(v);
                        chosen.addView(v);
                        if(!chosenCategory.contains(cate.name))
                            chosenCategory.add(cate.name);
                        //Log.d("List", Arrays.toString(chosenCategory.toArray()));
                    }
                }
            });
        }


        /**传参之二：返回NewsCategory的参数传递*/

        ImageButton return_button =view.findViewById(R.id.return_button);
        return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                //Log.d("List", Arrays.toString(chosenCategory.toArray()));
                bundle.putStringArrayList("category",chosenCategory);
                NewsCategory newsCategory=new NewsCategory();
                newsCategory.setArguments(bundle);
                //Log.d("TAG",bundle.toString());
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer,newsCategory, null)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }



    public CategoryChoice(){
        super();
    }//TODO 必要？

}
//TODO 各个extend是否需要显式super？