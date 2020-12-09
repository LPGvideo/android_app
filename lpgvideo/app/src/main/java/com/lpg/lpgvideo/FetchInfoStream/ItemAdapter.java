package com.lpg.lpgvideo.FetchInfoStream;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lpg.lpgvideo.MainActivity;
import com.lpg.lpgvideo.PlayRemoteVideo.PlayVideoActivity;
import com.lpg.lpgvideo.R;

import java.util.ArrayList;
import java.util.List;


public class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {

    private List<Item> mItemArrayList;
    private View mView;

    public ItemAdapter(List<Item> itemList) {
        mItemArrayList = new ArrayList<>();
        mItemArrayList.addAll(itemList);
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item,parent,false);
        ItemHolder holder = new ItemHolder(mView);//用从layout获取view初始化一个holder
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Item item = mItemArrayList.get(position);
        holder.name.setText(item.studentId);
        holder.author.setText(item.username);
        String shortTime = item.time.substring(0,10);
        holder.time.setText(shortTime);
        holder.cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PlayVideoActivity.class);
                intent.putExtra("videoUrl",item.getVideoUrl());
                v.getContext().startActivity(intent);
            }
        });
        //RequestOptions options = new RequestOptions();

        Glide.with(mView.getContext()).load(item.imageUrl).into(holder.cover);
    }

    @Override
    public int getItemCount() {
        return mItemArrayList.size();
    }

    //自定义方法：按照传入的筛选准则更改mItems并通知
    public void dataChangedHandler(List<Item> newItems) {
        mItemArrayList.clear();
//        mItems = newItems;
        mItemArrayList.addAll(newItems);
        notifyDataSetChanged();
    }
}
