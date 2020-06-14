package com.sw.PuppyFriends;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SitterApplicationActivity extends Activity {

    private DatabaseReference mPostReference;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference conditionRef = mRootRef.child("sitting_detail_info");
    DatabaseReference sitterRef = mRootRef.child("sitting_application_info");

    LinearLayout linearLayout;

    static int cnt = 0;
    String id;
    String price1;
    String price2;
    String location;

    int dateSelection;
    int size = getSize();

    String[] splitedDate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // application_status 공유해서 사용함
        setContentView(R.layout.application_status);

        // 이 레이아웃에 버튼이랑 textview 추가할 것임
        linearLayout = findViewById(R.id.user_info_layout);

        // 전 화면에서 id값 가져옴
        // login activity에 mail을 split해서 다른 액티비티로 넘겨주는 코드 추가함
        Intent intent = getIntent();

        id = intent.getExtras().getString("id");
        price1 = intent.getExtras().getString("price1");
        price2 = intent.getExtras().getString("price2");
        dateSelection = intent.getExtras().getInt("date");
        location = intent.getExtras().getString("location");

        Log.d("info : ", id + " , " + price1 + "~" + price2 + " , " +  Integer.toString(dateSelection*10) + location);

        SimpleDateFormat format = new SimpleDateFormat("MM/dd");
        Date time = new Date();

        String date = format.format(time);
        splitedDate = date.split("/");

        // 견주 목록 출력
        getFirebaseDB();
    }

    // 펫시터 정보를 db에 올림
    public void postFirebaseDB(String str){

        mPostReference = FirebaseDatabase.getInstance().getReference();

        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValue = null;

        // 처음에는 is_connected 부분을 f로 지정함
        // 만약 견주가 펫시터를 선택하면 그 때 t로 바뀌게 됌
        SittingApplicationInfo post = new SittingApplicationInfo(id,"sitter", str, false, Integer.toString(size++));

        postValue = post.toMap();

        childUpdates.put("/sitting_application_info/" + id , postValue);
        mPostReference.updateChildren(childUpdates);

    }

    public int getSize(){
        sitterRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                size = (int) dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return size;
    }

    // sitting detail info db에서 견주들 목록 받아옴
    public void getFirebaseDB(){

        conditionRef.addValueEventListener(new ValueEventListener() {
            String result;

            TextView title = (TextView)findViewById(R.id.applistatustitletext);

            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                linearLayout.removeAllViews();

                Log.d("getFirebaseDB","key : " + dataSnapshot.getChildrenCount());

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                    String key = postSnapshot.getKey();
                    SittingDetailInfo get;

                    get = postSnapshot.getValue(SittingDetailInfo.class);
                    result = "id : " + get.id;

                    String d = get.date;
                    String[] splited = d.split("/");

                    int differ = (Integer.parseInt(splited[0]) - Integer.parseInt(splitedDate[0])) * 30 +
                            (Integer.parseInt(splited[1]) - Integer.parseInt(splitedDate[1]));

                    Log.d("firebase key", key);

                    if(get.type.equals("not-sitter") && (Integer.parseInt(get.desired_price) >= Integer.parseInt(price1))
                            && (Integer.parseInt(get.desired_price) <= Integer.parseInt(price2)) && (Math.abs(differ) <= dateSelection*5) &&
                            location.equals("상관없음") && (!key.equals(id))) {

                        addTextViewLayout(result);
                        addButtonLayout();
                        title.setText("신청 정보");

                    } else if(get.type.equals("not-sitter") && (Integer.parseInt(get.desired_price) >= Integer.parseInt(price1))
                            && (Integer.parseInt(get.desired_price) <= Integer.parseInt(price2)) && (Math.abs(differ) <= dateSelection*5) &&
                            get.location.equals(location) && (!key.equals(id))) {

                        addTextViewLayout(result);
                        addButtonLayout();
                        title.setText("신청 정보");

                    } else {
                        title.setText("조건에 맞는 견주가 없습니다");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("getFirebaseDatabase","loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    // 동적으로 버튼 추가하는 함수
    private void addButtonLayout(){
        final Button btn1 = new Button(this);
        final Button btn2 = new Button(this);

        // 버튼 속성
        btn1.setText("선택");
        btn1.setGravity(Gravity.CENTER);
        btn1.setTextSize(15);

        btn2.setText("프로필 확인하기");
        btn2.setGravity(Gravity.CENTER);
        btn2.setTextSize(15);

        // 동적으로 아이디 설정, 숫자임
        // text view를 먼저 넣고 버튼을 넣어서 text view 아이디가 0이면 버튼 아이디는 1임
        btn1.setId(cnt++);
        btn1.setBackgroundColor(getResources().getColor(R.color.purple1));
        btn2.setId(cnt++);
        btn2.setBackgroundColor(getResources().getColor(R.color.blue3));

        btn1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                // 바로 위의     mail 정보를 가지고 있는 text view 가져옴
                TextView t = findViewById(btn1.getId()-1);
                String seletedId = t.getText().toString();
                String[] splitedId = seletedId.split(" |\\.");
                Toast.makeText(SitterApplicationActivity.this, "clicked : " + splitedId[2], Toast.LENGTH_SHORT).show();

                // db에 넣어줄 것임
                postFirebaseDB(splitedId[2]);
                finish();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("ResourceType") TextView t = findViewById(btn1.getId()-1);
                String seletedId = t.getText().toString();
                String[] splitedId = seletedId.split(" |\\.");
                Intent intent = new Intent(SitterApplicationActivity.this, CheckProfileActivity.class);
                intent.putExtra("id",splitedId[2]);
//                Toast.makeText(SitterApplicationActivity.this, "clicked : " + splitedId[2], Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        linearLayout.addView(btn1);
        linearLayout.addView(btn2);
    }

    // 동적으로 text view 추가하는 함수
    private void addTextViewLayout(String str){
        TextView textView = new TextView(this);

        // text view 속성
        textView.setText(str);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setId(cnt++);
        linearLayout.addView(textView);
    }

}
