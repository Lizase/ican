package com.example.weiting.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView remind;
    RadioButton radioButton;
    RadioButton radioButton2;
    RadioButton radioButton3;
    RadioGroup radioGroup;
    Button play;
    TextView textView5;
    TextView textView6;
    TextView textView7;
    TextView textView8;
    int MoraGamer = -1;
    String[] MoraString = {"剪刀","石頭","布"};

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
        remind = (TextView) findViewById(R.id.remind);
        radioButton = (RadioButton) findViewById(R.id.radioButton);
        radioButton = (RadioButton) findViewById(R.id.radioButton2);
        radioButton = (RadioButton) findViewById(R.id.radioButton3);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        play = (Button) findViewById(R.id.play);
        textView5 = (TextView) findViewById(R.id.textView5);
        textView6 = (TextView) findViewById(R.id.textView6);
        textView7 = (TextView) findViewById(R.id.textView7);
        textView8 = (TextView) findViewById(R.id.textView8);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.radioButton:
                        MoraGamer = 2;
                        break;
                    case R.id.radioButton2:
                        MoraGamer = 1;
                        break;
                    case R.id.radioButton3:
                        MoraGamer = 0;
                        break;

                }
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().equals(""))
                    remind.setText("請選擇玩家名稱");
                else if (MoraGamer == -1)
                    remind.setText("請選擇出拳的種類");
                else {
                    textView5.setText(editText.getText());
                    textView7.setText(MoraString[MoraGamer]);
                    int MoraComputer = (int) (Math.random() * 3);
                    textView8.setText(MoraString[MoraComputer]);
                    if ((MoraGamer == 0 && MoraComputer == 1) || (MoraGamer == 1 && MoraComputer == 2) || (MoraGamer == 2 && MoraComputer == 0)) {
                        textView6.setText("電腦");
                        remind.setText("可惜，電腦獲勝");
                    } else if (MoraComputer == MoraGamer) {
                        textView6.setText("平局");
                        remind.setText("平局，再試一場看看");
                    } else {
                        textView6.setText(editText.getText());
                        remind.setText("恭喜你獲勝了");
                    }

                }
            }
        });


    }

}
