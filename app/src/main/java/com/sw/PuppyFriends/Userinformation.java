package com.sw.PuppyFriends;

public class Userinformation {

    public String name;
    public String address;
    public String phoneno;

    public Userinformation(){
    }

    public Userinformation(String name,String surname, String phoneno){
        this.name = name;
        this.address = surname;
        this.phoneno = phoneno;
    }
    public String getUserName() {
        return name;
    }
    public String getUserAddress() {
        return address;
    }
    public String getUserPhoneno() {
        return phoneno;
    }
}
