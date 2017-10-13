package com.example.weiting.taipei_project;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private fragment mFragment01;
    private FragmentManager mFragmentMgr;
    //private Fragment  mFragment;//记录当前处于哪个fragment
    //private Fragment1 fragment1;
    //private Fragment2 fragment2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_blank);
        mFragmentMgr = getFragmentManager();

        mFragment01 = new fragment();
        mFragmentMgr.beginTransaction()
                .replace(R.id.home, mFragment01, "TAG-mFragment01")
                .commit();
        /*if (mFragment01.isAdded()) {
            mFragmentMgr.beginTransaction().show(mFragment01).hide(mFragment01).commit();
        } else {
            mFragmentMgr.beginTransaction().add(R.id.home, mFragment01)
                    .show(mFragment01).commit();
        }*/
        //mFragment = fragment1;
    }
}
