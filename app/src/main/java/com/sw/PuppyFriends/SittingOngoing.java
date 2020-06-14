package com.sw.PuppyFriends;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SittingOngoing extends AppCompatActivity {

    private DatabaseReference conditionRef = FirebaseDatabase.getInstance().getReference();

    private static final int COUNT_DOWN_INTERVAL = 60000;

    Button btn_calling, btn_endcaring;
    TextView textView_title, timetv;

    private int count;
    private CountDownTimer countDownTimer;

    String matching_id, usertype, owner_id, sitter_id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ongoing_petsitting);

        Intent intent = getIntent();
        matching_id = intent.getExtras().getString("matching_id");
        usertype = intent.getExtras().getString("usertype");
        owner_id = intent.getExtras().getString("owner_id");
        sitter_id = intent.getExtras().getString("sitter_id");
        Log.d("output : ", owner_id + sitter_id);
        final String[] splitedOwnerId = owner_id.split("@");
        final String[] splitedSitterId = sitter_id.split("@");

        btn_calling = (Button) findViewById(R.id.btn_calling);
        btn_calling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), Users.class);

                if(usertype.equals("sitter"))
                    intent1.putExtra("target_id", splitedOwnerId[0]);
                else
                    intent1.putExtra("target_id", splitedSitterId[0]);

                startActivity(intent1);
            }
        });
        btn_endcaring = (Button) findViewById(R.id.btn_endcaring);
        btn_endcaring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(usertype.equals("sitter")){
                    Intent matchintent = new Intent(SittingOngoing.this, matchreport.class);
                    matchintent.putExtra("matching_id", matching_id);
                    matchintent.putExtra("usertype", "sitter");
                    matchintent.putExtra("owner_id", owner_id);
                    matchintent.putExtra("sitter_id", sitter_id);
                    startActivity(matchintent);
                    finish();
                } else if(usertype.equals("owner")){
                    Intent ratingintent = new Intent(SittingOngoing.this, Rating.class);
                    ratingintent.putExtra("your_id", sitter_id);
                    ratingintent.putExtra("usertype", "owner");
                    ratingintent.putExtra("matching_id", matching_id);
                    ratingintent.putExtra("my_id", owner_id);
                    startActivity(ratingintent);
                    finish();
                }

            }
        });

        timetv = (TextView)findViewById(R.id.timetv);
        textView_title = (TextView) findViewById(R.id.textView_title);
        startTimer();
    }

    public void countDownTimer(int time){
        count = time;

        countDownTimer = new CountDownTimer(time*COUNT_DOWN_INTERVAL, COUNT_DOWN_INTERVAL) {
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                postTime(Integer.toString(count));
                timetv.setText(count-- + "분 남았습니다.");
            }
            public void onFinish() {
                if (usertype.equals("sitter")) {
                    Intent matchintent = new Intent(SittingOngoing.this, matchreport.class);
                    matchintent.putExtra("matching_id", matching_id);
                    matchintent.putExtra("usertype", "sitter");
                    matchintent.putExtra("owner_id", owner_id);
                    matchintent.putExtra("sitter_id", sitter_id);
                    startActivity(matchintent);
                    finish();
                } else if (usertype.equals("owner")) {
                    Intent ratingintent = new Intent(SittingOngoing.this, Rating.class);
                    ratingintent.putExtra("your_id", sitter_id);
                    ratingintent.putExtra("usertype", "owner");
                    ratingintent.putExtra("matching_id", matching_id);
                    ratingintent.putExtra("my_id", owner_id);
                    startActivity(ratingintent);
                    finish();
                }
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try{
            countDownTimer.cancel();
        } catch (Exception e) {}
        countDownTimer=null;
    }

    public void startTimer(){
        conditionRef.child("matching").child(matching_id).child("사전만남").child("돌봄시간").addListenerForSingleValueEvent(new ValueEventListener() {
            String time;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                time = dataSnapshot.getValue().toString();
                Log.d("sitting time", time);
                countDownTimer(Integer.parseInt(time));
                countDownTimer.start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void postTime(String time){
        conditionRef.child("matching").child(matching_id).child("사전만남").child("돌봄시간").setValue(time);
    }
}