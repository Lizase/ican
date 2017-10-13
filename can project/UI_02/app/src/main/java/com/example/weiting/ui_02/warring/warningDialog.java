package com.example.weiting.ui_02.warring;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weiting.ui_02.R;

/**
 * Created by Weiting on 2017/7/31.
 */

public class warningDialog extends Activity {
    Button btn_confirm;
    Button btn_cancel;
    TextView text_content;
    TextView text_title;
    Button text_close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.warning);

        text_close = (Button)findViewById(R.id.text_close);
        text_title  = (TextView)findViewById(R.id.text_title);
        text_content = (TextView)findViewById(R.id.text_content);


        text_close.setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View v) {
                finish();
            }
        });
    }
}
