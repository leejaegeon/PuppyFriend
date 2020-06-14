package com.sw.PuppyFriends;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SettingDetailInfoActivity extends AppCompatActivity {

    private DatabaseReference mPostReference;
//    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
//    DatabaseReference conditionRef = mRootRef.child("sitting_application_info");

    Button nextBtn;
    EditText dogNameTxt;
    EditText dogBreedTxt;
    EditText dogAgeTxt;

    EditText userNameTxt;
    RadioGroup radioGroup;
    RadioGroup locRadioGroup;
    EditText userAgeTxt;

    EditText dateTxt1;
    EditText dateTxt2;
    EditText priceTxt;

    String id;
    String gender;
    String loc;

    TextView dateTxt;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // application_status 공유해서 사용함
        setContentView(R.layout.detail_condition_setting);

        Intent intent = getIntent();

        id = intent.getExtras().getString("id");
        Toast.makeText(SettingDetailInfoActivity.this, "ID : " + id, Toast.LENGTH_SHORT).show();

        nextBtn = findViewById(R.id.next_btn2);
        dogNameTxt = findViewById(R.id.dog_name_inp_txt);
        dogBreedTxt = findViewById(R.id.dog_breed_inp_txt);
        dogAgeTxt = findViewById(R.id.dog_age_inp_txt);

        userNameTxt = findViewById(R.id.user_name_inp_txt);
        userAgeTxt = findViewById(R.id.user_age_inp_txt);

        radioGroup = findViewById(R.id.radio_btn_group);
        locRadioGroup = findViewById(R.id.loc_radio_btn_group);

        dateTxt1 = findViewById(R.id.date_inp_txt1);
        dateTxt2 = findViewById(R.id.date_inp_txt2);
        priceTxt = findViewById(R.id.price_inp_txt);

        dateTxt = findViewById(R.id.contect_info_txt);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.male_btn:
                        gender = "male";
                        break;
                    case R.id.female_btn:
                        gender = "female";
                        break;
                }
            }
        });

        locRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.loc1_btn:
                        loc = "대덕구";
                        break;
                    case R.id.loc2_btn:
                        loc = "유성구";
                        break;
                    case R.id.loc3_btn:
                        loc = "동구";
                        break;
                    case R.id.loc4_btn:
                        loc = "서구";
                        break;
                    case R.id.loc5_btn:
                        loc = "중구";
                        break;
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((dogNameTxt.getText().toString().equals("")) || (dogBreedTxt.getText().toString().equals("")) || (dogAgeTxt.getText().toString().equals(""))
                        || (userNameTxt.getText().toString().equals("")) || (userAgeTxt.getText().toString().equals("")) || (loc == null)
                        || (dateTxt1.getText().toString().equals("")) || (dateTxt2.getText().toString().equals("")) || (priceTxt.getText().toString().equals(""))){
                    Toast.makeText(SettingDetailInfoActivity.this, "입력되지 않은 문항이 있습니다.", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        Integer.parseInt(priceTxt.getText().toString());

                        postFirebaseDB(id, dogNameTxt.getText().toString(), dogBreedTxt.getText().toString(), dogAgeTxt.getText().toString(),
                                userNameTxt.getText().toString(), gender, userAgeTxt.getText().toString(), loc,
                                dateTxt1.getText().toString()+"/"+dateTxt2.getText().toString(),priceTxt.getText().toString());

                        Intent intent = new Intent(SettingDetailInfoActivity.this, UserApplicationActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    } catch (NumberFormatException e) {
                        Toast.makeText(SettingDetailInfoActivity.this, "금액 항목에는 숫자만 입력 가능합니다.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }

    public void postFirebaseDB(String mId, String dogName, String dogBreed, String dogAge, String userName
            , String userGender, String userAge, String location, String date, String price){

        mPostReference = FirebaseDatabase.getInstance().getReference();

        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValue = null;

        SittingDetailInfo post;

        post = new SittingDetailInfo(mId, dogName, dogBreed, dogAge, userName, userAge, userGender, location, date, price);
        postValue = post.toMap();
        childUpdates.put("/sitting_detail_info/" + mId , postValue);
        mPostReference.updateChildren(childUpdates);
    }

}