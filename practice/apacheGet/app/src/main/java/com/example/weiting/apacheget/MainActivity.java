package com.example.weiting.apacheget;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Build;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import org.json.JSONArray;
import org.json.JSONObject;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
public class MainActivity extends AppCompatActivity {

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = (TextView)findViewById(R.id.textView);
        findViews();
        setListeners();


        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build());


        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());

    }

    private TextView view;
    private Button button_get_record;
    private Button button_put_record;

    private void findViews() {
        button_get_record = (Button)findViewById(R.id.get_record);
    }

    private void setListeners() {
        button_get_record.setOnClickListener(getDBRecord);

    }

    private Button.OnClickListener getDBRecord = new Button.OnClickListener() {
        public void onClick(View v) {
            // TODO Auto-generated method stub
            TableLayout user_list = (TableLayout)findViewById(R.id.user_list);
            user_list.setStretchAllColumns(true);
            TableLayout.LayoutParams row_layout = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            TableRow.LayoutParams view_layout = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            try {
                String result = connectDB1.getData();  //呼叫 DBConnector 查詢SQL
                view.setText(result);

                /*JSONArray jsonArray = new JSONArray(result);
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    TableRow tr = new TableRow(MainActivity.this);
                    tr.setLayoutParams(row_layout);
                    tr.setGravity(Gravity.CENTER_HORIZONTAL);

                    TextView user_acc = new TextView(MainActivity.this);
                    user_acc.setText(jsonData.getString("Value01"));
                    user_acc.setLayoutParams(view_layout);

                    TextView user_pwd = new TextView(MainActivity.this);
                    user_pwd.setText(jsonData.getString("Value02"));
                    user_pwd.setLayoutParams(view_layout);

                    tr.addView(user_acc);
                    tr.addView(user_pwd);
                    user_list.addView(tr);
                }*/
            } catch(Exception e) {
                Log.e("log_tag", e.toString());
            }
        }
    };

}
