package com.feicuiedu.gitdroid.ptrdemo.view;

/**
 * Created by Administrator on 2016/7/1 0001.
 */
public interface LoadMoreView<T> {

    // 添加更多数据
    void addMoreData(T datas);

    // 隐藏加载更多的视图
    void hideLoadMore();

    // 加载更多 -- 加载中
    void showLoadMoreLoading();

    // 加载更多 -- 加载发生错误
    void showLoadMoreErro(String msg);

    // 加载更多 -- 没有更多数据
    void showLoadMoreEnd();
}
