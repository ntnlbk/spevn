package com.LibBib.spevn;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import yuku.ambilwarna.AmbilWarnaDialog;

public class OptionsActivity extends AppCompatActivity {

    public String name;

    private ImageButton  pluston, minuston, plustextsize, minustextsize;
    private ImageButton backbtn;
    private Button acceptbtn;
    private String extra="";
    private TextView tontext, textsizetext;
    private ImageView showchordsim, changecolorim;
    private String isfromplaylist;
    private CheckBox chords;
    public int showchords , oldshowchords;
    public  String color, oldcolor, nameofplaylist;
    public int ton, oldton, textsize, oldtextsize;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        textsizetext =(TextView)findViewById(R.id.textsize);

        acceptbtn=(Button)findViewById(R.id.accept);
        chords=(CheckBox)findViewById(R.id.chords);
        backbtn=(ImageButton)findViewById(R.id.backbtn2);
        showchordsim=(ImageView)findViewById(R.id.imageView11);

        plustextsize=(ImageButton)findViewById(R.id.plustextsize);
        minustextsize=(ImageButton)findViewById(R.id.minustextsize);
        pluston=(ImageButton)findViewById(R.id.pluston);
        minuston=(ImageButton)findViewById(R.id.minuston);
        tontext=(TextView)findViewById(R.id.tontext);
        changecolorim=(ImageView)findViewById(R.id.imageView13);


        Intent intent = getIntent();
        name = intent.getStringExtra("имя");
        isfromplaylist=intent.getStringExtra("isfromplaylist");
        if(isfromplaylist.equals("true"))
            nameofplaylist=intent.getStringExtra("nameofplaylist");
        if(name.equals("Хвала на вышынях Богу(G)"))
            extra="Gloria";
        if(name.equals("Слухай, Ізраэлю!"))
            extra="Слухай";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput("options"))); //считываение настроек
            int i=0;
            String src="";
            showchords=1;
            color="fa0000";
            ton=0;
            textsize=0;
            while ((src = br.readLine()) != null){
                switch (i) {
                    case 0:
                        showchords = Integer.parseInt(src);
                        oldshowchords = Integer.parseInt(src);
                        break;
                    case 1:
                        color=src;
                        oldcolor=src;
                        break;
                    case 2:
                        ton=Integer.parseInt(src);
                        oldton=Integer.parseInt(src);
                        break;
                    case 3:
                        textsize=Integer.parseInt(src);
                        oldtextsize=Integer.parseInt(src);
                        break;
                }
                i++;
            }

        } catch (FileNotFoundException e) {

            showchords=1;
            color="fa0000";
            ton=0;
            oldshowchords=1;
            oldcolor="fa0000";
            oldton=0;
            textsize=0;
            oldtextsize=0;
            e.printStackTrace();
        } catch (IOException e) {
            showchords=1;
            color="fa0000";
            ton=0;
            oldshowchords=1;
            oldcolor="fa0000";
            oldton=0;
            textsize=0;
            oldtextsize=0;
            e.printStackTrace();
        }

        if (showchords==1)
            chords.setChecked(true);
        else
            chords.setChecked(false);




        tontext.setText(String.valueOf(ton));
        textsizetext.setText(String.valueOf(textsize));

        acceptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(openFileOutput("options", MODE_PRIVATE)));  //обновление настроек
                    bw.write(String.valueOf(showchords));
                    bw.newLine();
                    bw.write(color);
                    bw.newLine();
                    bw.write(String.valueOf(ton));
                    bw.newLine();
                    bw.write(String.valueOf(textsize));
                    bw.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                oldcolor=color;
                oldshowchords=showchords;
                oldton=ton;
                oldtextsize=textsize;
             onBackPressed();
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        pluston.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ton<12){
                ton++;
                tontext.setText(String.valueOf(ton));
                }
            }
        });

        minuston.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ton>-12){
                ton--;
                tontext.setText(String.valueOf(ton));}
            }
        });
        minustextsize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textsize>=-5){
                textsize--;
                textsizetext.setText(String.valueOf(textsize));
                }
            }
        });
        plustextsize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textsize<=9){
                textsize++;
                textsizetext.setText(String.valueOf(textsize));
                }
            }
        });
        chords.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    showchords=1;
                else
                    showchords=0;
            }
        });

        changecolorim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ColorPickerDialog.Builder(OptionsActivity.this)
                        .setTitle("Выбор цвета")
                        .setPreferenceName("MyColorPickerDialog")
                        .setPositiveButton(getString(R.string.confirm),
                                new ColorEnvelopeListener() {
                                    @Override
                                    public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                        color = envelope.getHexCode();
                                        color= color.substring(2);



                                    }
                                })
                        .setNegativeButton(getString(R.string.cancel),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                        .attachAlphaSlideBar(false) // the default value is true.
                        .attachBrightnessSlideBar(true)  // the default value is true.
                        .setBottomSpace(12) // set a bottom space between the last slidebar and buttons.
                        .show();
            }
        });

        showchordsim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chords.isChecked()) {
                    showchords = 0;
                    chords.setChecked(false);
                }
                else {
                    showchords = 1;
                    chords.setChecked(true);
                }

            }
        });
    }

    @Override
    public void onBackPressed(){

        if(oldcolor.equals(color) && oldshowchords==showchords && oldton==ton && oldtextsize==textsize) {

            if (name.equals("none")) {
                Intent intent = new Intent(OptionsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(OptionsActivity.this, textofsongwindoww.class);
                intent.putExtra("имя", name);
                intent.putExtra("extra", extra);
                if(isfromplaylist.equals("true")){
                    intent.putExtra("isfromplaylist", "true");
                    intent.putExtra("nameofplaylist", nameofplaylist);
                }
                else{
                    intent.putExtra("isfromplaylist", "fasle");
                }
                startActivity(intent);
                finish();
            }
        } else{
            AlertDialog.Builder builder = new AlertDialog.Builder(OptionsActivity.this);
            builder.setTitle("Подтвердите выход")
                    .setMessage("Есть несохраненные изменения в настройках")
                    .setPositiveButton("Отменить выход", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Закрываем окно
                            dialog.cancel();
                        }

                    }).setNegativeButton("Выйти", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (name.equals("none")) {
                        Intent intent = new Intent(OptionsActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(OptionsActivity.this, textofsongwindoww.class);
                        intent.putExtra("имя", name);
                        intent.putExtra("extra", extra);
                        if(isfromplaylist.equals("true")){
                            intent.putExtra("isfromplaylist", "true");
                            intent.putExtra("nameofplaylist", nameofplaylist);}
                        else
                            intent.putExtra("isfromplaylist", "fasle");
                        startActivity(intent);
                        finish();
                    }
                }
            });
                builder.create();
                builder.show();
        }
    }


}