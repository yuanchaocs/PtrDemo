package com.feicuiedu.gitdroid.ptrdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mugen.Mugen;
import com.mugen.MugenCallbacks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

//              https://github.com/yuanchaocs/PtrDemo.git

public class RepoListFragment extends Fragment implements PtrView<List<String>>, LoadMoreView<List<String>> {

    @Bind(R.id.ptrClassicFrameLayout) PtrClassicFrameLayout ptrFrameLayout;
    @Bind(R.id.lvRepos) ListView listView;
    @Bind(R.id.emptyView) TextView emptyView;
    @Bind(R.id.errorView) TextView errorView;

    private ArrayAdapter<String> adapter;

    private FooterView footerView; // 上拉加载更多的视图

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_repo_list, container, false);
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        //
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
        // 下拉刷新
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override public void onRefreshBegin(PtrFrameLayout frame) {
                loadData();
            }
        });

        footerView = new FooterView(getContext());
        // 上拉加载更多(listview滑动动最后的位置了，就可以loadmore)
        Mugen.with(listView, new MugenCallbacks() {
            @Override public void onLoadMore() {
                Toast.makeText(getContext(), "loadmore", Toast.LENGTH_SHORT).show();
                loadMore();
            }

            // 是否正在加载，此方法用来避免重复加载
            @Override public boolean isLoading() {
                return listView.getFooterViewsCount() > 0 && footerView.isLoading();
            }

            // 是否所有数据都已加载
            @Override public boolean hasLoadedAllItems() {
                return listView.getFooterViewsCount() > 0 && footerView.isComplete();
            }
        }).start();
    }

    @OnClick({R.id.emptyView, R.id.errorView})
    public void autoRefresh() {
        ptrFrameLayout.autoRefresh();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    // 这是下拉刷新视图层的业务逻辑-----------------------------------------------------------
    private void loadData() {
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
                    showEmptyView(); // listview不可见了,空的textview可见了
                }
                // 模似错误数据时的(视图)情况
                else if (size == 1) {
                    showErroView("unkown erro"); // listview不可见了,空的textview不可见了,错误的textview可见了
                }
                // 模似正常获取到了数据的(视图)情况
                else {
                    showContentView(); // 显示内容视图(让listview能显示)
                    // 视图进行数据刷新
                    refreshData(datas);
                }
                // 停至结束这次下拉刷新
                stopRefresh();
            }
        });
    }

    private static int count = 0;

    // 这是拉刷新视图的实现----------------------------------------------------------------
    @Override public void showContentView() {
        ptrFrameLayout.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
    }

    @Override public void showErroView(String msg) {
        ptrFrameLayout.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
    }

    @Override public void showEmptyView() {
        ptrFrameLayout.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
    }

    @Override public void showMessage(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override public void refreshData(List<String> datas) {
        adapter.clear();
        adapter.addAll(datas);
    }

    @Override public void stopRefresh() {
        ptrFrameLayout.refreshComplete(); // 下拉刷新完成
    }

    // 这是上拉加载更多视图层的业务逻辑------------------------------------------------
    private void loadMore() {
        // 显示加载中...
        showLoadMoreLoading();
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
                        addMoreData(loadDatas);
                        // 隐藏加载中....
                        hideLoadMore();
                    }
                });
            }
        }).start();
    }

    // 这是上拉加载更多视图层实现------------------------------------------------------
    @Override public void addMoreData(List<String> datas) {
        adapter.addAll(datas);
    }

    @Override public void hideLoadMore() {
        listView.removeFooterView(footerView);
    }

    @Override public void showLoadMoreLoading() {
        if (listView.getFooterViewsCount() == 0) {
            listView.addFooterView(footerView);
        }
        footerView.showLoading();
    }

    @Override public void showLoadMoreErro(String msg) {
        if (listView.getFooterViewsCount() == 0) {
            listView.addFooterView(footerView);
        }
        footerView.showError(msg);
    }

    @Override public void showLoadMoreEnd() {
        if (listView.getFooterViewsCount() == 0) {
            listView.addFooterView(footerView);
        }
        footerView.showComplete();
    }
}