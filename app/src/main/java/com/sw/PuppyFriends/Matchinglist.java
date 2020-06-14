package com.sw.PuppyFriends;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Matchinglist extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference pathReference = databaseReference.child("matching");
    String id;

    String owner_id, sitter_id, usertype, status;

    String your_id, tt, stst, my_id;

    static int cnt = 0;
    int size = getSize();

    LinearLayout linearLayout;

    Boolean reportExist;



    String a;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matchinglist);

        Intent intent = getIntent();
        id = intent.getExtras().getString("id");
        linearLayout = (LinearLayout)findViewById(R.id.matchinglistlayout);


        pathReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                linearLayout.removeAllViews();

                addTextViewLayout(id);

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                    String matchingidkey = postSnapshot.getKey();

//                    if(postSnapshot.child("owner_id").exists()){

                    /////////////////////////////////////
                    if(postSnapshot.child("owner_id").exists() && postSnapshot.child("sitter_id").exists() && postSnapshot.child("status").child("owner").exists() && postSnapshot.child("status").child("sitter").exists()){
                        /////////////////////////////////////
                        if(postSnapshot.child("owner_id").getValue().toString().equals(id) || postSnapshot.child("sitter_id").getValue().toString().equals(id)){

                            if(postSnapshot.child("sitter_id").exists() && postSnapshot.child("owner_id").getValue().toString().equals(id)) {
                                your_id = postSnapshot.child("sitter_id").getValue().toString();
                                tt = "펫시터";
                                my_id = postSnapshot.child("owner_id").getValue().toString();
                                stst = postSnapshot.child("status").child("owner").getValue().toString(); //돌봄 진행 상태 확인
                            }
                            else if(postSnapshot.child("sitter_id").exists() && postSnapshot.child("sitter_id").getValue().toString().equals(id)) {
                                your_id = postSnapshot.child("owner_id").getValue().toString();
                                my_id = postSnapshot.child("sitter_id").getValue().toString();
                                tt = "견주";
                                stst = postSnapshot.child("status").child("sitter").getValue().toString(); //돌봄 진행 상태 확인
                            }

                            reportExist = postSnapshot.child("matchreport").exists(); //펫시터가 돌봄일지 작성했는지 여부

                            addTextViewLayout(matchingidkey, stst, your_id);
                            addButtonLayout1(your_id, tt, my_id);
                            addButtonLayout2(matchingidkey, stst, reportExist);

                        }

                        ///////////////////////////
                    }
                    //////////////////////////////

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private int getSize(){
        pathReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                size =  (int)dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return size;
    }

    private void addTextViewLayout(String str){
        TextView textView = new TextView(this);

        // text view 속성
        textView.setText(str);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setId(cnt++);
        linearLayout.addView(textView);
    }
    private void addTextViewLayout(String str, String status, String yi){
        TextView textView = new TextView(this);

        // text view 속성
        textView.setText(yi+" ("+status+")");
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setId(cnt++);
        linearLayout.addView(textView);
    }

    @SuppressLint("ResourceAsColor")
    private void addButtonLayout1(String st, String tt, final String my_id){
        final Button btn = new Button(this);

        btn.setText(tt+" 프로필 확인");
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

    @SuppressLint("ResourceAsColor")
    private void addButtonLayout2(final String mk, String st, final Boolean mr){
        final Button btn = new Button(this);

        if(st.equals("진행중")) a = "선택";
        else if(st.equals("종료")) a = "돌봄일지 확인";

        btn.setText(a);
        btn.setGravity(Gravity.CENTER);
        btn.setTextSize(20);
        btn.setId(cnt++);
        btn.setBackgroundColor(getResources().getColor(R.color.purple1));


        btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {

                pathReference.child(mk).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        owner_id = dataSnapshot.child("owner_id").getValue().toString();
                        sitter_id = dataSnapshot.child("sitter_id").getValue().toString();
                        if(id.equals(owner_id)) usertype = "owner";
                        else if(id.equals(sitter_id)) usertype = "sitter";

                        //status 구분
                        if(dataSnapshot.child("status").exists() && dataSnapshot.child("status").child(usertype).getValue().toString().equals("진행중")) status = "진행중";
                        else if(dataSnapshot.child("status").exists() && dataSnapshot.child("status").child(usertype).getValue().toString().equals("종료")) status = "종료";

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if(status.equals("진행중")){
                            Intent intent = new Intent(getApplication(), Waiting.class);
                            intent.putExtra("owner_id", owner_id);
                            intent.putExtra("sitter_id", sitter_id);
                            intent.putExtra("matching_id", mk);
                            intent.putExtra("usertype", usertype);
                            startActivity(intent);
                        } else if(status.equals("종료")){
                            if(mr){ //돌봄일지 있으면
                                Intent intent = new Intent(getApplication(), matchreportResult.class);
                                intent.putExtra("owner_id", owner_id);
                                intent.putExtra("sitter_id", sitter_id);
                                intent.putExtra("matching_id", mk);
                                intent.putExtra("usertype", usertype);
                                startActivity(intent);
                            } else { //작성 없으면
                                Toast.makeText(getApplicationContext(), "펫시터가 돌봄일지를 작성하지 않았습니다", Toast.LENGTH_LONG).show();
                            }




//                            Toast.makeText(getApplicationContext(), mk+"는 종료되었습니다.", Toast.LENGTH_SHORT).show();
                        }



                    }
                }, 500);
            }
        });

        linearLayout.addView(btn);
    }

}