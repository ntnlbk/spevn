package com.LibBib.spevn;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlaylistActivity extends AppCompatActivity implements InterfacePlaylist {



    private ListView playlistlist;

    private ImageButton addplaylist, backbtn;
    private AdapterForPlaylist adapter;
    ArrayList<String> namesofplaylists = new ArrayList<String>();

    @Override
    protected void onResume() {


        super.onResume();
    }

    Map<String, Integer> chislo = new HashMap<String, Integer>();


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PlaylistActivity.this, MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);


        playlistlist=(ListView)findViewById(R.id.playlistlist);
        addplaylist=(ImageButton)findViewById(R.id.addplaylistbtn);
        backbtn=(ImageButton)findViewById(R.id.backbtnplaylist);

        namesofplaylists.clear();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);
        Intent intent = getIntent();
        String type="", name ="";
        if(intent.getStringExtra("type") != null){
            type = intent.getStringExtra("type");
            name = intent.getStringExtra("name");
        }
        if(type.equals("delete")){
            readingplaylists();
            delete(name);
        }

        if(type.equals("redact"))
            redact(name);
        readingplaylists();



         addplaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addplaylist();
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }



    public void readingplaylists(){

        namesofplaylists.clear();

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
        String[] temp =  namesofplaylists.toArray(new String[0]);

         adapter = new AdapterForPlaylist  (this, temp, temp,chislo, false, new String[]{});
        playlistlist.setAdapter(adapter);
    }
    public void addplaylist(){
        LayoutInflater li = LayoutInflater.from(PlaylistActivity.this);
        View promptsView = li.inflate(R.layout.dialog, null);
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(PlaylistActivity.this);
        mDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView.findViewById(R.id.input_text);
        final Button okbtn = (Button)promptsView.findViewById(R.id.savebtn);
        final Button closebtn = (Button)promptsView.findViewById(R.id.closebtndialog);
        final AlertDialog alertDialog = mDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String name = userInput.getText().toString();
                    if(name.equals("Избранное") || name.equals("") || name.equals("Антон лох") || name.equals("Яна лох") || namesofplaylists.contains(name))
                        Toast.makeText(PlaylistActivity.this, "Такое имя недоступно", Toast.LENGTH_LONG).show();
                    else {
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(openFileOutput("namesofplaylists", MODE_APPEND)));
                        bw.write(name);
                        bw.newLine();
                        bw.close();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                alertDialog.dismiss();
                readingplaylists();
            }
        });

        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        //Настраиваем сообщение в диалоговом окне:
        mDialogBuilder
                .setCancelable(false);



        //Создаем AlertDialog:


        //и отображаем его:
        alertDialog.show();
    }

    @Override
    public void delete(String name) {
        if (name.equals("Избранное")) {
            Toast.makeText(PlaylistActivity.this, "Удалить этот плейлист невозможно", Toast.LENGTH_LONG).show();
        } else {
            namesofplaylists.remove(name);
            try {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(openFileOutput("namesofplaylists", MODE_PRIVATE)));
                for (int i = 0; i < namesofplaylists.size(); i++) {
                    String temp = namesofplaylists.get(i);
                    bw.write(temp);
                    bw.newLine();

                }
                bw.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(openFileOutput(name, MODE_PRIVATE)));
                bw.write("");
                bw.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            readingplaylists();
        }
    }

    @Override
    public void openplaylist(String name) {
        Intent intent = new Intent(PlaylistActivity.this, PlaylistSongsActivity.class);
        intent.putExtra("playlist", name);
        startActivity(intent);
        finish();
    }

    @Override
    public void redact(final String name) {
        if (name.equals("Избранное")) {
            Toast.makeText(PlaylistActivity.this, "Переименовать этот плейлист невозможно", Toast.LENGTH_LONG).show();
        } else {
            LayoutInflater li = LayoutInflater.from(PlaylistActivity.this);
            View promptsView = li.inflate(R.layout.dialog, null);
            AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(PlaylistActivity.this, R.style.DialogStyle);
            mDialogBuilder.setView(promptsView);
            final EditText userInput = (EditText) promptsView.findViewById(R.id.input_text);
            userInput.setHint("Введите новое название");
            final TextView text = (TextView) promptsView.findViewById(R.id.tv);
            text.setText("Измените название");
            final Button okbtn = (Button) promptsView.findViewById(R.id.savebtn);
            final Button closebtn = (Button) promptsView.findViewById(R.id.closebtndialog);
            final AlertDialog alertDialog = mDialogBuilder.create();
            //alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            okbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String newname = userInput.getText().toString();
                    if (newname.equals("Избранное") || newname.equals("") || newname.equals("Антон лох") || newname.equals("Яна лох") || namesofplaylists.contains(newname))
                        Toast.makeText(PlaylistActivity.this, "Такое имя недоступно", Toast.LENGTH_LONG).show();
                    else {
                        namesofplaylists.set(namesofplaylists.indexOf(name), newname);
                        ArrayList<String> songs = new ArrayList<>();
                        try {
                            BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(name)));
                            String src="";

                            while ((src = br.readLine()) != null){
                                songs.add(src);
                            }
                            br.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            BufferedWriter bw1 = new BufferedWriter(new OutputStreamWriter(openFileOutput(name, MODE_PRIVATE)));
                            bw1.write("");
                            bw1.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            BufferedWriter bw2 = new BufferedWriter(new OutputStreamWriter(openFileOutput(newname, MODE_PRIVATE)));
                            for (int i =0; i <songs.size(); i++){
                                bw2.write(songs.get(i));
                                bw2.newLine();
                            }
                            bw2.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(openFileOutput("namesofplaylists", MODE_PRIVATE)));
                            for (int i = 0; i < namesofplaylists.size(); i++) {
                                String temp = namesofplaylists.get(i);
                                bw.write(temp);
                                bw.newLine();

                            }
                            bw.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        alertDialog.dismiss();

                        readingplaylists();
                    }
                }
            });

            closebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            //Настраиваем сообщение в диалоговом окне:
            mDialogBuilder
                    .setCancelable(false);


            //Создаем AlertDialog:


            //и отображаем его:
            alertDialog.show();

        }
    }

}