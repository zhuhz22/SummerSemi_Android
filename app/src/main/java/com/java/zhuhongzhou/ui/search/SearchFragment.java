package com.java.zhuhongzhou.ui.search;


import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.java.zhuhongzhou.R;
import com.java.zhuhongzhou.datatypes.News;
import com.java.zhuhongzhou.ui.newsfragment.NewsListFragment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class SearchFragment  extends Fragment {
    private String category;
    private String startDate;
    private String endDate;
    private String words;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);//TODO 这句到底对不对？是否会反而无法加载真正的继承类的view？
        if (Objects.isNull(view)) {view = inflater.inflate(R.layout.search_layout, container, false);}


        TextView flag=view.findViewById(R.id.flag_flag);
        RadioGroup radioGroup = view.findViewById(R.id.radio_group);
        View finalView = view;//TODO 是否会影响生命周期？替代方案？
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton cateButton= finalView.findViewById(checkedId);
                category=cateButton.getText().toString();
            }
        });

        EditText startDateEdit = view.findViewById(R.id.edit_start_date);
        EditText endDateEdit = view.findViewById(R.id.edit_end_date);
        EditText wordsEdit = view.findViewById(R.id.edit_words);


        Button button=view.findViewById(R.id.search_start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endDate=endDateEdit.getText().toString();
                startDate=startDateEdit.getText().toString();
                words=wordsEdit.getText().toString();
                if(endDate.equals("")){
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    endDate = now.format(formatter);
                }
                if(Objects.isNull(category)) category="";
                NewsListFragment newsListFragment=NewsListFragment.newInstance(category,words,startDate,endDate);
                try{
                    getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer,newsListFragment, null)
                            .addToBackStack(null)
                            .commit();
                }
                catch (Exception e){e.printStackTrace();}
            }
        });
        return view;
    }
}
