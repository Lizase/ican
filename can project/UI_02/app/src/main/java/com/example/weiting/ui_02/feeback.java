package com.example.weiting.ui_02;

/**
 * Created by Weiting on 2017/8/1.
 */
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import java.util.ArrayList;


public class feeback extends Activity {
    public RadioGroup radioGroup;
    public RadioButton radioButton1;
    public RadioButton radioButton2;
    public RadioButton radioButton3;
    public EditText suggestion;
    public Button button;
    String result = "";
    String comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feeback);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        radioButton1 = (RadioButton)findViewById(R.id.radioButton2);
        radioButton2 = (RadioButton)findViewById(R.id.radioButton3);
        radioButton3 = (RadioButton)findViewById(R.id.radioButton4);
        button = (Button)findViewById(R.id.button);
        suggestion = (EditText)findViewById(R.id.editText);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.radioButton2:
                        break;
                    case R.id.radioButton3:
                        break;
                    case R.id.radioButton4:
                        break;
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("check1","button click");
                comment = suggestion.getText().toString();
                update();
            }
        });
    }
    public void update(){
        new AsyncTask<Integer,Void,String>(){
            @Override
            protected void onPreExecute(){
                result = "";
                super.onPreExecute();
            }
            @Override
            protected String doInBackground(Integer...parms){
                String url = "";
                ArrayList<NameValuePair> user = new ArrayList<NameValuePair>();
                user.add(new BasicNameValuePair("id","pppppp"));
                user.add(new BasicNameValuePair("comment",comment));
                url = "http://140.134.26.143/ican/ican/Areport_connect.php";
                //user.add(new BasicNameValuePair("comment",suggestion.getText().toString()));
                do{
                    result = canDBconnect.getData(user,url);
                }while(result == "");
                return result;
            }
            @Override
            protected void onProgressUpdate(Void...parms){

            }
            @Override
            protected void onPostExecute(String result){
                super.onPostExecute(result);
                Toast.makeText(feeback.this,result,Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

}
