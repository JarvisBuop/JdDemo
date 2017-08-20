package com.jd.jarvisdemo1.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jd.jarvisdemo1.R;
import com.jd.jarvisdemo1.bean.IntentData;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView mRecy;
    MybaseAdapter mAdapter;
    List<IntentData> mDataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initData() {
        mDataList.add(new IntentData("测试searchview",R.mipmap.ic_help,SearchAct.class));

        mAdapter.notifyDataSetChanged();
    }

    private void initView() {
        mDataList = new ArrayList();
        mRecy = (RecyclerView) findViewById(R.id.common_recyclerview);
        mRecy.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MybaseAdapter();
        mRecy.setAdapter(mAdapter);
    }

    private class MybaseAdapter extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(MainActivity.this).inflate(android.R.layout.activity_list_item, parent, false);
            return new MyViewholder(inflate);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof MyViewholder){
                final IntentData intentData = mDataList.get(position);
                ((MyViewholder) holder).txt.setText(intentData.title);
                ((MyViewholder) holder).img.setImageResource(intentData.ids);
                ((MyViewholder) holder).txt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this,intentData.intentTarget);
                        startActivity(intent);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mDataList == null?0:mDataList.size();
        }
    }

    private class MyViewholder extends RecyclerView.ViewHolder {
        private TextView txt;
        private ImageView img;
        public MyViewholder(View itemView) {
            super(itemView);
            init(itemView);
        }

        private void init(View itemView) {
            txt = (TextView) itemView.findViewById(android.R.id.text1);
            img = (ImageView) itemView.findViewById(android.R.id.icon);
        }
    }
}
