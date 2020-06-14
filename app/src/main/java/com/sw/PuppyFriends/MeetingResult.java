package com.sw.PuppyFriends;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MeetingResult extends AppCompatActivity {

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mReference =  mDatabase.getReference();

    private TextView meetingresultownerid, meetingresultsitterid, meetingresult1, meetingresult2, meetingresult3, meetingresult4, meetingresult5, meetingresult6, meetingresult7, meetingresult8, meetingresult9, meetingresult10;

    private String matching_id;
    private String owner_id;
    private String sitter_id;

    private Button agree;

    private String dtdtdt;

    private Boolean isSitter = false;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meeting_result);

        Intent intent = getIntent();
        matching_id = intent.getExtras().getString("matching_id");
        owner_id = intent.getExtras().getString("owner_id");
        sitter_id = intent.getExtras().getString("sitter_id");

        isSitter = intent.getExtras().getBoolean("isSitter");

        meetingresultownerid = (TextView)findViewById(R.id.meetingresultownerid);
        meetingresultsitterid = (TextView)findViewById(R.id.meetingresultsitterid);
        meetingresultownerid.setText("견주 : "+owner_id);
        meetingresultsitterid.setText("펫시터 : "+sitter_id);

        meetingresult1 = (TextView)findViewById(R.id.meetingresult1);
        meetingresult2 = (TextView)findViewById(R.id.meetingresult2);
        meetingresult3 = (TextView)findViewById(R.id.meetingresult3);
        meetingresult4 = (TextView)findViewById(R.id.meetingresult4);
        meetingresult5 = (TextView)findViewById(R.id.meetingresult5);
        meetingresult6 = (TextView)findViewById(R.id.meetingresult6);
        meetingresult7 = (TextView)findViewById(R.id.meetingresult7);
        meetingresult8 = (TextView)findViewById(R.id.meetingresult8);
        meetingresult9 = (TextView)findViewById(R.id.meetingresult9);
        meetingresult10 = (TextView)findViewById(R.id.meetingresult10);



        agree = (Button)findViewById(R.id.result_agree_btn);
        agree.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                if(!isSitter){
                    //견주라면 펫시터 동의 기다리기
                    mReference.child("matching").child(matching_id).child("사전만남").child("owner_agree").setValue("yes");
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.putExtra("matching_id", matching_id);
                    intent.putExtra("id", owner_id);
                    intent.putExtra("sitter_id", sitter_id);
                    intent.putExtra("usertype", "owner");
//                    intent.putExtra("돌봄시간", dtdtdt);
                    startActivity(intent);
                    finish();
                }
                else if(isSitter){
                    //펫시터라면 돌봄 진행중 화면으로
                    mReference.child("matching").child(matching_id).child("사전만남").child("sitter_agree").setValue("yes");
                    Intent intent = new Intent(getApplicationContext(), SittingOngoing.class);
                    intent.putExtra("matching_id", matching_id);
                    intent.putExtra("usertype", "sitter");
                    intent.putExtra("owner_id", owner_id);
                    intent.putExtra("sitter_id", sitter_id);
//                    intent.putExtra("돌봄시간", dtdtdt);
                    startActivity(intent);
                    finish();
                }

            }
        });


        //데이터베이스 구조 "matching"/matching_id/"사전만남"
        //데이터베이스에 맞도록 수정
        String root1 = "matching";
        //인텐트로 받아온 매칭id
        String root2 = matching_id;

        String meeting1 = "날짜";
        String meeting2 = "장소";
        String meeting3 = "밥주는시간";
        String meeting4 = "사료위치";
        String meeting5 = "산책시간";
        String meeting6 = "산책상세";
        String meeting7 = "특이사항";
        String meeting8 = "etc";
        String meeting9 = "가격";
        String meeting10 = "돌봄시간";

        //날짜
        mReference.child(root1).child(root2).child("사전만남").child(meeting1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                meetingresult1.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //장소
        mReference.child(root1).child(root2).child("사전만남").child(meeting2).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                meetingresult2.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //밥주는시간
        mReference.child(root1).child(root2).child("사전만남").child(meeting3).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                meetingresult3.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //사료위치
        mReference.child(root1).child(root2).child("사전만남").child(meeting4).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                meetingresult4.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //산책시간
        mReference.child(root1).child(root2).child("사전만남").child(meeting5).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                meetingresult5.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //산책상세
        mReference.child(root1).child(root2).child("사전만남").child(meeting6).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                meetingresult6.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //특이사항
        mReference.child(root1).child(root2).child("사전만남").child(meeting7).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                meetingresult7.setText(dataSnapshot.getValue().toString());
                if(meetingresult7.getText().toString().isEmpty()) meetingresult7.setText("(공백)");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //기타
        mReference.child(root1).child(root2).child("사전만남").child(meeting8).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                meetingresult8.setText(dataSnapshot.getValue().toString());
                if(meetingresult8.getText().toString().isEmpty()) meetingresult8.setText("(공백)");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //가격
        mReference.child(root1).child(root2).child("사전만남").child(meeting9).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                meetingresult9.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mReference.child(root1).child(root2).child("사전만남").child(meeting10).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dtdtdt = dataSnapshot.getValue().toString();
                meetingresult10.setText(dtdtdt);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}