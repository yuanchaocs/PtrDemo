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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class RepoListFragment extends Fragment implements PtrView<List<String>> {

    @Bind(R.id.ptrClassicFrameLayout) PtrClassicFrameLayout ptrFrameLayout;
    @Bind(R.id.lvRepos) ListView listView;
    @Bind(R.id.emptyView) TextView emptyView;
    @Bind(R.id.errorView) TextView errorView;

    private ArrayAdapter<String> adapter;
    private ArrayList<String> datas = new ArrayList<String>();

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
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    // 这是视图层的业务逻辑-----------------------------------------------------------
    @OnClick({R.id.emptyView, R.id.errorView})
    public void loadData() {
        final int size = new Random().nextInt(5);
        new Thread(new Runnable() {
            @Override public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    // showMessage(e.getMessage());
                    return;
                }
                datas.clear();
                for (int i = 0; i < size; i++) {
                    datas.add("我是第" + (++count) + "条数据");
                }
                asyncLoadData(size);
            }
        }).start();
    }

    private void asyncLoadData(final int size) {
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

    // 这是视图的实现----------------------------------------------------------------
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
        adapter.notifyDataSetChanged();
    }

    @Override public void stopRefresh() {
        ptrFrameLayout.refreshComplete(); // 下拉刷新完成
    }
}