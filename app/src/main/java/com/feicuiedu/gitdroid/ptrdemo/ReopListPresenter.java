package com.feicuiedu.gitdroid.ptrdemo;

import android.os.AsyncTask;

import com.feicuiedu.gitdroid.ptrdemo.view.PtrPageView;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/7/1 0001.
 *
 * MVP
 *  View
 *  Presenter
 * <p/>
 * Fragment在做视图工作 (实现了PtrPageView视图接口, 也就是下拉和上拉功能视图接口)
 * <p/>
 * Presenter里做了具体要做的业务(下拉刷新获取数据, 上拉加载更多数据), 以及视图的触发
 *
 *
 * 直接使用MVP库 Mosby库
 * 让你的应用简单清晰的使用MVP架构方式来进行构建开发
 *
 * 依赖  mosby
 *
 */
public class ReopListPresenter extends MvpNullObjectBasePresenter<PtrPageView>{

    // 这是下拉刷新视图层的业务逻辑-----------------------------------------------------------
    public void loadData() {
        new LoadDataTask().execute();
    }

    // 这是上拉加载更多视图层的业务逻辑------------------------------------------------
    public void loadMore() {
        new LoadMoreTask().execute();
    }

    private static int count = 0;

    private final class LoadDataTask extends AsyncTask<Void, Void, List<String>> {

        @Override protected List<String> doInBackground(Void... params) {
            // 模拟网络连接
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            final int size = new Random().nextInt(40);
            final ArrayList<String> loadDatas = new ArrayList<String>();
            for (int i = 0; i < size; i++) {
                loadDatas.add("我是第" + (++count) + "条数据");
            }
            return loadDatas;
        }

        @Override protected void onPostExecute(List<String> datas) {
            super.onPostExecute(datas);
            int size = datas.size();
            // 模似空数据时的(视图)情况
            if (size == 0) {
                getView().showEmptyView(); // listview不可见了,空的textview可见了
            }
            // 模似错误数据时的(视图)情况
            else if (size == 1) {
                getView().showErroView("unkown erro"); // listview不可见了,空的textview不可见了,错误的textview可见了
            }
            // 模似正常获取到了数据的(视图)情况
            else {
                getView().showContentView(); // 显示内容视图(让listview能显示)
                // 视图进行数据刷新
                getView().refreshData(datas);
            }
            // 停至结束这次下拉刷新
            getView().stopRefresh();
        }
    }

    private final class LoadMoreTask extends AsyncTask<Void, Void, List<String>> {

        @Override protected void onPreExecute() {
            super.onPreExecute();
            // 显示加载中...
            getView().showLoadMoreLoading();
        }

        @Override protected List<String> doInBackground(Void... params) {
            // 模拟加载更多时,网络连接
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final ArrayList<String> loadDatas = new ArrayList<String>();
            for (int i = 0; i < 10; i++) {
                loadDatas.add("我是loadMore的第" + i + "条数据");
            }
            return loadDatas;
        }

        @Override protected void onPostExecute(List<String> datas) {
            super.onPostExecute(datas);
            // 将加载到的数据添加到视图上
            getView().addMoreData(datas);
            // 隐藏加载中....
            getView().hideLoadMore();
        }
    }
}
