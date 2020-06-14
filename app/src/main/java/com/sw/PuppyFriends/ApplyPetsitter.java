package com.sw.PuppyFriends;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ApplyPetsitter extends AppCompatActivity {


    Button button_register, button_cancel, button_applypetsitter;
    EditText et_name, et_gender, et_price, et_petsitterex, et_handlingex, et_car, et_smoking, et_petex;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_petsitter);

        button_register = (Button) findViewById(R.id.button_register);
        button_cancel = (Button) findViewById(R.id.button_cancel);
        button_applypetsitter = (Button) findViewById(R.id.button_applypetsitter);

        et_name = (EditText) findViewById(R.id.et_name);
        et_gender = (EditText) findViewById(R.id.et_gender);
        et_price = (EditText) findViewById(R.id.et_price);
        et_petsitterex = (EditText) findViewById(R.id.et_petsitterex);
        et_handlingex = (EditText) findViewById(R.id.et_handlingex);
        et_car = (EditText) findViewById(R.id.et_car);
        et_smoking = (EditText) findViewById(R.id.et_smoking);
        et_petex = (EditText) findViewById(R.id.et_petex);

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String e1 = et_name.getText().toString();
                String e2 = et_gender.getText().toString();
                String e3 = et_price.getText().toString();
                String e4 = et_petsitterex.getText().toString();
                String e5 = et_handlingex.getText().toString();
                String e6 = et_car.getText().toString();
                String e7 = et_smoking.getText().toString();
                String e8 = et_petex.getText().toString();

                if (e1.isEmpty())
                    Toast.makeText(getApplicationContext(), "이름을 입력해 주세요", Toast.LENGTH_LONG).show();
                else if (e2.isEmpty())
                    Toast.makeText(getApplicationContext(), "성별을 입력해 주세요", Toast.LENGTH_LONG).show();
                else if (e3.isEmpty())
                    Toast.makeText(getApplicationContext(), "받고 싶은 가격을 입력해 주세요", Toast.LENGTH_LONG).show();
                else if (e4.isEmpty())
                    Toast.makeText(getApplicationContext(), "팻시터 경험을 입력해 주세요", Toast.LENGTH_LONG).show();
                else if (e5.isEmpty())
                    Toast.makeText(getApplicationContext(), "대형견 핸들링 경험을 입력해 주세요", Toast.LENGTH_LONG).show();
                else if (e6.isEmpty())
                    Toast.makeText(getApplicationContext(), "자차 보유 여부를 입력해 주세요", Toast.LENGTH_LONG).show();
                else if (e7.isEmpty())
                    Toast.makeText(getApplicationContext(), "흡연 여부를 입력해 주세요", Toast.LENGTH_LONG).show();
                else if (e8.isEmpty())
                    Toast.makeText(getApplicationContext(), "반려동물 양육경험을 입력해 주세요", Toast.LENGTH_LONG).show();
                    //특이사항이랑 기타 이외에는 필수적으로 채워야 한다
                else {
                    databaseReference.child("apply_petsitter").child("이름").setValue(e1);
                    databaseReference.child("apply_petsitter").child("성별").setValue(e2);
                    databaseReference.child("apply_petsitter").child("희망가격").setValue(e3);
                    databaseReference.child("apply_petsitter").child("팻시터경험").setValue(e4);
                    databaseReference.child("apply_petsitter").child("대형견경험").setValue(e5);
                    databaseReference.child("apply_petsitter").child("자차보유여부").setValue(e6);
                    databaseReference.child("apply_petsitter").child("흡연여부").setValue(e7);
                    databaseReference.child("apply_petsitter").child("팻양육경험").setValue(e8);
                }
            }
        });
    }
}