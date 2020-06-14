package com.sw.PuppyFriends;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

class GetToken implements ValueEventListener {

    private String id;
    private String mUserToken;

    public GetToken(String id){
        this.id = id;
    }

    public String getmUserToken(){
        return mUserToken;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        String key = dataSnapshot.getKey();
        Log.d("firebase key : ", key);

        if(key.equals(id)){
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                mUserToken = Objects.requireNonNull(postSnapshot.getValue()).toString();
            }
            Log.d("get user token", mUserToken);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
