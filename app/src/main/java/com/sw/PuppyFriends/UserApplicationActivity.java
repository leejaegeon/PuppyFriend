package com.sw.PuppyFriends;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

// 견주가 영어로 뭔지 잘 모르겠어서 일단은 owner이라고 함
public class UserApplicationActivity extends AppCompatActivity {

    private DatabaseReference mPostReference;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference conditionRef = mRootRef.child("sitting_application_info");

    LinearLayout linearLayout;
    Button myInfoBtn;
    TextView title;

    String id;
    int size = -1;
    static int cnt = 0;

    String matching_id = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // application_status 공유해서 사용함
        setContentView(R.layout.application_status);

        linearLayout = findViewById(R.id.user_info_layout);

        title = findViewById(R.id.applistatustitletext);

        // 전 화면에서 id값 가져옴
        Intent intent = getIntent();

        id = intent.getExtras().getString("id");
        Log.d("id", id);

        // 견주는 들어가자마자 자기 정보가 DB에 올라감
        // 나중에는 화면을 하나 추가해서 기본 정보 작성하고 submit 버튼 누르면 정보 전송되도록 하면 될듯
        // false -> 견주 정보 올림, true -> 펫시터 정보 올림
//        postFirebaseDB(id, null,false);

        getFirebaseDB();
    }

    // sitting detail info db에서 견주들 목록 받아옴
    public void getMatchingId(final String mId){

        conditionRef.addValueEventListener(new ValueEventListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){

//                    SittingApplicationInfo get = postSnapshot.getValue(SittingApplicationInfo.class);
//
//                    if(get.id.equals(mId)){
//                        matching_id = get.matching_id;
//                        Log.d("matching_id", matching_id);
//                    }

                    String aid = postSnapshot.getKey();
                    if(aid.equals(mId)){ // sitting_application_info에서 mId와 같은 id를 찾아서 매칭아이디 가져옴
                        /////////////////////
                        if(postSnapshot.child("matching_id").exists()){
                        //////////////////////
                        matching_id = postSnapshot.child("matching_id").getValue().toString();
                        //////////////////
                        }
                        //////////////////
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("getFirebaseDatabase","loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    public void updateFirebaseDB(String mId){
        DatabaseReference hopperRef = conditionRef.child(mId);
        Map<String, Object> hopperUpdates = new HashMap<>();
        hopperUpdates.put("is_connected", "t");

        hopperRef.updateChildren(hopperUpdates);
    }


    // sitting_application_info에 있는 펫시터 중에서 자신을 선택한 펫시터만 골라서 보여주는 함수
    public void getFirebaseDB(){

        conditionRef.addValueEventListener(new ValueEventListener() {
            String result;

            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                linearLayout.removeAllViews();



                Log.d("getFirebaseDB","key : " + dataSnapshot.getChildrenCount());

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                    String key = postSnapshot.getKey();
                    SittingApplicationInfo get = postSnapshot.getValue(SittingApplicationInfo.class);

//                    result = "mail : " + get.id + ".com";
                    result = get.id;

                    Log.d("getFirebaseDatabase", "key: " + key);
                    Log.d("getFirebaseDatabase", "info: " + result);


                    ////////////////////////////
                    if(postSnapshot.child("application_id").exists()) {
                        ///////////////////////////


                    // 자신을 선택한 펫시터가 있으면
                    if(get.application_id.equals(id)){
                        // 목록 보여줌
                        addTextViewLayout(result);
                        addButtonLayout1(result, id);
                        addButtonLayout();
                    }

                        ///////////////////////
                    }
                    ///////////////////////

                }
                if(linearLayout.getChildCount()==0){
                    //자신을 선택한 펫시터가 없으면
                    noApplication();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("getFirebaseDatabase","loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    private void noApplication(){ //자신을 선택한 펫시터가 없을 때
        title.setText("아직 지원한 펫시터가 없습니다");

        final Button btn = new Button(this);

        btn.setText("조건 변경");
        btn.setGravity(Gravity.CENTER);
        btn.setTextSize(20);
        btn.setBackgroundColor(getResources().getColor(R.color.purple1));

        btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingDetailInfoActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        linearLayout.addView(btn);
    }

    @SuppressLint("ResourceAsColor")
    private void addButtonLayout(){
        final Button btn = new Button(this);

        btn.setText("선택");
        btn.setGravity(Gravity.CENTER);
        btn.setTextSize(20);
        btn.setId(cnt++);
        btn.setBackgroundColor(getResources().getColor(R.color.purple1));

        @SuppressLint("ResourceType") TextView t = findViewById(btn.getId()-2);
        final String seletedId = t.getText().toString();
//        final String[] splitedId = seletedId.split(" |\\.");

//        Toast.makeText(UserApplicationActivity.this, splitedId[2], Toast.LENGTH_SHORT).show();
        Toast.makeText(UserApplicationActivity.this, seletedId, Toast.LENGTH_SHORT).show();


        btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
//                TextView t = findViewById(btn.getId()-1);
//                String seletedId = t.getText().toString();
//                String[] splitedId = seletedId.split(" |\\.");
//
//                Toast.makeText(UserApplicationActivity.this, splitedId[2], Toast.LENGTH_SHORT).show();

//                getMatchingId(splitedId[2]);
//                updateFirebaseDB(splitedId[2]);
                getMatchingId(seletedId);
                updateFirebaseDB(seletedId);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//////////////////////////////////////////// 사전만남 입력하면 튕기는거 해결
                        mRootRef.child("matching").child(matching_id).child("owner_id").setValue("");
                        mRootRef.child("matching").child(matching_id).child("sitter_id").setValue("");
                        mRootRef.child("matching").child(matching_id).child("status").child("owner").setValue("");
                        mRootRef.child("matching").child(matching_id).child("status").child("sitter").setValue("");
////////////////////////////////////////////
                        Intent intent = new Intent(UserApplicationActivity.this, Meetingbefore_agree.class);
                        intent.putExtra("owner_id", id);
                        intent.putExtra("sitter_id", seletedId);
                        intent.putExtra("matching_id", matching_id);
                        startActivity(intent);
                        finish();
                    }
                }, 500);
            }
        });

        linearLayout.addView(btn);
    }

    private void addTextViewLayout(String str){
        TextView textView = new TextView(this);

        textView.setText(str);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setId(cnt++);
        linearLayout.addView(textView);
    }


    @SuppressLint("ResourceAsColor")
    private void addButtonLayout1(String st, final String my_id){
        final Button btn = new Button(this);

        btn.setText("프로필 확인");
        btn.setGravity(Gravity.CENTER);
        btn.setTextSize(20);
        btn.setId(cnt++);
        btn.setBackgroundColor(getResources().getColor(R.color.purple1));

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        param.bottomMargin = 30;
        btn.setLayoutParams(param);

        @SuppressLint("ResourceType") TextView t = findViewById(btn.getId()-1);
        final String seletedId = st;


        final String idid[] = seletedId.split("@");

        btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(getApplicationContext(), CheckProfileActivity.class);
                        intent.putExtra("id", idid[0]);
                        intent.putExtra("my_id", my_id);
                        startActivity(intent);

                    }
                }, 500);
            }
        });

        linearLayout.addView(btn);
    }


}
