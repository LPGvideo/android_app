package com.lpg.lpgvideo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lpg.lpgvideo.FetchInfoStream.FetchPojo;
import com.lpg.lpgvideo.FetchInfoStream.FetchService;
import com.lpg.lpgvideo.FetchInfoStream.Item;
import com.lpg.lpgvideo.FetchInfoStream.ItemAdapter;
import com.lpg.lpgvideo.FetchInfoStream.SearchLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private List<Item> mItems = new ArrayList<>();
    private final Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api-sjtu-camp.bytedance.com/").
            addConverterFactory(GsonConverterFactory.create()).build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Recycler View
        SearchLayout searchLayout = findViewById(R.id.search_layout);
        EditText searchValue = findViewById(R.id.search_value);
        Button cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchValue.setText("");
            }
        });
        Log.d("Main", "finish layout init");
        //对RecyclerView进行一系列配置
        initItems();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);//初始化一个布局管理器
        recyclerView.setLayoutManager(linearLayoutManager);//设置布局管理器
        ItemAdapter itemViewAdapter = new ItemAdapter(mItems);//初始化一个适配器
        recyclerView.setAdapter(itemViewAdapter);//设置适配器

        searchLayout.setOnEditSearchViewListener(new SearchLayout.OnEditSearchViewListener() {
            @Override
            public void afterChanged(String text) {
                List<Item> tmpItemLists = new ArrayList<>();
                for (Item item : mItems) {
                    if (item.getStudentId().contains(text) || item.getUsername().contains(text)) {
                        tmpItemLists.add(item);
                    }
                }
                itemViewAdapter.dataChangedHandler(tmpItemLists);
            }
        });

    }

    private void initItems() {
        FetchService fetchService =retrofit.create(FetchService.class);
        Call<FetchPojo> call =fetchService.fetchVideoList();
        call.enqueue(new Callback<FetchPojo>() {
            @Override
            public void onResponse(Call<FetchPojo> call, Response<FetchPojo> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                final FetchPojo fetchPojo = response.body();
                mItems = fetchPojo.getVideoLists();
            }

            @Override
            public void onFailure(Call<FetchPojo> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}