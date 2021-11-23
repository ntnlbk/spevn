package com.LibBib.spevn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import java.util.zip.Inflater;

public class textofsongwindoww extends AppCompatActivity implements AdapterForRec.ItemClickListener  {

    private InterstitialAd mInterstitial;
    private String gloriabegin="G D C D <br/>";

    FileDownloadTask task;
    private boolean istask=false;
    MediaPlayer player;
    private String gloria = "Gloria in excelsis Deo! Gloria, gloria. <br/>";
    private String sluhay = "О-о-о, Israel, sh’ma Israel!";
    private  String extra;
    private String name;
    private String[] namesofsongs;
    private String[] texts;
    private String isfromplaylist, nameofplaylist;
    private TextView text, nametext, listenbtn;
    ArrayList<String> namesofsongsfromplaylist = new ArrayList<String>();
    private ImageButton optionsbtn;

    private ImageButton backbtn;
    private ProgressBar progress;
    private int showchords; // 1 - показываем, 0 - нет
    private String tempchord;
    private SeekBar seek;
    private RecyclerView songs;

    private int tonalnost=0; //тональность
    private String color=""; // цвет аккордов (код цвета)

    ArrayList<String>majorchords = new ArrayList<String>(); //список мажорных аккордов

    ArrayList<String>minorchords = new ArrayList<String>(); //список минорных аккордов

    ArrayList<String> majorchords7 = new ArrayList<String>(); // мажорные с семеркой
    ArrayList<String> minorchords7 = new ArrayList<String>(); // минорные с семеркой



    String textt;

    public textofsongwindoww() {
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textofsongwindoww);
        namesofsongs = getResources().getStringArray(R.array.names);
        texts = getResources().getStringArray(R.array.texts);
        majorchords.add("C");
        majorchords.add("C#");
        majorchords.add("D");
        majorchords.add("D#");
        majorchords.add("E");
        majorchords.add("F");
        majorchords.add("F#");
        majorchords.add("G");
        majorchords.add("G#");
        majorchords.add("A");
        majorchords.add("B");
        majorchords.add("H");

        minorchords.add("c");
        minorchords.add("c#");
        minorchords.add("d");
        minorchords.add("d#");
        minorchords.add("e");
        minorchords.add("f");
        minorchords.add("f#");
        minorchords.add("g");
        minorchords.add("g#");
        minorchords.add("a");
        minorchords.add("b");
        minorchords.add("h");

        majorchords7.add("C7");
        majorchords7.add("C#7");
        majorchords7.add("D7");
        majorchords7.add("D#7");
        majorchords7.add("E7");
        majorchords7.add("F7");
        majorchords7.add("F#7");
        majorchords7.add("G7");
        majorchords7.add("G#7");
        majorchords7.add("A7");
        majorchords7.add("B7");
        majorchords7.add("H7");

        minorchords7.add("c7");
        minorchords7.add("c#7");
        minorchords7.add("d7");
        minorchords7.add("d#7");
        minorchords7.add("e7");
        minorchords7.add("f7");
        minorchords7.add("f#7");
        minorchords7.add("g7");
        minorchords7.add("g#7");
        minorchords7.add("a7");
        minorchords7.add("b7");
        minorchords7.add("h7");

        text = (TextView) findViewById(R.id.textView);
        backbtn = (ImageButton) findViewById(R.id.backbtn);
        optionsbtn = (ImageButton) findViewById(R.id.optionsbtn);
        nametext = (TextView) findViewById(R.id.nametext);
        songs = (RecyclerView) findViewById(R.id.songs);
        listenbtn = (TextView)findViewById(R.id.listenbtn);
        progress = (ProgressBar)findViewById(R.id.progressBar2);
        // set up the RecyclerView


        Intent intent = getIntent();

        name = intent.getStringExtra("имя");
        isfromplaylist = intent.getStringExtra("isfromplaylist");
        if (isfromplaylist.equals("true")) {
            nameofplaylist = intent.getStringExtra("nameofplaylist");
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(nameofplaylist)));
                String src = "";
                while ((src = br.readLine()) != null) {
                    namesofsongsfromplaylist.add(src);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();

            }
            songs.setLayoutManager((new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)));  // recyclerview работа
            AdapterForRec adapter = new AdapterForRec(this, namesofsongsfromplaylist, name);
            adapter.setClickListener(textofsongwindoww.this);
            songs.setAdapter(adapter);
            songs.scrollToPosition(namesofsongsfromplaylist.indexOf(name));
            songs.setVisibility(View.VISIBLE);
            text.setOnTouchListener(new OnSwipeTouchListener(textofsongwindoww.this) {

                public void onSwipeRight() {
                    if (namesofsongsfromplaylist.indexOf(name) - 1 >= 0)
                        changesogs(namesofsongsfromplaylist.indexOf(name) - 1);
                }

                public void onSwipeLeft() {
                    if (namesofsongsfromplaylist.indexOf(name) + 1 <= namesofsongsfromplaylist.size())
                        changesogs(namesofsongsfromplaylist.indexOf(name) + 1);
                }


            });

        }

        for (int i = 0; i < namesofsongs.length; i++) {
            if (namesofsongs[i].equals(name)) {
                textt = texts[i];
            }
        }
        nametext.setText(name);
        extra = intent.getStringExtra("extra");

        char[] a = textt.toCharArray();
        String texttt = "";
        String checkshowchods = "1";
        color = "fa0000";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput("options")));   //читаем файл настроек

            String src;
            int i = 0;
            while ((src = br.readLine()) != null) {
                switch (i) {
                    case 0:                      //на первой строчке - аккорды
                        checkshowchods = src;
                        break;
                    case 1:                      // на второй - цвет
                        color = src;
                        break;
                    case 2:
                        tonalnost = Integer.parseInt(src); // третья - тональность
                        break;
                }
                i++;
            }

            br.close();
        } catch (FileNotFoundException e) {
            checkshowchods = "1";
            color = "fa0000";
            tonalnost = 0;
            e.printStackTrace();
        } catch (IOException e) {
            checkshowchods = "1";
            color = "fa0000";
            tonalnost = 0;
            e.printStackTrace();
        }
        showchords = Integer.parseInt(checkshowchods);


        for (int i = 0; i < a.length; i++) {
            if (a[i] >= 65 && a[i] <= 122 && a[i] != 105 && a[i] != 98 && a[i] != 73 && a[i] != 114 || a[i] == 35 || a[i] == 55) {  //выделяем аккорды красным цветом
                if (showchords == 1) {
                    String red;
                    i += searchchords(i, a);
                    red = tempchord;
                    red = "<font color=#" + color + ">" + red + "</font> <";
                    texttt = texttt + red;
                }

            } else {
                char temp = a[i];
                String usuall = String.valueOf(temp);
                texttt = texttt + usuall;
            }
        }
        if (extra.equals("Gloria")) {
            String texttemp1 = "";
            char[] temp1 = gloriabegin.toCharArray();
            for (int i = 0; i < temp1.length; i++) {
                if (temp1[i] >= 65 && temp1[i] <= 122 && temp1[i] != 105 && temp1[i] != 98 && temp1[i] != 73 && temp1[i] != 114 || temp1[i] == 35 || temp1[i] == 55) {  //выделяем аккорды красным цветом
                    if (showchords == 1) {
                        char temp = temp1[i];
                        String red = new String(String.valueOf(temp));
                        i += searchchords(i, temp1);
                        red = tempchord;
                        red = "<font color=#" + color + ">" + red + "</font> ";
                        texttemp1 = texttemp1 + red;
                    }

                } else {
                    char temp = temp1[i];
                    String usuall = new String(String.valueOf(temp));
                    texttemp1 = texttemp1 + usuall;
                }
            }

            text.setText(Html.fromHtml(texttemp1 + gloria + texttt));

        } else {
            if (extra.equals("Слухай")) {


                text.setText(Html.fromHtml(texttt + sluhay));

            } else {

                text.setText(Html.fromHtml(texttt));

            }


        }
        text.setMovementMethod(new ScrollingMovementMethod());
        optionsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player!=null)
               player.stop();
               if(task !=null)
               task.cancel();
                Intent intent = new Intent(textofsongwindoww.this, OptionsActivity.class);
                if (isfromplaylist.equals("true")) {
                    intent.putExtra("isfromplaylist", "true");
                    intent.putExtra("nameofplaylist", nameofplaylist);
                } else {
                    intent.putExtra("isfromplaylist", "false");
                    intent.putExtra("nameofplaylist", "noplaylist");
                }
                intent.putExtra("имя", name);

                startActivity(intent);
                finish();

            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        listenbtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                LayoutInflater inflater = textofsongwindoww.this.getLayoutInflater();
                View promptsView = inflater.inflate(R.layout.listendialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(textofsongwindoww.this, R.style.DialogStyle);
                builder.setView(promptsView);

                ImageView closedialog = (ImageView) promptsView.findViewById(R.id.closeimgbtn);
                ImageView playbtn = (ImageView)promptsView.findViewById(R.id.playbtn);
                 seek = (SeekBar)promptsView.findViewById(R.id.seekBar);
                TextView alltime = (TextView)promptsView.findViewById(R.id.alltime);
                TextView curtime = (TextView)promptsView.findViewById(R.id.momenttime);
                TextView nameofsong = (TextView)promptsView.findViewById(R.id.nameofsong);
                nameofsong.setText(name);
                final Dialog dialog = builder.create();

                closedialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(task!=null)
                        task.cancel();
                        istask=false;
                        if(player!= null)
                        player.stop();
                        dialog.dismiss();
                    }
                });
                seek.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if(player!=null){
                            int songlength = player.getDuration();
                        player.seekTo((seek.getProgress() * songlength) / 100);
                        }
                        return false;
                    }
                });
                Thread timer = new Thread(new Runnable() {                                                     //новый поток для отображения процесса воспроизведени аудио
                    public void run() {
                        while (true)
                        if(player!=null)
                        while (player.getDuration() != player.getCurrentPosition()) {

                            try {
                                Thread.sleep(100);
                                int currentpos = player.getCurrentPosition();
                                int songlength = player.getDuration();
                                seek.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        seek.setProgress((currentpos * 100) / songlength);                  //отображение
                                    }
                                });

                                long curminutes = (currentpos / 1000) / 60;
                                int curseconds = (int) ((currentpos / 1000) % 60);
                                if (curseconds < 10)
                                    curtime.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            curtime.setText(curminutes + ":0" + curseconds);
                                        }
                                    });
                                else
                                    curtime.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            curtime.setText(curminutes + ":" + curseconds);
                                        }
                                    });

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
                if(!istask){
                    playbtn.setImageResource(R.drawable.playbtn);
                }else if (player.isPlaying() ){

                    playbtn.setImageResource(R.drawable.pausebtn);
                } else if(!player.isPlaying() && istask){
                    playbtn.setImageResource(R.drawable.playbtn);
                    playbtn.setClickable(false);
                }
                if(player!=null && player.isPlaying()){
                    int songlength = player.getDuration();
                    long minutes = (songlength / 1000) / 60;
                    int seconds = (int) ((songlength / 1000) % 60);
                    if (seconds < 10)
                        alltime.setText(minutes + ":0" + seconds);
                    else
                        alltime.setText(minutes + ":" + seconds);
                }
                timer.start();
                playbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(player!=null && player.isPlaying()==false &&istask){
                            player.start();

                            playbtn.setImageResource(R.drawable.pausebtn);
                        }
                        else if(istask==false){

                            playbtn.setImageResource(R.drawable.pausebtn);



                        player = new MediaPlayer();
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReference();
                        StorageReference spaceRef = storageRef.child("songs");



                        File finalTemp1 = new File("/data/data/com.LibBib.spevn/cache", name+".mp3");

                        if (finalTemp1.exists() && !istask){
                            istask=true;
                            player = MediaPlayer.create(textofsongwindoww.this, Uri.fromFile(finalTemp1));

                            player.start();

                            int songlength = player.getDuration();
                            long minutes = (songlength / 1000) / 60;
                            int seconds = (int) ((songlength / 1000) % 60);
                            if (seconds < 10)
                                alltime.setText(minutes + ":0" + seconds);
                            else
                                alltime.setText(minutes + ":" + seconds);

                        } else if(!hasConnection(textofsongwindoww.this)){
                            Toast.makeText(textofsongwindoww.this,"Проверьте подключение к интернету",Toast.LENGTH_SHORT).show();
                            playbtn.setImageResource(R.drawable.playbtn);
                        }else if(!player.isPlaying()){
                            istask=true;
                            task = (FileDownloadTask) spaceRef.child(name + ".mp3").getFile(finalTemp1).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                                    progress.setVisibility(View.INVISIBLE);

                                    player = MediaPlayer.create(textofsongwindoww.this, Uri.fromFile(finalTemp1));
                                    playbtn.setClickable(true);
                                    player.start();

                                    int songlength = player.getDuration();
                                    long minutes = (songlength / 1000) / 60;
                                    int seconds = (int) ((songlength / 1000) % 60);
                                    if (seconds < 10)
                                        alltime.setText(minutes + ":0" + seconds);
                                    else
                                        alltime.setText(minutes + ":" + seconds);



                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(textofsongwindoww.this, "Запись песни отсутствует", Toast.LENGTH_SHORT).show();
                                    playbtn.setClickable(true);
                                    playbtn.setImageResource(R.drawable.playbtn);
                                    progress.setVisibility(View.INVISIBLE);
                                    istask=false;
                                }
                            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(@NonNull FileDownloadTask.TaskSnapshot snapshot) {
                                    playbtn.setClickable(false);
                                    playbtn.setImageResource(R.drawable.playbtn);
                                    progress.setVisibility(View.VISIBLE);
                                    // Toast.makeText(textofsongwindoww.this, String.valueOf(snapshot.getBytesTransferred()), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        }
                        else if(player.isPlaying()){
                            playbtn.setImageResource(R.drawable.playbtn);
                            player.pause();
                        }
                }

                });

                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.y = 2100;

                dialog.getWindow().setAttributes(params);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00E5E5E5")));
                dialog.show();
                return false;
            }
        });

    }

    public int searchchords(int i, char[] a){                                                           // главная функция транспонирования, тут определяется длинна аккорда, потом ее замена по
        if (a[i]!=35 && a[i]!=55) {                                                                     // транспонированию и возврат в гланвыхй цикл.
            String temp = String.valueOf(a[i]);
            if (a[i + 1] >= 65 && a[i + 1] <= 122 && a[i + 1] != 105 && a[i + 1] != 98 && a[i + 1] != 73 && a[i + 1] != 114 || a[i + 1] == 35 || a[i + 1] == 55) {
                temp += String.valueOf(a[i + 1]);
                if (a[i + 2] >= 65 && a[i + 2] <= 122 && a[i + 2] != 105 && a[i + 2] != 98 && a[i + 2] != 73 && a[i + 2] != 114 || a[i + 2] == 35 || a[i + 2] == 55) {
                    temp += String.valueOf(a[i + 2]);
                    int number = majorchords.indexOf(temp);
                    if(number!=-1){
                   //    Log.d("MyLog", temp + " " + number);

                        int x = number+tonalnost;
                        if (x<0)
                            x=12+x;
                        if (x>11)
                            x=x-12;
                      //  Log.d("MyLog", " После изменения: " + majorchords.get(x));
                        tempchord=majorchords.get(x);
                        return (3);
                    } else{
                        number=minorchords.indexOf(temp);
                        if(number!=-1) {
                     //       Log.d("MyLog", temp + " " + number);

                            int x = number + tonalnost;
                            if (x < 0)
                                x = 12 + x;
                            if (x > 11)
                                x = x - 12;
                            //    Log.d("MyLog", " После изменения: " + minorchords.get(x));
                            tempchord = minorchords.get(x);
                            return (3);
                        } else {
                            number=majorchords7.indexOf(temp);
                            if(number!=-1) {
                     //           Log.d("MyLog", temp + " " + number);

                                int x = number + tonalnost;
                                if (x < 0)
                                    x = 12 + x;
                                if (x > 11)
                                    x = x - 12;
                                //    Log.d("MyLog", " После изменения: " + minorchords.get(x));
                                tempchord = majorchords7.get(x);
                                return (3);
                            } else{
                                number=minorchords7.indexOf(temp);
                     //           Log.d("MyLog", temp + " " + number);

                                int x = number + tonalnost;
                                if (x < 0)
                                    x = 12 + x;
                                if (x > 11)
                                    x = x - 12;
                                //    Log.d("MyLog", " После изменения: " + minorchords.get(x));
                                tempchord = minorchords7.get(x);
                                return (3);
                            }
                        }
                    }
                } else {
                    int number = majorchords.indexOf(temp);
                    if(number!=-1){
              //     Log.d("MyLog", temp + " " + number);

                        int x = number+tonalnost;
                    if (x<0)
                        x=12+x;
                    if (x>11)
                            x=x-12;
                 //   Log.d("MyLog", " После изменения: " + majorchords.get(x));
                        tempchord=majorchords.get(x);
                        return (2);
                    }
                    else{
                        number=minorchords.indexOf(temp);
                        if(number!=-1) {
                //            Log.d("MyLog", temp + " " + number);

                            int x = number + tonalnost;
                            if (x < 0)
                                x = 12 + x;
                            if (x > 11)
                                x = x - 12;
                            //      Log.d("MyLog", " После изменения: " + minorchords.get(x));
                            tempchord = minorchords.get(x);
                            return (2);
                        } else {
                            number=majorchords7.indexOf(temp);
                            if(number!=-1) {
                 //               Log.d("MyLog", temp + " " + number);

                                int x = number + tonalnost;
                                if (x < 0)
                                    x = 12 + x;
                                if (x > 11)
                                    x = x - 12;
                                //      Log.d("MyLog", " После изменения: " + minorchords.get(x));
                                tempchord = majorchords7.get(x);
                                return (2);
                            }else{
                                number=minorchords7.indexOf(temp);
                    //            Log.d("MyLog", temp + " " + number);

                                int x = number + tonalnost;
                                if (x < 0)
                                    x = 12 + x;
                                if (x > 11)
                                    x = x - 12;
                                //      Log.d("MyLog", " После изменения: " + minorchords.get(x));
                                tempchord = minorchords7.get(x);
                                return (2);
                            }
                        }
                    }
                }
            } else {
                int number = majorchords.indexOf(temp);
                if(number!=-1){
            //    Log.d("MyLog", temp + " " + number);

                    int x = number+tonalnost;
                    if (x<0)
                        x=12+x;
                    if (x>11)
                        x=x-12;
             //       Log.d("MyLog", " После изменения: " + majorchords.get(x));
                    tempchord=majorchords.get(x);
                    return (1);
                }
                else{
                    number=minorchords.indexOf(temp);
                    if(number!=-1) {
             //           Log.d("MyLog", temp + " " + number);

                        int x = number + tonalnost;
                        if (x < 0)
                            x = 12 + x;
                        if (x > 11)
                            x = x - 12;
                        //      Log.d("MyLog", " После изменения: " + minorchords.get(x));
                        tempchord = minorchords.get(x);
                        return (1);
                    } else{
                        number=majorchords7.indexOf(temp);
                        if(number!=-1) {
               //             Log.d("MyLog", temp + " " + number);

                            int x = number + tonalnost;
                            if (x < 0)
                                x = 12 + x;
                            if (x > 11)
                                x = x - 12;
                            //      Log.d("MyLog", " После изменения: " + minorchords.get(x));
                            tempchord = majorchords7.get(x);
                            return (1);
                        } else {
                            number=minorchords7.indexOf(temp);
                //            Log.d("MyLog", temp + " " + number);

                            int x = number + tonalnost;
                            if (x < 0)
                                x = 12 + x;
                            if (x > 11)
                                x = x - 12;
                            //      Log.d("MyLog", " После изменения: " + minorchords.get(x));
                            tempchord = minorchords7.get(x);
                            return (1);
                        }
                    }
                }
            }

        } else{
            return 0;
        }

    }


    @Override
    public void onItemClick(View view, int position) {

        changesogs(position);


    }
    public void changesogs(int position){
        name=namesofsongsfromplaylist.get(position);

        AdapterForRec adapter = new AdapterForRec(this, namesofsongsfromplaylist, name);
        adapter.setClickListener(textofsongwindoww.this);
        songs.setAdapter(adapter);
        songs.scrollToPosition(position);
        for(int i =0; i< namesofsongs.length; i++) {
            if (namesofsongs[i].equals( name)) {
                textt=texts[i];
            }
        }
        nametext.setText(name);
        Intent intent = getIntent();
        extra=intent.getStringExtra("extra");

        char[] a = textt.toCharArray();
        String texttt = "";
        String checkshowchods = "1";
        color = "fa0000";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput("options")));   //читаем файл настроек

            String src;
            int i = 0;
            while ((src = br.readLine()) != null) {
                switch (i) {
                    case 0:                      //на первой строчке - аккорды
                        checkshowchods = src;
                        break;
                    case 1:                      // на второй - цвет
                        color = src;
                        break;
                    case 2:
                        tonalnost = Integer.parseInt(src); // третья - тональность
                        break;
                }
                i++;
            }

            br.close();
        } catch (FileNotFoundException e) {
            checkshowchods = "1";
            color = "fa0000";
            tonalnost = 0;
            e.printStackTrace();
        } catch (IOException e) {
            checkshowchods = "1";
            color = "fa0000";
            tonalnost = 0;
            e.printStackTrace();
        }
        showchords = Integer.parseInt(checkshowchods);
        for (int i = 0; i < a.length; i++) {
            if (a[i] >= 65 && a[i] <= 122 && a[i] != 105 && a[i] != 98 && a[i] != 73 && a[i] != 114 || a[i] == 35 || a[i] == 55) {  //выделяем аккорды красным цветом
                if (showchords == 1) {
                    char temp = a[i];
                    String red = new String(String.valueOf(temp));
                    i += searchchords(i, a);
                    red = tempchord;
                    red = "<font color=#" + color + ">" + red + "</font> <";
                    texttt = texttt + red;
                }

            } else {
                char temp = a[i];
                String usuall = new String(String.valueOf(temp));
                texttt = texttt + usuall;
            }
        }
        if(name.equals("Хвала на вышынях Богу(G)")){
            String texttemp1 = "";
            char[] temp1 = gloriabegin.toCharArray();
            for (int i = 0; i < temp1.length; i++) {
                if (temp1[i] >= 65 && temp1[i] <= 122 && temp1[i] != 105 && temp1[i] != 98 && temp1[i] != 73 && temp1[i] != 114 || temp1[i] == 35 || temp1[i] == 55) {  //выделяем аккорды красным цветом
                    if (showchords == 1) {
                        char temp = temp1[i];
                        String red = new String(String.valueOf(temp));
                        i += searchchords(i, temp1);
                        red = tempchord;
                        red = "<font color=#" + color + ">" + red + "</font> ";
                        texttemp1 = texttemp1 + red;
                    }

                } else {
                    char temp = temp1[i];
                    String usuall = new String(String.valueOf(temp));
                    texttemp1 = texttemp1 + usuall;
                }
            }

            text.setText(Html.fromHtml(texttemp1+  gloria + texttt));

        }
        else {
            if(extra.equals("Слухай")){


                text.setText(Html.fromHtml( texttt + sluhay));

            }
            else {

                text.setText(Html.fromHtml(texttt));

            }


        }
        text.setMovementMethod(new ScrollingMovementMethod());
    }
    @Override
    public void onBackPressed() {
        if(task!=null)
       task.cancel();
        istask=false;
        if(player!=null)
       player.stop();
        super.onBackPressed();
    }

    public static boolean hasConnection(final Context context)
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        return false;
    }
}



