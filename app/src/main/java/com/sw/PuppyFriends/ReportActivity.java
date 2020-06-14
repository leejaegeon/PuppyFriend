package com.sw.PuppyFriends;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReportActivity extends Activity {
    Button sendReport;
    EditText Title;
    EditText textMessage;
    TextView mCounter;
    private DatabaseReference databaseReference;
    String id;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Intent intent = getIntent();
        id = intent.getExtras().getString("my_id");
        sendReport = (Button) findViewById(R.id.SendReport);
        Title = (EditText) findViewById(R.id.title);
        textMessage = (EditText) findViewById(R.id.textMessage);
        mCounter = (TextView) findViewById(R.id.counter);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        textMessage.addTextChangedListener(mTextEditorWatcher);


        sendReport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String title = Title.getText().toString();
                String message = textMessage.getText().toString();
                if (title.length()>0 && message.length()>0) {
                    databaseReference.child("report").child(id).child(title).setValue(message);
                    Toast.makeText(getBaseContext(), "보냄", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(getBaseContext(), "내용을 입력하세요", Toast.LENGTH_SHORT).show();
            }
        });
    }

    final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String smsNo;
            if(s.length() == 0)
                smsNo = "0";
            else
                smsNo = String.valueOf(s.length() + 1);
            String smsLength = String.valueOf(160-(s.length()));
            mCounter.setText(smsNo+"/"+smsLength);
        }
        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub
        }
    };

}