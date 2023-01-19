package com.LibBib.spevn;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlaylistSongsActivity extends AppCompatActivity implements MyIntefrace, PlaylistSongs {

    private String name;
    private TextView nametext, nosongstext;
    private ListView songs;
    private ProgressBar progressBar;
    ArrayList<String> namesofsongs = new ArrayList<String>();
    private ImageButton back, more;
    private Button addsongs;
    String[] temp2;
    Dialog maindialog;
    Intent intent2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_songs);
        Intent intent = getIntent();
        name = intent.getStringExtra("playlist");
        back=(ImageButton)findViewById(R.id.backbtnsongsplaylist);
        nametext=(TextView)findViewById(R.id.name);
        songs=(ListView)findViewById(R.id.songs);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);
        more=(ImageButton)findViewById(R.id.moresongsplaylist);
        nosongstext=(TextView)findViewById(R.id.nosongstext);
        addsongs=(Button)findViewById(R.id.button);
        nametext.setText(name);
        intent2 = new Intent(PlaylistSongsActivity.this, PlaylistActivity.class);
        addsongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlaylistSongsActivity.this, MainActivity.class);
                intent.putExtra("playlistsong", name);
                startActivity(intent);

            }
        });

        readsongs(); 
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    @Override
    public void onBackPressed() {
        startActivity(intent2);
        finish();
    }

    public void readsongs(){
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        namesofsongs.clear();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(name)));
            String src="";
            while ((src = br.readLine()) != null){
                namesofsongs.add(src);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }
        if (namesofsongs.size()==0){
            addsongs.setVisibility(Button.VISIBLE);
            nosongstext.setVisibility(TextView.VISIBLE);
            songs.setVisibility(ListView.INVISIBLE);
        }else {
            addsongs.setVisibility(Button.INVISIBLE);
            nosongstext.setVisibility(TextView.INVISIBLE);
            songs.setVisibility(ListView.VISIBLE);
            String[] temp = namesofsongs.toArray(new String[0]);
            String [] namesofsongs2 = getResources().getStringArray(R.array.names);
            String [] texts = getResources().getStringArray(R.array.texts);
            String[] types = getResources().getStringArray(R.array.typesofsongs);
            Map<String, String> typesofsongs = new HashMap<>();
            typesofsongs.clear();
            for(int i = 0; i < namesofsongs2.length; i++){
                typesofsongs.put(namesofsongs2[i], types[i]);
            }
            final MyAdapter adapter = new MyAdapter(this, temp, temp, "Playlist", false, new ArrayList<String>(), typesofsongs, true, name,false);
            songs.setAdapter(adapter);
            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(adapter.getLongtap())
                    adapter.dialog(222222);
                    else{
                        LayoutInflater inflater =(LayoutInflater)PlaylistSongsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View promptsView = inflater.inflate(R.layout.moredialogplaylistinsongs, null);
                        final AlertDialog.Builder builder = new AlertDialog.Builder(PlaylistSongsActivity.this, R.style.DialogStyle);
                        builder.setView(promptsView);

                        final Button delete = (Button) promptsView.findViewById(R.id.delete);
                        Button redact = (Button) promptsView.findViewById(R.id.redact);
                        Button addsongs = (Button) promptsView.findViewById(R.id.addsongs);

                        final Dialog dialog = builder.create();
                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                intent2.putExtra("type", "delete");
                                intent2.putExtra("name", name);


                                dialog.dismiss();
                                onBackPressed();


                            }
                        });
                        redact.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                intent2.putExtra("type", "redact");
                                intent2.putExtra("name", name);


                                dialog.dismiss();
                                onBackPressed();


                            }
                        });
                        addsongs.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                Intent intent = new Intent(PlaylistSongsActivity.this, MainActivity.class);
                                intent.putExtra("playlistsong", name);
                                startActivity(intent);

                            }
                        });




                        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                        params.y = 2100;

                        dialog.getWindow().setAttributes(params);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00E5E5E5")));
                        dialog.show();
                    }
                }
            });

        }

    }

    @Override
    public void mymethod() {

    }

    @Override
    public void mymethoddestroy() {

    }

    @Override
    public void progressbar() {
        progressBar.setVisibility(ProgressBar.VISIBLE);
    }

    @Override
    public void updatetextchecked(int i) {

    }

    @Override
    public void addtoizbr(ArrayList<String> positions) {

    }

    @Override
    public void addtoplalist(ArrayList<String> namesofsongstoadd) {
        temp2 =  namesofsongstoadd.toArray(new String[0]);
        LayoutInflater inflater = (LayoutInflater)PlaylistSongsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View promptsView = inflater.inflate(R.layout.addtoplaylistdialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(PlaylistSongsActivity.this, R.style.DialogStyle);
        builder.setView(promptsView);

        final ListView playlists = (ListView)promptsView.findViewById(R.id.playlists);
        TextView close = (TextView) promptsView.findViewById(R.id.closetextbtn);
        Button addnewplaylist = (Button)promptsView.findViewById(R.id.addnewplaylist);
        final ArrayList<String> namesofplaylists = new ArrayList<String>();
        final Map<String, Integer> chislo = new HashMap<String, Integer>();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput("namesofplaylists")));
            String src= "";
            while ((src = br.readLine()) != null){
                namesofplaylists.add(src);
            }
            if (namesofplaylists.contains("Избранное")==false)
                namesofplaylists.add(0, "Избранное");
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            namesofplaylists.add(0, "Избранное");
        } catch (IOException e) {
            e.printStackTrace();
            namesofplaylists.add(0, "Избранное");
        }

        for(int i =0; i<namesofplaylists.size(); i++){
            String name = namesofplaylists.get(i);
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(name)));
                String src= "";
                int temp =0;
                while ((src = br.readLine()) != null){
                    temp++;
                }
                chislo.put(name, temp);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                chislo.put(name, 0);
            } catch (IOException e) {
                e.printStackTrace();
                chislo.put(name, 0);
            }
        }


        if(namesofplaylists.size()>5){
            ViewGroup.LayoutParams params = playlists.getLayoutParams();
            float dip = 230f;
            Resources r = getResources();
            float px = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dip,
                    r.getDisplayMetrics()
            );
            params.height =(int)px;
            playlists.setLayoutParams(params);
            playlists.requestLayout();
        }
        String[] temp =  namesofplaylists.toArray(new String[0]);
        final Dialog dialog = builder.create();
        maindialog=dialog;
        AdapterForPlaylist adapter = new AdapterForPlaylist  (this, temp, temp,chislo, true, temp2);
        playlists.setAdapter(adapter);



        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        addnewplaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(PlaylistSongsActivity.this);
                View promptsView = li.inflate(R.layout.dialog, null);
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(PlaylistSongsActivity.this);
                mDialogBuilder.setView(promptsView);
                final EditText userInput = (EditText) promptsView.findViewById(R.id.input_text);
                final Button okbtn = (Button)promptsView.findViewById(R.id.savebtn);
                final Button closebtn = (Button)promptsView.findViewById(R.id.closebtndialog);
                final AlertDialog alertDialog = mDialogBuilder.create();
                okbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            String name = userInput.getText().toString();
                            if(name.equals("Избранное") || name.equals("")  || namesofplaylists.contains(name))
                                Toast.makeText(PlaylistSongsActivity.this, getString(R.string.incorrect_name), Toast.LENGTH_LONG).show();
                            else {
                                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(openFileOutput("namesofplaylists", MODE_APPEND)));
                                bw.write(name);
                                bw.newLine();
                                bw.close();
                                namesofplaylists.add(name);
                                chislo.put(name, 0);
                                String[] temp =  namesofplaylists.toArray(new String[0]);
                                AdapterForPlaylist adapter = new AdapterForPlaylist  (PlaylistSongsActivity.this, temp, temp,chislo, true, temp2);
                                playlists.setAdapter(adapter);
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        alertDialog.dismiss();



                    }
                });

                closebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                mDialogBuilder
                        .setCancelable(false);

                alertDialog.show();
            }
        });




        dialog.show();
    }

    @Override
    public void addsongstoplaylist(String[] namesofsongstoadd, String nameofplaylist) {
        maindialog.dismiss();
        ArrayList<String> allready = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(nameofplaylist))); // проверяем наличие песен в плейлисте
            String src="";
            while ((src = br.readLine()) != null){
                allready.add(src);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(openFileOutput(nameofplaylist, MODE_APPEND)));
            for(int i =0; i< namesofsongstoadd.length; i++){
                if(allready.contains(namesofsongstoadd[i])==false){
                bw.write(namesofsongstoadd[i]);
                bw.newLine();}
                else{
                    Toast.makeText(PlaylistSongsActivity.this, getString(R.string.song) + namesofsongstoadd[i] + getString(R.string.already_in_fav), Toast.LENGTH_SHORT).show();
                }
            }
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void audiochoose(String name) {

    }

    @Override
    protected void onResume() {
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        readsongs();
        super.onResume();
    }

    @Override
    public void delete(ArrayList<String> positions) {
        for(int i =0; i<positions.size() ; i++){
            String temp = positions.get(i);

            namesofsongs.remove(temp);}
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(openFileOutput(name, MODE_PRIVATE)));
            for(int i = 0; i< namesofsongs.size();i++){
                bw.write(namesofsongs.get(i));
                bw.newLine();
            }
            bw.close();
            readsongs();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}