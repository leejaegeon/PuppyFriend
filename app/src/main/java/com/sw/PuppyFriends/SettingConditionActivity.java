package com.sw.PuppyFriends;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;

public class SettingConditionActivity extends AppCompatActivity {

    Button nextBtn;
    String id;

    EditText price1;
    EditText price2;

    Button dateBtn1;
    Button dateBtn2;
    Button dateBtn3;

    RadioGroup locRadioGroup;
    String location;

    int dateSelection = -1;

    String isServ1Checked = "f";
    String isServ2Checked = "f";
    String isServ3Checked = "f";
    String isServ4Checked = "f";

    CheckBox checkBox1;
    CheckBox checkBox2;
    CheckBox checkBox3;
    CheckBox checkBox4;

    @SuppressLint("ResourceType")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // application_status 공유해서 사용함
        setContentView(R.layout.condition_setting);

        nextBtn = findViewById(R.id.next_btn);
        price1 = findViewById(R.id.target_low_price);
        price2 = findViewById(R.id.target_high_price);

        dateBtn1 = findViewById(R.id.day5_select_btn);
        dateBtn2 = findViewById(R.id.day10_select_btn);
        dateBtn3 = findViewById(R.id.extra_day_select_btn);

//        checkBox1 = findViewById(R.id.service_check1);
//        checkBox2 = findViewById(R.id.service_check2);
//        checkBox3 = findViewById(R.id.service_check3);
//        checkBox4 = findViewById(R.id.service_check4);

        locRadioGroup = findViewById(R.id.loc_radio_btn_group);

        locRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.loc1_btn:
                        location = "대덕구";
                        break;
                    case R.id.loc2_btn:
                        location = "유성구";
                        break;
                    case R.id.loc3_btn:
                        location = "동구";
                        break;
                    case R.id.loc4_btn:
                        location = "서구";
                        break;
                    case R.id.loc5_btn:
                        location = "중구";
                        break;
                    case R.id.loc6_btn:
                        location = "상관없음";
                        break;
                }
            }
        });

        Intent intent = getIntent();

        id = intent.getExtras().getString("id");
        Log.d("id", id);

        dateBtn1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                dateSelection = 1;
                dateBtn1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_24px, 0, 0, 0);
                dateBtn2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                dateBtn3.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
        });

        dateBtn2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                dateSelection = 2;
                dateBtn2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_24px, 0, 0, 0);
                dateBtn1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                dateBtn3.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
        });

        dateBtn3.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                dateSelection = 100;
                dateBtn3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_24px, 0, 0, 0);
                dateBtn1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                dateBtn2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dateSelection != -1){
//                    getCheckBoxStatus();
                    Intent intent = new Intent(SettingConditionActivity.this, SitterApplicationActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("price1", price1.getText().toString());
                    intent.putExtra("price2", price2.getText().toString());
                    intent.putExtra("date", dateSelection);
                    intent.putExtra("location", location);
                    intent.putExtra("service", isServ1Checked + isServ2Checked + isServ3Checked + isServ4Checked);
                    startActivity(intent);
                }
            }
        });
    }

    private void getCheckBoxStatus(){
        if (checkBox1.isChecked()) {
            isServ1Checked = "t";
        } else {
            isServ1Checked = "f";
        }

        if (checkBox2.isChecked()) {
            isServ2Checked = "t";
        } else {
            isServ2Checked = "f";
        }

        if (checkBox3.isChecked()) {
            isServ3Checked = "t";
        } else {
            isServ3Checked = "f";
        }

        if (checkBox4.isChecked()) {
            isServ4Checked = "t";
        } else {
            isServ4Checked = "f";
        }

        Log.d("check box :" , isServ1Checked + isServ2Checked + isServ3Checked + isServ4Checked);
    }

}
