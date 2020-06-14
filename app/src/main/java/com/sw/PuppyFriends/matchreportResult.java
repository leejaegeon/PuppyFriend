package com.sw.PuppyFriends;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class matchreportResult extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mReference = firebaseDatabase.getReference();



    TextView petsittername1, match_month1, match_day1, matchtime1, eat1, review1, bowel1, walk1;
    String matching_id, usertype, owner_id, sitter_id;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchreportresult);

        Intent intent = getIntent();
        matching_id = intent.getExtras().getString("matching_id");
        usertype = intent.getExtras().getString("usertype");
        owner_id = intent.getExtras().getString("owner_id");
        sitter_id = intent.getExtras().getString("sitter_id");



        petsittername1 = (TextView)findViewById(R.id.petsittername1);
        matchtime1 = (TextView) findViewById(R.id.matchtime1);
        match_month1 = (TextView)findViewById(R.id.date_month);
        match_day1 = (TextView)findViewById(R.id.date_day);
        eat1 = (TextView)findViewById(R.id.eat1);
        review1 = (TextView)findViewById(R.id.review1);
        bowel1 = (TextView)findViewById(R.id.bowel1);
        walk1 = (TextView)findViewById(R.id.walk1);

        String root1 = "matching";

        String root2 = matching_id;

        String meeting1 = "펫시터이름";
        String meeting2_1="월";
        String meeting2_2="일";
        String meeting3 = "돌봄시간";
        String meeting4 = "밥먹은시간";
        String meeting5 = "배변횟수";
        String meeting6 = "산책한시간";
        String meeting7 = "특이사항";

        //펫시터이름
        mReference.child(root1).child(root2).child("matchreport").child(meeting1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                petsittername1.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //방문날짜 월
        mReference.child(root1).child(root2).child("matchreport").child("방문날짜").child(meeting2_1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                match_month1.setText(dataSnapshot.getValue().toString()+"월");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //방문날짜 일
        mReference.child(root1).child(root2).child("matchreport").child("방문날짜").child(meeting2_2).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                match_day1.setText(dataSnapshot.getValue().toString()+"일");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //돌봄한 시간
        mReference.child(root1).child(root2).child("matchreport").child(meeting3).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                matchtime1.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //밥먹은 시간
        mReference.child(root1).child(root2).child("matchreport").child(meeting4).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eat1.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //배변횟수
        mReference.child(root1).child(root2).child("matchreport").child(meeting5).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bowel1.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //산책한 시간
        mReference.child(root1).child(root2).child("matchreport").child(meeting6).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                walk1.setText(dataSnapshot.getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //특이사항
        mReference.child(root1).child(root2).child("matchreport").child(meeting7).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                review1.setText(dataSnapshot.getValue().toString());
                if(review1.getText().toString().isEmpty()) review1.setText("특이사항 없음.");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}