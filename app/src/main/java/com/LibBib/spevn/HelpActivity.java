package com.LibBib.spevn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class HelpActivity extends AppCompatActivity {

    private ImageView backbtn;
    private LinearLayout addsongbtn, addaudiobtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        backbtn = (ImageView) findViewById(R.id.backbtn3);
        addsongbtn = (LinearLayout) findViewById(R.id.songbtn);
        addaudiobtn = (LinearLayout) findViewById(R.id.audiobtn);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        addsongbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HelpActivity.this, addsongactivity.class);
                startActivity(intent);
            }
        });

        addaudiobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HelpActivity.this, addaudioactivity.class);
                startActivity(intent);
            }
        });

    }
}