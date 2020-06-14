package com.sw.PuppyFriends;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Rating extends AppCompatActivity {

    String your_id, usertype, ratingtype, matching_id, my_id;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    TextView title;
    TextView detail;
    RadioGroup r;
    TextView point;
    EditText review;
    Button ratingbtn;

    Boolean terminateOK;

    int m;
    int p;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating);

        Intent intent = getIntent();
        your_id = intent.getExtras().getString("your_id");
        usertype = intent.getExtras().getString("usertype");
        my_id = intent.getExtras().getString("my_id");
        matching_id = intent.getExtras().getString("matching_id");


        /// 데이터베이스 테스트용 임시id
//        your_id = "y";
//        usertype = "owner";
//        matching_id = "mmmm";
        ///

        title = (TextView)findViewById(R.id.ratingtitle);
        detail = (TextView)findViewById(R.id.ratingdetail);
        r = (RadioGroup)findViewById(R.id.ratingradiogroup);
        point = (TextView)findViewById(R.id.ratingstarpoint);
        review = (EditText)findViewById(R.id.ratingreview);
        ratingbtn = (Button)findViewById(R.id.ratingbtn);

        if(usertype.equals("owner")) {
            title.setText("펫시터 평가");
            detail.setText("펫시터의 돌봄에 대한 리뷰를 작성해주세요. 약속을 성실히 이행했는지, 돌봄의 만족도는 어떘는지 등 종합적인 의견을 작성해주세요.");
            ratingtype = "펫시터";
        }
        else if(usertype.equals("sitter")) {
            title.setText("견주 평가");
            detail.setText("돌봄을 부탁한 견주에 대한 리뷰를 작성해주세요. 펫시터를 충분히 배려하였는지, 특이사항을 정확하게 고지했는지 등 종합적인 의견을 작성해주세요.");
            ratingtype = "견주";
        }

        r.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.rating1) {
                    point.setText("1");
                    m=1;
                }
                else if (i==R.id.rating2) {
                    point.setText("2");
                    m=2;
                }
                else if (i==R.id.rating3) {
                    point.setText("3");
                    m=3;
                }
                else if (i==R.id.rating4) {
                    point.setText("4");
                    m=4;
                }
                else if (i==R.id.rating5) {
                    point.setText("5");
                    m=5;
                }
            }

        });

//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.child("point").exists()){ //이미 점수가 있으면 불러와서 더하고 평균
//                    int nowpoint = Integer.parseInt(dataSnapshot.child("point").getValue().toString());
//
//                    if(point.getText().toString().equals("1")) newpoint = 1;
//                    else if (point.getText().toString().equals("2")) newpoint = 2;
//                    else if (point.getText().toString().equals("3")) newpoint = 3;
//                    else if (point.getText().toString().equals("4")) newpoint = 4;
//                    else if (point.getText().toString().equals("5")) newpoint = 5;
//
//                    finalpoint = nowpoint + newpoint;
//                }
//                else{ //기존 점수가 없으면
//
//                    if(point.getText().toString().equals("1")) newpoint = 1;
//                    else if (point.getText().toString().equals("2")) newpoint = 2;
//                    else if (point.getText().toString().equals("3")) newpoint = 3;
//                    else if (point.getText().toString().equals("4")) newpoint = 4;
//                    else if (point.getText().toString().equals("5")) newpoint = 5;
//
//                    finalpoint = newpoint;
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        ratingbtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                if(point.getText().toString().equals("0"))  Toast.makeText(getApplicationContext(), "평점을 입력해주세요", Toast.LENGTH_LONG).show();
                else{

                    databaseReference.child("matching").child(matching_id).child("status").child(usertype).setValue("종료");

                    databaseReference.child("rating").child(your_id).child("review").push().setValue(review.getText().toString());

                    databaseReference.child("rating").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String mm;
                            String cc;
                            if(dataSnapshot.child(your_id).exists()){

                                if(dataSnapshot.child(your_id).child("totalpoint").exists()){
                                    //총점 가져오기
                                    mm = dataSnapshot.child(your_id).child("totalpoint").getValue().toString();
                                } else{
                                    mm = "0";
                                }
                                if(dataSnapshot.child(your_id).child("count").exists()){
                                    //카운트 가져오기
                                    cc = dataSnapshot.child(your_id).child("count").getValue().toString();
                                } else{
                                    cc = "0";
                                }
                            }
                            else{
                                mm = "0"; cc = "0";
                            }

                            int mmm = Integer.parseInt(mm);
                            int count = Integer.parseInt(cc);
//                            int n = m; //입력한 점수
                            int ff = mmm+m; // 가져온 총점 + 입력한 점수
                            count++;

                            databaseReference.child("rating").child(your_id).child("totalpoint").setValue(ff);
                            databaseReference.child("rating").child(your_id).child("count").setValue(count);

                            int evep = ff/count; //점수 평균
                            databaseReference.child("rating").child(your_id).child("평점평균").setValue(evep);


                            addRatedSitter(your_id, usertype, evep);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    if(usertype.equals("owner")) {
                        terminateOK(usertype);
                        removeSittingDetailInfo(); //usertype이 owner이면 매칭이 종료된 후 '펫시터 구하기' 정보 삭제
                    }

                    else if(usertype.equals("sitter")) {
                        terminateOK(usertype);
                        removeSittingApplicationInfo(); //usertype이 owner이면 매칭이 종료된 후 sitting application info에서 정보 삭제
                    }

                    finish();
                }
            }
        });
    }


    private void terminateOK(final String ut){
        databaseReference.child("matching").child(matching_id).child("status").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean a = dataSnapshot.child(ut).getValue().toString().equals("종료");
//                Boolean b = dataSnapshot.child("sitter").getValue().toString().equals("종료");
//                terminateOK = a && b;
                terminateOK = a;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void removeSittingDetailInfo(){
        //매칭이 종료되면 DB에서 owner의 sitting_detail_info(견주가 '펫시터 구하기'를 위해 입력한 정보) 삭제
        databaseReference.child("sitting_detail_info").child(my_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(terminateOK) {
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        postSnapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void removeSittingApplicationInfo(){
        //매칭이 종료되면 DB에서 sitting application info에서 정보 삭제
        databaseReference.child("sitting_application_info").child(my_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(terminateOK) {
//                    dataSnapshot.child("application_id").getRef().setValue("");
                    databaseReference.child("sitting_application_info").push().child("application_id").setValue(my_id+":done");
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        postSnapshot.getRef().removeValue();
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addRatedSitter(final String y, String ut, final int ev){
        if(ut.equals("owner")){
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int ord = 5-ev;

                    databaseReference.child("rated_sitter").child(y).child("point").setValue(ev);
                    databaseReference.child("rated_sitter").child(y).child("order").setValue(ord);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

//
//    private int nowpoint(){
//
//        databaseReference.child("rating").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.child(your_id).child("point").exists()){ //이미 점수가 있으면 받아오기
////                    p = Integer.parseInt(dataSnapshot.child(your_id).child("point").getValue().toString());
//                    p = 8;
//
//                }
//                else{ //기존 점수가 없으면 0
//                    p = 0;
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        return p;
//    }
}
