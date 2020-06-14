package com.sw.PuppyFriends;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


public class Meeting extends AppCompatActivity {

    String matching_id;
    String owner_id;
    String sitter_id;

    LinearLayout container;
    EditText meeting_edit;
    Button complete;
    EditText feeEdit;

    TextView meetingownerid, meetingsitterid;

    EditText application_date, meetingguide1, meetingguide2, meetingguide3, meetingguide4, meetingguide5, meetingguide6, meetingguide7;


    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meeting);

        Intent intent = getIntent();
        matching_id = intent.getExtras().getString("matching_id");
        owner_id = intent.getExtras().getString("owner_id");
        sitter_id = intent.getExtras().getString("sitter_id");
        Toast.makeText(Meeting.this, "matching id : " + matching_id, Toast.LENGTH_SHORT).show();

        databaseReference.child("matching").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(matching_id).child("사전만남").exists()){
                    //사전만남 이미 했으면
                    Intent intent = new Intent(getApplicationContext(), MeetingResult.class);
                    intent.putExtra("owner_id", owner_id);
                    intent.putExtra("sitter_id", sitter_id);
                    intent.putExtra("matching_id", matching_id);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        meetingownerid = (TextView)findViewById(R.id.meetingownerid);
        meetingsitterid = (TextView)findViewById(R.id.meetingsitterid);
        meetingownerid.setText("견주 : "+owner_id);
        meetingsitterid.setText("펫시터 : "+sitter_id);

        container = (LinearLayout)findViewById(R.id.container);
        meeting_edit = (EditText)findViewById(R.id.meeting_edit);

        feeEdit = (EditText)findViewById(R.id.fee);
        complete = (Button)findViewById(R.id.complete);

        application_date = (EditText)findViewById(R.id.application_date);
        meetingguide1 = (EditText)findViewById(R.id.meetingguide1);
        meetingguide2 = (EditText)findViewById(R.id.meetingguide2);
        meetingguide3 = (EditText)findViewById(R.id.meetingguide3);
        meetingguide4 = (EditText)findViewById(R.id.meetingguide4);
        meetingguide5 = (EditText)findViewById(R.id.meetingguide5);
        meetingguide6 = (EditText)findViewById(R.id.meetingguide6);
        meetingguide7 = (EditText)findViewById(R.id.meetingguide7);

        complete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                final String date = application_date.getText().toString();
                final String detail1 = meetingguide1.getText().toString();
                final String detail2 = meetingguide2.getText().toString();
                final String detail3 = meetingguide3.getText().toString();
                final String detail4 = meetingguide4.getText().toString();
                final String detail5 = meetingguide5.getText().toString();
                final String detail6 = meetingguide6.getText().toString();
                final String detail7 = meetingguide7.getText().toString();
                final String etc = meeting_edit.getText().toString();
                final String fee = feeEdit.getText().toString();

                //데이터베이스에 올리기
                if(date.isEmpty()) Toast.makeText(getApplicationContext(), "날짜를 입력해주세요", Toast.LENGTH_LONG).show();
                else if(detail1.isEmpty() || detail2.isEmpty() || detail3.isEmpty() || detail4.isEmpty() || detail5.isEmpty()){
                    Toast.makeText(getApplicationContext(), "빈 칸을 채워주세요", Toast.LENGTH_LONG).show();
                }
                else if(fee.isEmpty()) Toast.makeText(getApplicationContext(), "최종 가격을 입력해주세요", Toast.LENGTH_LONG).show();
                    //특이사항이랑 기타 이외에는 필수적으로 채워야 한다
                else{
                    databaseReference.child("matching").child(matching_id).child("사전만남").child("날짜").setValue(date);
                    databaseReference.child("matching").child(matching_id).child("사전만남").child("장소").setValue(detail1);
                    databaseReference.child("matching").child(matching_id).child("사전만남").child("밥주는시간").setValue(detail2);
                    databaseReference.child("matching").child(matching_id).child("사전만남").child("사료위치").setValue(detail3);
                    databaseReference.child("matching").child(matching_id).child("사전만남").child("산책시간").setValue(detail4);
                    databaseReference.child("matching").child(matching_id).child("사전만남").child("산책상세").setValue(detail5);
                    databaseReference.child("matching").child(matching_id).child("사전만남").child("특이사항").setValue(detail6);
                    databaseReference.child("matching").child(matching_id).child("사전만남").child("돌봄시간").setValue(detail7);
                    databaseReference.child("matching").child(matching_id).child("사전만남").child("etc").setValue(etc);
                    databaseReference.child("matching").child(matching_id).child("사전만남").child("가격").setValue(fee);

                    databaseReference.child("matching").child(matching_id).child("owner_id").setValue(owner_id);
                    databaseReference.child("matching").child(matching_id).child("sitter_id").setValue(sitter_id);

                    databaseReference.child("matching").child(matching_id).child("사전만남").child("owner_agree").setValue("not yet");
                    databaseReference.child("matching").child(matching_id).child("사전만남").child("sitter_agree").setValue("not yet");

                    databaseReference.child("matching").child(matching_id).child("status").child("owner").setValue("진행중");
                    databaseReference.child("matching").child(matching_id).child("status").child("sitter").setValue("진행중");

                    Intent intent = new Intent(getApplicationContext(), MeetingResult.class);
                    intent.putExtra("owner_id", owner_id);
                    intent.putExtra("sitter_id", sitter_id);
                    intent.putExtra("matching_id", matching_id);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
