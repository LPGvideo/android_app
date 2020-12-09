package com.lpg.lpgvideo.FetchInfoStream;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lpg.lpgvideo.R;

public class SearchLayout extends FrameLayout {
    private OnEditSearchViewListener mListener;
    EditText input;
    public SearchLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public SearchLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SearchLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void setOnEditSearchViewListener(OnEditSearchViewListener listener) {
        mListener = listener;
    }

    private void init() {
        inflate(getContext(), R.layout.layout_search,this);
        input = findViewById(R.id.search_value);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                mListener.afterChanged(s.toString());
            }
        });
    }
    //该接口在主窗口中实现，需要对EditText中的文本做出响应
    public interface OnEditSearchViewListener {
        void afterChanged(String text);
    }
}
