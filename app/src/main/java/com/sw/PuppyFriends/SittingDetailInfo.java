package com.sw.PuppyFriends;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

// 견주들이 작성한 정보를 저장할 클래스
public class SittingDetailInfo {

    public String id;
    public String type;

    public String dogName;
    public String dogBreed;
    public String dogAge;
    public String userName;
    public String userAge;
    public String userGender;
    public String location;
    public String date;
    public String desired_price;

    public SittingDetailInfo(){ }

    public SittingDetailInfo(String id, String dogName, String dogBreed, String dogAge, String userName,
                             String userAge, String userGender, String location, String date, String desiredPrice) {
        this.id = id;
        this.dogName = dogName;
        this.dogBreed = dogBreed;
        this.dogAge = dogAge;
        this.userName = userName;
        this.userAge = userAge;
        this.userGender = userGender;
        this.location = location;
        this.date = date;
        this.desired_price = desiredPrice;
        this.type = "not-sitter";
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();

        result.put("id", id);
        result.put("type", type);
        result.put("dog_name", dogName);
        result.put("dog_breed", dogBreed);
        result.put("dog_age", dogAge);
        result.put("user_name", userName);
        result.put("user_age", userAge);
        result.put("user_gender", userGender);
        result.put("location", location);
        result.put("date", date);
        result.put("desired_price", desired_price);

        return result;
    }
}
