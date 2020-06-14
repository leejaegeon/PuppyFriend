package com.sw.PuppyFriends;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    Button searchSitterButton;
    Button doSitterButton;
    Button sittingcheck;
    Button sitterlistbtn;

    String id;
    DatabaseReference mPostReference;
    FirebaseMessageManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();

        id = intent.getExtras().getString("id");
        Toast.makeText(HomeActivity.this, "ID : " + id, Toast.LENGTH_SHORT).show();

        searchSitterButton = (Button) findViewById(R.id.search);
        doSitterButton = (Button) findViewById(R.id.sitter);
        sittingcheck = (Button)findViewById(R.id.sittingcheck);

//        postToken();

        // 푸시알림 보내는 코드
        manager = new FirebaseMessageManager();
        manager.postUpstreamMsg("userA", "test", "test message");

        sittingcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Matchinglist.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        sitterlistbtn = (Button)findViewById(R.id.sitterlistbtn);
        sitterlistbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Sitterlist.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        searchSitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //이미 등록한 sitting deatil info가 있으면 입력받는건 건너뛰고 없으면 입력받기
                databaseReference.child("sitting_detail_info").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()){ //없을 때 입력받기
                            Intent intent = new Intent(getApplicationContext(), SettingDetailInfoActivity.class);
                            intent.putExtra("id", id);
                            startActivity(intent);
                        }else{ //있을 때 건너뛰기
                            Intent intent = new Intent(getApplicationContext(), UserApplicationActivity.class);
                            intent.putExtra("id", id);
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        doSitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingConditionActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_favorites:
                                Toast.makeText(getApplicationContext(), "Favorites", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(HomeActivity.this, Favorite.class);
                                intent.putExtra("id",id);

                                startActivity(intent);
                                break;
                            case R.id.action_home:
                                Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.action_profile:
                                Intent intentProfile = new Intent(HomeActivity.this, ProfileActivity.class);
                                intentProfile.putExtra("id",id);
                                startActivity(intentProfile);
                                break;
                        }
                        return true;
                    }
                });
    }

//    public void putUserToken(String token){
//        mPostReference = FirebaseDatabase.getInstance().getReference();
//
//        Map<String, Object> childUpdates = new HashMap<>();
//        Map<String, Object> postValue = null;
//
//        String[] splitedId = id.split("@");
//
//        FcmToken post = new FcmToken(splitedId[0], token);
//
//        postValue = post.toMap();
//
//        childUpdates.put("/fcm_token/" + splitedId[0] , postValue);
//        mPostReference.updateChildren(childUpdates);
//    }

//    public void postToken(){
//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                    String token;
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        if (!task.isSuccessful()) {
//                            Log.w("getInstanceId failed", task.getException());
//                            return;
//                        }
//
//                        token = task.getResult().getToken();
//                        Log.d("user token", token);
//
//                        putUserToken(token);
//                    }
//                });
//    }
}