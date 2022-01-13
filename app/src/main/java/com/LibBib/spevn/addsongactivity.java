package com.LibBib.spevn;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class addsongactivity extends AppCompatActivity {

    private ImageView backbtn;

    private TextView addtextfilebtn, addmusfilebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addsongactivity);

        backbtn=(ImageView)findViewById(R.id.backbtn1);
        addtextfilebtn = (TextView)findViewById(R.id.textfileaddbtn);
        addmusfilebtn = (TextView)findViewById(R.id.musfileaddbtn);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        addtextfilebtn.setText(Html.fromHtml("или вы можете <font color='#FFC700'> прикрепить текстовый файл </font>"));  //делаем выделенные слова желтыми
        addmusfilebtn.setText(Html.fromHtml("или вы можете <font color='#FFC700'> прикрепить аудиофайл </font>"));

    }
}