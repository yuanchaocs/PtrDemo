package com.feicuiedu.gitdroid.ptrdemo.view;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

/**
 * Created by Administrator on 2016/7/1 0001.
 */
public interface PtrPageView extends MvpView,PtrView<List<String>>, LoadMoreView<List<String>>{
}
