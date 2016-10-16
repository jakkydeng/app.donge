package com.example.hujiahong.hujiahong;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


/**
 * 展示职责
 * Created by hujiahong on 16/10/16.
 */

public class ShowResponsibilitiesActivity extends MyBaseActivity{
    private TextView show_word;
    private String key;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.navigationBarLayout.setVisibility(View.VISIBLE);
        getIntentData();
        initView();
        loadData();
    }

    private void loadData() {
        if (key.equals("bangongshi")){
            super.top_textview.setText("办公室");
            show_word.setText("这个是个办公室hahahahahaahahahah这个是个办公室");
        }
    }

    private void  initView() {
        View mainView = LayoutInflater.from(this).inflate(R.layout.show_responsibilities, super.mainlayout, false);
        super.mainlayout.addView(mainView);
        show_word = (TextView) mainView.findViewById(R.id.show_word);
    }

    public void getIntentData() {
        key = getIntent().getStringExtra("key");
    }
}
