package com.lpg.lpgvideo.FetchInfoStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lpg.lpgvideo.MainActivity;
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
        holder.time.setText(item.time);

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
