package com.sw.PuppyFriends;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;

public class CheckProfileActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private TextView profileNameTextView, profileAddressTextView, profilePhonenoTextView;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private ImageView profilePicImageView;
    private FirebaseStorage firebaseStorage;
    private TextView textViewemailname;

    String id, my_id;
    //id = 쟤, my_id = 나

    Boolean infavorite;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_profile);

        FloatingActionButton message = findViewById(R.id.message);
        FloatingActionButton favorite = findViewById(R.id.favorite);
        FloatingActionButton report = findViewById(R.id.report);

        Intent intent = getIntent();
        id = intent.getExtras().getString("id");
        my_id = intent.getExtras().getString("my_id");
//        final String username = id.substring(0, id.lastIndexOf("@"));
        String[] un = id.split("@");
        String[] me = my_id.split("@");
        final String my_idid = me[0];
        final String username = un[0];
        Toast.makeText(CheckProfileActivity.this, "clicked : " + username, Toast.LENGTH_SHORT).show();

        profilePicImageView = findViewById(R.id.profile_pic_imageView_some);
        profileNameTextView = findViewById(R.id.profile_name_textView_some);
        profileAddressTextView = findViewById(R.id.profile_address_textView_some);
        profilePhonenoTextView = findViewById(R.id.profile_phoneno_textView_some);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();

        storageReference.child("ProfilePic").child(username).child("Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerInside().into(profilePicImageView);
            }
        });

        databaseReference.child("users").child(username).child("Info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                Userinformation userProfile = dataSnapshot.getValue(Userinformation.class);
                profileNameTextView.setText(dataSnapshot.child("name").getValue().toString());
                profileAddressTextView.setText(dataSnapshot.child("address").getValue().toString());
                profilePhonenoTextView.setText(dataSnapshot.child("phoneno").getValue().toString());
            }
            @Override
            public void onCancelled( DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });


        databaseReference.child("users").child(my_idid).child("favorite").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                infavorite = dataSnapshot.child(id).exists();
            }
            @Override
            public void onCancelled( DatabaseError databaseError) {
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), Users.class);
                intent1.putExtra("target_id", username);
                startActivity(intent1);
                Snackbar.make(view, "메세지 보내기", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(infavorite){ //관심목록에 있으면
                    databaseReference.child("users").child(my_idid).child("favorite").child(id).getRef().removeValue();
                    Toast.makeText(getApplicationContext(), "관심 목록에서 삭제되었습니다", Toast.LENGTH_LONG).show();
                }
                else{ //관심목록에 없으면
                    databaseReference.child("users").child(my_idid).child("favorite").child(id).setValue(id);
                    Toast.makeText(getApplicationContext(), "관심 목록에 추가되었습니다", Toast.LENGTH_LONG).show();
                }
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), ReportActivity.class);
                intent1.putExtra("my_id", my_idid);
                startActivity(intent1);
                Snackbar.make(view, "신고하기", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }


}