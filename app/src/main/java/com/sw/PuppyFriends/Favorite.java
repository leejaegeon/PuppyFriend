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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Favorite extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    static int cnt = 0;

    String id;

    LinearLayout linearLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite);

        linearLayout = (LinearLayout)findViewById(R.id.favoritelayout);

        Intent intent = getIntent();
        id = intent.getExtras().getString("id");
        String[] nn = id.split("@");
        final String idid = nn[0];

        databaseReference.child("users").child(idid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("favorite").exists()){
                    getFavorite(idid);
                }
                else{
                    linearLayout.removeAllViews();
                    addTextViewLayout("추가한 관심 사용자가 없습니다");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getFavorite(String id){
        databaseReference.child("users").child(id).child("favorite").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                linearLayout.removeAllViews();

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                    String favoritekey = postSnapshot.getKey();
                    addTextViewLayout(favoritekey);
                    addButtonLayout(favoritekey);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

    @SuppressLint("ResourceAsColor")
    private void addButtonLayout(String st){
        final Button btn = new Button(this);
        final Button btn2 = new Button(this);

        btn.setText("프로필");
        btn.setGravity(Gravity.CENTER);
        btn.setTextSize(20);
        btn.setId(cnt++);
        btn.setBackgroundColor(getResources().getColor(R.color.purple1));

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        param.bottomMargin = 30;
        btn.setLayoutParams(param);

        @SuppressLint("ResourceType") TextView t = findViewById(btn.getId()-1);
        final String seletedId = t.getText().toString();

        btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(getApplicationContext(), CheckProfileActivity.class);
                        intent.putExtra("id", seletedId);
                        intent.putExtra("my_id",id);
                        startActivity(intent);

                    }
                }, 500);
            }
        });

        linearLayout.addView(btn);
    }

}
