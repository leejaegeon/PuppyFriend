package com.sw.PuppyFriends;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class Application_status extends AppCompatActivity {

    Button btn_next;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.application_status);

        btn_next = (Button) findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent matchintent = new Intent(Application_status.this, SittingOngoing.class);
                startActivity(matchintent);
            }
            // ongoing_petsitting 연결
        });
    }
}
