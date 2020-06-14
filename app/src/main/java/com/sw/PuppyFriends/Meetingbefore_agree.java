package com.sw.PuppyFriends;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Meetingbefore_agree extends Activity {

    String matching_id;
    String owner_id;
    String sitter_id;
    Button yes, no;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetingbefore_agree);


        Intent intent = getIntent();
        matching_id = intent.getExtras().getString("matching_id");
        owner_id = intent.getExtras().getString("owner_id");
        sitter_id = intent.getExtras().getString("sitter_id");

        yes = (Button)findViewById(R.id.yes);
        no = (Button)findViewById(R.id.no);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Meeting.class);
                intent.putExtra("owner_id", owner_id);
                intent.putExtra("sitter_id", sitter_id);
                intent.putExtra("matching_id", matching_id);
                startActivity(intent);
                finish();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}