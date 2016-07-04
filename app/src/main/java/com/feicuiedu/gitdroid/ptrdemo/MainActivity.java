package com.feicuiedu.gitdroid.ptrdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


//             自己的Fragment  ---- extends MvpFragment<V是谁,P是谁>
// View        自己的View      ---- extends MvpView
// Presenter   自己的Presenter ---- extends MvpNullObjectBasePresenter<V是谁>

// Activity or Fragment extends MvpFragment<> implements 自己的View {}

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
