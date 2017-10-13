package com.example.weiting.bluetooth;

import android.app.Activity;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Main2Activity extends Activity {
    private BlankFragment mFragment01;
    private FragmentManager mFragmentMgr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_blank);
       mFragmentMgr = getFragmentManager();

        mFragmentMgr.beginTransaction()
                .replace(R.id.fragment_container, mFragment01, "TAG-mFragment01")
                .commit();
        if(findViewById(R.id.fragment_container) != null){
            if(savedInstanceState != null){
                return;
            }

        }
    }
}
