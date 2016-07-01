package com.feicuiedu.gitdroid.ptrdemo;

import com.feicuiedu.gitdroid.ptrdemo.view.LoadMoreView;
import com.feicuiedu.gitdroid.ptrdemo.view.PtrPageView;
import com.feicuiedu.gitdroid.ptrdemo.view.PtrView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/7/1 0001.
 * <p/>
 * RepoListFragment身上的，视图业务逻辑，单独写出来
 * <p/>
 * ReopListPresenter真正想做的事情：
 * 执行业务逻辑 ------
 * 调用相应视图
 * <p/>
 * 使得RepoListFragment只有视图及控制
 * RepoListFragment只实现视图, 不调用视图
 * <p/>
 * 你要做业务逻辑
 */
public class ReopListPresenter {
    private PtrPageView pageView;

    public ReopListPresenter(PtrPageView pageView) {
        this.pageView = pageView;
    }

    // 这是下拉刷新视图层的业务逻辑-----------------------------------------------------------
    public void loadData() {
        final int size = new Random().nextInt(40);
        new Thread(new Runnable() {
            @Override public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
                final ArrayList<String> loadDatas = new ArrayList<String>();
                for (int i = 0; i < size; i++) {
                    loadDatas.add("我是第" + (++count) + "条数据");
                }
                asyncLoadData(size, loadDatas);
            }
        }).start();
    }

    private void asyncLoadData(final int size, final ArrayList<String> datas) {
        ptrFrameLayout.post(new Runnable() {
            @Override public void run() {
                // 模似空数据时的(视图)情况
                if (size == 0) {
                    pageView.showEmptyView(); // listview不可见了,空的textview可见了
                }
                // 模似错误数据时的(视图)情况
                else if (size == 1) {
                    pageView.showErroView("unkown erro"); // listview不可见了,空的textview不可见了,错误的textview可见了
                }
                // 模似正常获取到了数据的(视图)情况
                else {
                    pageView.showContentView(); // 显示内容视图(让listview能显示)
                    // 视图进行数据刷新
                    pageView.refreshData(datas);
                }
                // 停至结束这次下拉刷新
                pageView.stopRefresh();
            }
        });
    }

    private static int count = 0;

    // 这是上拉加载更多视图层的业务逻辑------------------------------------------------
    public void loadMore() {
        // 显示加载中...
        pageView.showLoadMoreLoading();
        new Thread(new Runnable() {
            @Override public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final ArrayList<String> loadDatas = new ArrayList<String>();
                for (int i = 0; i < 10; i++) {
                    loadDatas.add("我是loadMore的第" + i + "条数据");
                }
                ptrFrameLayout.post(new Runnable() {
                    @Override public void run() {
                        // 将加载到的数据添加到视图上
                        pageView.addMoreData(loadDatas);
                        // 隐藏加载中....
                        pageView.hideLoadMore();
                    }
                });
            }
        }).start();
    }
}
