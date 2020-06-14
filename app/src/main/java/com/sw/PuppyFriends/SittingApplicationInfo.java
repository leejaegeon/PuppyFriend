package com.sw.PuppyFriends;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class SittingApplicationInfo {

    public String matching_id;
    public String type; // type : sitter
    public String id;   // 펫시터의 아이디
    public String is_connected;     // 견주가 자신을 선택했을 때 true가 됌
    public String application_id;   // 연결할 견주의 아이디

    // 데이터 받아올 때 필요함
    public SittingApplicationInfo(){ }

    public SittingApplicationInfo(String id, String type, String applicationId, boolean isConnected, String matchingId){
        this.type = type;
        this.id = id;
        this.application_id = applicationId;
        this.matching_id = matchingId;
        if(isConnected)
            this.is_connected = "t";
        else
            this.is_connected = "f";
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();

        result.put("id", id);
        result.put("type", type);
        result.put("is_connected", is_connected);
        result.put("application_id", application_id);
        result.put("matching_id", matching_id);

        return result;
    }

}
