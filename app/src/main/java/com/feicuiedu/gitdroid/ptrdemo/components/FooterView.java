package com.feicuiedu.gitdroid.ptrdemo.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.feicuiedu.gitdroid.ptrdemo.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * This view is used for "infinite scroll", when ListView scroll to its end, and we try to
 * fetch data in a background thread, this view is added to ListView via {@link android.widget.ListView#addFooterView(View)}.
 *
 * This view has three different status: show a progress bar, show a complete message, or show an error message.
 * When the error message is shown, user can retry loading more by clicking the FooterView.
 *
 * <p/>
 * 本视图用来实现“无穷滚动”功能，当ListView滚动到末尾，并且我们尝试在后台线程加载更多数据时，
 * 此视图会通过ListView的addFootView()方法被添加到ListView上。
 *
 * 此视图有三种不同的状态：展示一个加载中的进度条，显示一条所有数据加载完毕的信息，
 * 或者显示一条加载发生错误的信息。当错误信息被显示时，用户可以通过点击此FooterView来重试加载更多数据。
 */
public class FooterView extends FrameLayout {

    // 代表视图三种状态的静态常量值
    private static final int STATE_LOADING = 0;
    private static final int STATE_COMPLETE = 1;
    private static final int STATE_ERROR = 2;

    @Bind(R.id.progressBar) ProgressBar progressBar;

    @Bind(R.id.tv_complete) TextView tvComplete;

    @Bind(R.id.tv_error) TextView tvError;

    // 默认为加载中状态
    private int state = STATE_LOADING;

    public FooterView(Context context) {
        this(context, null);
    }

    public FooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.content_load_footer, this, true);
        init();
    }

    private void init() {
        ButterKnife.bind(this);
    }

    // 显示加载中
    public void showLoading() {
        state = STATE_LOADING;
        progressBar.setVisibility(VISIBLE);
        tvComplete.setVisibility(GONE);
        tvError.setVisibility(GONE);
    }

    // 显示加载发生错误
    public void showError(String error) {
        state = STATE_ERROR;
        progressBar.setVisibility(GONE);
        tvComplete.setVisibility(GONE);
        tvError.setVisibility(VISIBLE);
    }

    // 显示所有数据加载完毕
    public void showComplete() {
        state = STATE_COMPLETE;
        progressBar.setVisibility(GONE);
        tvComplete.setVisibility(VISIBLE);
        tvError.setVisibility(GONE);
    }

    /**
     * @return true if current state is loading, false otherwise.
     */
    public boolean isLoading(){
        return state == STATE_LOADING;
    }

    /**
     * @return true if current state is complete, false otherwise.
     */
    public boolean isComplete(){
        return state == STATE_COMPLETE;
    }

    public void setErrorClickListener(OnClickListener onClickListener){
        tvError.setOnClickListener(onClickListener);
    }
}
