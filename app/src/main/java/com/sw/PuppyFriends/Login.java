package com.sw.PuppyFriends;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    private EditText SignInMail, SignInPass;
    String usermail, pass;
    private FirebaseAuth auth;
    private Button SignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_login);
        SignInMail = (EditText) findViewById(R.id.SignInMail);
        SignInPass = (EditText) findViewById(R.id.SignInPass);
        SignInButton = (Button) findViewById(R.id.SignInButton);
        auth = FirebaseAuth.getInstance();

        SignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = SignInMail.getText().toString();
                final String password = SignInPass.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "이메일 주소를 입력해 주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해 주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    if (password.length() < 5) {
                                        Toast.makeText(getApplicationContext(),"비밀번호는 5자리 이상이어야 합니다",Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(),"에러",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });

                usermail = SignInMail.getText().toString();
                pass = SignInPass.getText().toString();
                final String user = usermail.substring(0, usermail.lastIndexOf("@"));

                    String url = "https://authentication-1c209.firebaseio.com/users.json";

                final ProgressDialog pd = new ProgressDialog(Login.this);
                pd.setMessage("Loading...");
                pd.show();

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String s) {
                            if(s.equals("null")){
                                Toast.makeText(Login.this, "user not found", Toast.LENGTH_LONG).show();
                            }
                            else{
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if(!obj.has(user)){
                                        Toast.makeText(Login.this, "user not found", Toast.LENGTH_LONG).show();
                                    }
                                    else if(obj.getJSONObject(user).getString("password").equals(pass)){
                                        UserDetails.username = user;
                                        UserDetails.password = pass;
                                        Intent intent = new Intent(Login.this, HomeActivity.class);
                                        intent.putExtra("id", SignInMail.getText().toString().split("\\.")[0]);
                                        startActivity(intent);
                                    }
                                    else {
                                        Toast.makeText(Login.this, "incorrect password", Toast.LENGTH_LONG).show();
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
                            System.out.println("" + volleyError);
                            pd.dismiss();
                        }
                    });

                    RequestQueue rQueue = Volley.newRequestQueue(Login.this);
                    rQueue.add(request);
            }
        });
    }


    public void NavigateSignUp(View v) {

        final ProgressDialog pd = new ProgressDialog(Login.this);
        pd.setMessage("Loading...");
        pd.show();

        Intent inent = new Intent(this, Register.class);
        startActivity(inent);
    }
    public void NavigateForgetMyPassword(View v) {

        final ProgressDialog pd = new ProgressDialog(Login.this);
        pd.setMessage("Loading...");
        pd.show();

        Intent inent = new Intent(this, ResetPasswordActivity.class);
        startActivity(inent);
    }
}
