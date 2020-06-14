package com.sw.PuppyFriends;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {

    EditText SignUpMail,SignUpPass;
    String usermail, pass;
    Button SignUpButton;
    private FirebaseAuth auth;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        SignUpMail = findViewById(R.id.SignUpMail);
        SignUpPass = findViewById(R.id.SignUpPass);
        auth=FirebaseAuth.getInstance();
        SignUpButton = (Button) findViewById(R.id.SignUpButton);
        SignUpButton.setEnabled(false);

        Firebase.setAndroidContext(this);

        SignUpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String email = SignUpMail.getText().toString();
                final String pass = SignUpPass.getText().toString();

                final ProgressDialog pd = new ProgressDialog(Register.this);
                pd.setMessage("Loading...");
                pd.show();
                if( !SignUpButton.isEnabled()) {
                    Toast.makeText(getApplicationContext(), "이메일 주소를 입력해 주세요", Toast.LENGTH_LONG).show();
                }
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(),"이메일 주소를 입력해 주세요",Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    Toast.makeText(getApplicationContext(),"비밀번호를 입력해 주세요",Toast.LENGTH_LONG).show();
                }
                if (pass.length() == 0){
                    Toast.makeText(getApplicationContext(),"비밀번호를 입력해 주세요",Toast.LENGTH_LONG).show();
                }
                if (pass.length()<6){
                    Toast.makeText(getApplicationContext(),"비밀번호는 6자리 이상이어야 합니다",Toast.LENGTH_LONG).show();
                }
                else{
                    auth.createUserWithEmailAndPassword(email,pass)
                            .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (!task.isSuccessful()) {
                                        Toast.makeText(Register.this, "에러",Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        Intent intent = new Intent(Register.this, EditProfileActivity.class);
                                        intent.putExtra("id", SignUpMail.getText().toString().split("\\.")[0]);
                                        startActivity(intent);
                                    }
                                }
                            });}

            }
        });
    }

    public void navigate_sign_in(View v){
        usermail = SignUpMail.getText().toString();
        pass = SignUpPass.getText().toString();
        SignUpButton.setEnabled(true);
        final String user = usermail.substring(0, usermail.lastIndexOf("@"));

        final ProgressDialog pd = new ProgressDialog(Register.this);
        pd.setMessage("Loading...");
        pd.show();

        String url = "https://authentication-1c209.firebaseio.com/users.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                Firebase reference = new Firebase("https://authentication-1c209.firebaseio.com/users");

                if(s.equals("null")) {
                    reference.child(user).child("password").setValue(pass);
                    Toast.makeText(Register.this, "등록 가능", Toast.LENGTH_LONG).show();
                }
                if(usermail.length() == 0){
                    Toast.makeText(getApplicationContext(),"이메일 주소를 입력해 주세요",Toast.LENGTH_LONG).show();
                    return;
                }
                if(pass.length() == 0){
                    Toast.makeText(getApplicationContext(),"비밀번호를 입력해 주세요",Toast.LENGTH_LONG).show();
                }
                if (pass.length()<6){
                    Toast.makeText(getApplicationContext(),"비밀번호는 6자리 이상이어야 합니다",Toast.LENGTH_LONG).show();
                }
                else {
                    try {
                        JSONObject obj = new JSONObject(s);

                        if (!obj.has(user)) {
                            reference.child(user).child("password").setValue(pass);
                            Toast.makeText(Register.this, "등록 가능", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(Register.this, "이메일 중복", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                pd.dismiss();
            }

        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError );
                pd.dismiss();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(Register.this);
        rQueue.add(request);

    }
}