package com.sw.PuppyFriends;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

class FcmToken {

    public String id;
    public String token;

    public FcmToken() {}

    public FcmToken(String id, String token){
        this.id = id;
        this.token = token;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("id", id);
        result.put("token", token);

        return result;
    }
}
