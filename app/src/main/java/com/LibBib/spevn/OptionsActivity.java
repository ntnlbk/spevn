package com.LibBib.spevn;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

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

    private ImageButton pluston, minuston, plustextsize, minustextsize;
    private ImageButton backbtn;
    private Button acceptbtn;
    private String extra = "";
    private TextView tontext, textsizetext;
    private ImageView showchordsim, changecolorim, darkthemeim;
    private String isfromplaylist;
    private CheckBox chords, darkthemecheck;
    public int showchords, oldshowchords;
    private int darktheme;
    public String color, oldcolor, nameofplaylist;
    public int ton, oldton, textsize, oldtextsize, olddarktheme;
    private int temp;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putInt("darktheme", darktheme);
        savedInstanceState.putInt("ton", ton);
        savedInstanceState.putInt("showchords", showchords);
        savedInstanceState.putString("color", color);
        savedInstanceState.putInt("textsize", textsize);
        // Always call the superclass so it can save the view hierarchy state
      temp =  AppCompatDelegate.getDefaultNightMode();
     // Toast.makeText(OptionsActivity.this, String.valueOf(AppCompatDelegate.getDefaultNightMode()),Toast.LENGTH_LONG).show();

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        //  AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        textsizetext = (TextView) findViewById(R.id.textsize);
        temp =  AppCompatDelegate.getDefaultNightMode();
        darkthemeim = (ImageView) findViewById(R.id.imageView9);
        acceptbtn = (Button) findViewById(R.id.accept);
        darkthemecheck = (CheckBox) findViewById(R.id.dark_theme);
        chords = (CheckBox) findViewById(R.id.chords);
        backbtn = (ImageButton) findViewById(R.id.backbtn2);
        showchordsim = (ImageView) findViewById(R.id.imageView11);
       // Toast.makeText(OptionsActivity.this, String.valueOf(AppCompatDelegate.getDefaultNightMode()),Toast.LENGTH_LONG).show();
        plustextsize = (ImageButton) findViewById(R.id.plustextsize);
        minustextsize = (ImageButton) findViewById(R.id.minustextsize);
        pluston = (ImageButton) findViewById(R.id.pluston);
        minuston = (ImageButton) findViewById(R.id.minuston);
        tontext = (TextView) findViewById(R.id.tontext);
        changecolorim = (ImageView) findViewById(R.id.imageView13);

        Intent intent = getIntent();
        name = intent.getStringExtra("имя");
        isfromplaylist = intent.getStringExtra("isfromplaylist");
        if (isfromplaylist.equals("true"))
            nameofplaylist = intent.getStringExtra("nameofplaylist");
        if (name.equals("Хвала на вышынях Богу(G)"))
            extra = "Gloria";
        if (name.equals("Слухай, Ізраэлю!"))
            extra = "Слухай";
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            ton = savedInstanceState.getInt("ton");
            darktheme = savedInstanceState.getInt("darktheme");
            showchords = savedInstanceState.getInt("showchords");
            color = savedInstanceState.getString("color");
            textsize = savedInstanceState.getInt("textsize");
        } else {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput("options"))); //считываение настроек
                int i = 0;
                String src = "";
                showchords = 1;
                color = "fa0000";
                ton = 0;
                textsize = 0;
                darktheme = 0;
                olddarktheme = 0;
                while ((src = br.readLine()) != null) {
                    switch (i) {
                        case 0:
                            showchords = Integer.parseInt(src);
                            oldshowchords = Integer.parseInt(src);
                            break;
                        case 1:
                            color = src;
                            oldcolor = src;
                            break;
                        case 2:
                            ton = Integer.parseInt(src);
                            oldton = Integer.parseInt(src);
                            break;
                        case 3:
                            textsize = Integer.parseInt(src);
                            oldtextsize = Integer.parseInt(src);
                            break;
                        case 4:
                            olddarktheme = Integer.parseInt(src);
                            darktheme = olddarktheme;
                            break;
                    }
                    i++;
                }

            } catch (FileNotFoundException e) {

                showchords = 1;
                color = "fa0000";
                ton = 0;
                oldshowchords = 1;
                oldcolor = "fa0000";
                oldton = 0;
                textsize = 0;
                oldtextsize = 0;
                darktheme = 0;
                olddarktheme = 0;
                e.printStackTrace();
            } catch (IOException e) {
                showchords = 1;
                color = "fa0000";
                ton = 0;
                oldshowchords = 1;
                oldcolor = "fa0000";
                oldton = 0;
                textsize = 0;
                oldtextsize = 0;
                darktheme = 0;
                olddarktheme = 0;
                e.printStackTrace();
            }
        }
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput("options"))); //считываение настроек
            int i = 0;
            String src = "";
            olddarktheme = 0;
            while ((src = br.readLine()) != null) {
                switch (i) {
                    case 0:
                        oldshowchords = Integer.parseInt(src);
                        break;
                    case 1:

                        oldcolor = src;
                        break;
                    case 2:

                        oldton = Integer.parseInt(src);
                        break;
                    case 3:

                        oldtextsize = Integer.parseInt(src);
                        break;
                    case 4:
                        olddarktheme = Integer.parseInt(src);

                        break;
                }
                i++;
            }

        } catch (FileNotFoundException e) {

            showchords = 1;
            color = "fa0000";
            ton = 0;
            oldshowchords = 1;
            oldcolor = "fa0000";
            oldton = 0;
            textsize = 0;
            oldtextsize = 0;
            darktheme = 0;
            olddarktheme = 0;
            e.printStackTrace();
        } catch (IOException e) {
            showchords = 1;
            color = "fa0000";
            ton = 0;
            oldshowchords = 1;
            oldcolor = "fa0000";
            oldton = 0;
            textsize = 0;
            oldtextsize = 0;
            darktheme = 0;
            olddarktheme = 0;
            e.printStackTrace();
        }

        if (showchords == 1)
            chords.setChecked(true);
        else
            chords.setChecked(false);

        if (olddarktheme == 1)
            darkthemecheck.setChecked(true);
        else {
            darkthemecheck.setChecked(false);
        }

        tontext.setText(String.valueOf(ton));
        textsizetext.setText(String.valueOf(textsize));

        acceptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (savedInstanceState != null)
                        savedInstanceState.clear();
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(openFileOutput("options", MODE_PRIVATE)));  //обновление настроек
                    bw.write(String.valueOf(showchords));
                    bw.newLine();
                    bw.write(color);
                    bw.newLine();
                    bw.write(String.valueOf(ton));
                    bw.newLine();
                    bw.write(String.valueOf(textsize));
                    bw.newLine();
                    bw.write(String.valueOf(darktheme));
                    bw.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                oldcolor = color;
                oldshowchords = showchords;
                oldton = ton;
                oldtextsize = textsize;
                olddarktheme = darktheme;
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
                if (ton < 12) {
                    ton++;
                    tontext.setText(String.valueOf(ton));
                }
            }
        });

        minuston.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ton > -12) {
                    ton--;
                    tontext.setText(String.valueOf(ton));
                }
            }
        });
        minustextsize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textsize >= -5) {
                    textsize--;
                    textsizetext.setText(String.valueOf(textsize));
                }
            }
        });
        plustextsize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textsize <= 9) {
                    textsize++;
                    textsizetext.setText(String.valueOf(textsize));
                }
            }
        });
        chords.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    showchords = 1;
                else
                    showchords = 0;
            }
        });


        changecolorim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ColorPickerDialog.Builder(OptionsActivity.this)
                        .setTitle(getString(R.string.choosing_color))
                        .setPreferenceName("MyColorPickerDialog")
                        .setPositiveButton(getString(R.string.confirm),
                                new ColorEnvelopeListener() {
                                    @Override
                                    public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                        color = envelope.getHexCode();
                                        color = color.substring(2);


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
                if (chords.isChecked()) {
                    showchords = 0;
                    chords.setChecked(false);
                } else {
                    showchords = 1;
                    chords.setChecked(true);
                }

            }
        });

        darkthemeim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (darkthemecheck.isChecked()) {
                    darkthemecheck.setChecked(false);
                    darktheme = 0;
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    temp =  AppCompatDelegate.getDefaultNightMode();

                } else {

                    darkthemecheck.setChecked(true);
                    darktheme = 1;
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    temp =  AppCompatDelegate.getDefaultNightMode();

                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (oldcolor.equals(color) && oldshowchords == showchords && oldton == ton && oldtextsize == textsize && olddarktheme == darktheme) {

            if (name.equals("none")) {
                Intent intent = new Intent(OptionsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(OptionsActivity.this, textofsongwindoww.class);
                intent.putExtra("имя", name);
                intent.putExtra("extra", extra);
                if (isfromplaylist.equals("true")) {
                    intent.putExtra("isfromplaylist", "true");
                    intent.putExtra("nameofplaylist", nameofplaylist);
                } else {
                    intent.putExtra("isfromplaylist", "fasle");
                }
                startActivity(intent);

                finish();
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(OptionsActivity.this);
            builder.setTitle(getString(R.string.accept_exit))
                    .setMessage(getString(R.string.there_are_unsaved_options))
                    .setPositiveButton(getString(R.string.cancel_exit), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Закрываем окно
                            dialog.cancel();
                        }

                    }).setNegativeButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
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
                                if (isfromplaylist.equals("true")) {
                                    intent.putExtra("isfromplaylist", "true");
                                    intent.putExtra("nameofplaylist", nameofplaylist);
                                } else
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