package com.LibBib.spevn;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class MainActivity extends AppCompatActivity implements MyIntefrace {


    private int C2 ;
    private  int C4;
    private String[] namesofsongs, texts, temp2, types, nameofsongsdin;
    private ListView listt;
    private TextView allsongs, xsongs, shortsongs, longsongs, maintextbtn, playlisttextbtn, aboutustextbtn, optionstextbtn, howmanycheckedtxt, randomsongbtn, addsongbtn, helpbtn;
    private EditText searchtext;
    private Boolean audiochoose = false;
    private Boolean longclick = false;
    private AdView mAdView;
    private ImageButton menubtn, acceptbtn;
    private DrawerLayout drawer;
    private ArrayList<String> listItems;
    private MyAdapter adapter2;
    private ProgressBar progressBar;
    private RelativeLayout layout;
    private Boolean menutopopen = false;
    private ImageButton close, morebtnchecked;
    private String playlistsongs = "sffdsf", filter = "";
    private ArrayList<String> filtry = new ArrayList<>();
    private Dialog maindialog;
    private Map<String, String> typesofsongs = new HashMap<>();
    private int filtr = 0;
    private SharedPreferences prefs;
    private static final int VERSION_CODE_FIREBASE = 11;


    @Override
    public void mymethod() {
        close.setVisibility(View.VISIBLE);
        morebtnchecked.setVisibility(View.VISIBLE);

        menutopopen = true;
        longclick = true;
        DrawerLayout.LayoutParams lp = new DrawerLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        float dip = 66f;
        Resources r = getResources();
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dip,
                r.getDisplayMetrics()
        );
        lp.setMargins(0, (int) px, 0, 0);
        layout.setLayoutParams(lp);
    }

    @Override
    public void mymethoddestroy() {
        close.setVisibility(View.INVISIBLE);
        morebtnchecked.setVisibility(View.INVISIBLE);
        acceptbtn.setVisibility(View.INVISIBLE);
        DrawerLayout.LayoutParams lp = new DrawerLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        longclick = false;
        lp.topMargin = 0;
        layout.setLayoutParams(lp);
        menutopopen = false;
    }

    @Override
    public void progressbar() {
        progressBar.setVisibility(ProgressBar.VISIBLE);
        // finish();
    }

    @Override
    public void updatetextchecked(int i) {
        howmanycheckedtxt.setText(String.valueOf(i) + " " + getResources().getString(R.string.checked));
    }

    @Override
    public void addtoizbr(ArrayList<String> positions) {
        ArrayList<String> allready = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(getString(R.string.favourite)))); // проверяем наличие песен в плейлисте
            String src = "";
            while ((src = br.readLine()) != null) {
                allready.add(src);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(openFileOutput(getString(R.string.favourite), MODE_APPEND)));
            for (int i = 0; i < positions.size(); i++) {
                if (allready.contains(positions.get(i)) == false) {
                    bw.write(positions.get(i));
                    bw.newLine();
                } else {
                    Toast.makeText(MainActivity.this,  getResources().getString(R.string.song) + positions.get(i) + getResources().getString(R.string.already_in_fav), Toast.LENGTH_SHORT).show();
                }

            }
            bw.close();
            if (playlistsongs.equals("sadfsadf") == false || longclick)
                onBackPressed();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addtoplalist(final ArrayList<String> namesofsongstoadd) {
        temp2 = namesofsongstoadd.toArray(new String[0]);
        LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View promptsView = inflater.inflate(R.layout.addtoplaylistdialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.DialogStyle);
        builder.setView(promptsView);

        final ListView playlists = (ListView) promptsView.findViewById(R.id.playlists);
        TextView close = (TextView) promptsView.findViewById(R.id.closetextbtn);
        Button addnewplaylist = (Button) promptsView.findViewById(R.id.addnewplaylist);
        final ArrayList<String> namesofplaylists = new ArrayList<String>();
        final Map<String, Integer> chislo = new HashMap<String, Integer>();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput("namesofplaylists")));
            String src = "";
            while ((src = br.readLine()) != null) {
                namesofplaylists.add(src);
            }
            if (namesofplaylists.contains(getString(R.string.favourite)) == false)
                namesofplaylists.add(0, getString(R.string.favourite));
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            namesofplaylists.add(0, getString(R.string.favourite));
        } catch (IOException e) {
            e.printStackTrace();
            namesofplaylists.add(0, getString(R.string.favourite));
        }

        for (int i = 0; i < namesofplaylists.size(); i++) {
            String name = namesofplaylists.get(i);
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(name)));
                String src = "";
                int temp = 0;
                while ((src = br.readLine()) != null) {
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


        if (namesofplaylists.size() > 5) {
            ViewGroup.LayoutParams params = playlists.getLayoutParams();
            float dip = 230f;
            Resources r = getResources();
            float px = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dip,
                    r.getDisplayMetrics()
            );
            params.height = (int) px;
            playlists.setLayoutParams(params);
            playlists.requestLayout();
        }
        String[] temp = namesofplaylists.toArray(new String[0]);
        final Dialog CustomDialog = builder.create();
        maindialog = CustomDialog;
        AdapterForPlaylist adapter = new AdapterForPlaylist(this, temp, temp, chislo, true, temp2);
        playlists.setAdapter(adapter);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog.dismiss();
            }
        });
        addnewplaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(MainActivity.this);
                View promptsView = li.inflate(R.layout.dialog, null);
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                mDialogBuilder.setView(promptsView);
                final EditText userInput = (EditText) promptsView.findViewById(R.id.input_text);
                final Button okbtn = (Button) promptsView.findViewById(R.id.savebtn);
                final Button closebtn = (Button) promptsView.findViewById(R.id.closebtndialog);
                final AlertDialog alertDialog = mDialogBuilder.create();
                okbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            String name = userInput.getText().toString();
                            if (name.equals(getString(R.string.favourite)) || name.equals("") || namesofplaylists.contains(name))
                                Toast.makeText(MainActivity.this, getResources().getString(R.string.incorrect_name), Toast.LENGTH_LONG).show();
                            else {
                                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(openFileOutput("namesofplaylists", MODE_APPEND)));
                                bw.write(name);
                                bw.newLine();
                                bw.close();
                                namesofplaylists.add(name);
                                chislo.put(name, 0);
                                String[] temp = namesofplaylists.toArray(new String[0]);
                                AdapterForPlaylist adapter = new AdapterForPlaylist(MainActivity.this, temp, temp, chislo, true, temp2);
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


        CustomDialog.show();
    }

    @Override
    public void addsongstoplaylist(String[] namesofsongstoadd, String nameofplaylist) {
        maindialog.dismiss();
        ArrayList<String> allready = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(nameofplaylist))); // проверяем наличие песен в плейлисте
            String src = "";
            while ((src = br.readLine()) != null) {
                allready.add(src);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(openFileOutput(nameofplaylist, MODE_APPEND)));
            for (int i = 0; i < namesofsongstoadd.length; i++) {
                if (allready.contains(namesofsongstoadd[i]) == false) {
                    bw.write(namesofsongstoadd[i]);
                    bw.newLine();
                } else {
                    Toast.makeText(MainActivity.this,  getResources().getString(R.string.song) + namesofsongstoadd[i] + getResources().getString(R.string.already_in_fav), Toast.LENGTH_SHORT).show();

                }
            }
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (menutopopen) {
            onBackPressed();
        }
    }
    @Override
    public void audiochoose(String name) {
        Intent intent = new Intent();
        intent.putExtra("name", name);
        setResult(RESULT_OK, intent);
        finish();
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); //темная тема
         C2 = getResources().getColor(R.color.C2);
         C4 = getResources().getColor(R.color.C4);
        namesofsongs = getResources().getStringArray(R.array.names);
        nameofsongsdin = namesofsongs;
        listItems = new ArrayList<>(Arrays.asList(namesofsongs));
        texts = getResources().getStringArray(R.array.texts);
        types = getResources().getStringArray(R.array.typesofsongs);
        typesofsongs.clear();
        for (int i = 0; i < namesofsongs.length; i++) {
            typesofsongs.put(namesofsongs[i], types[i]);
        }
        Resources resourses = getResources();
        listt = (ListView) findViewById(R.id.namess);
        searchtext = (EditText) findViewById(R.id.editText);
        allsongs = (TextView) findViewById(R.id.allsongs);
        xsongs = (TextView) findViewById(R.id.xsongs);
        shortsongs = (TextView) findViewById(R.id.shortsongs);
        longsongs = (TextView) findViewById(R.id.longsongs);
        close = (ImageButton) findViewById(R.id.closebtn);
        maintextbtn = (TextView) findViewById(R.id.maintextbtn);
        playlisttextbtn = (TextView) findViewById(R.id.playlisttextbtn);
        addsongbtn = (TextView) findViewById(R.id.addsong);
        aboutustextbtn = (TextView) findViewById(R.id.aboutustextbtn);
        optionstextbtn = (TextView) findViewById(R.id.optionstextbtn);
        layout = (RelativeLayout) findViewById(R.id.relative);
        progressBar = (ProgressBar) findViewById(R.id.progressBar3);
        acceptbtn = (ImageButton) findViewById(R.id.acceptbtn);
        howmanycheckedtxt = (TextView) findViewById(R.id.howmanychecked);
        morebtnchecked = (ImageButton) findViewById(R.id.morebtnchecked);
        randomsongbtn = (TextView) findViewById(R.id.randomsong);
        helpbtn = (TextView) findViewById(R.id.helpbtn);
        mAdView = findViewById(R.id.adView);
        drawer = (DrawerLayout) findViewById(R.id.relative_layout);
        menubtn = (ImageButton) findViewById(R.id.menubtn);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        Intent intent = getIntent();
        close.setVisibility(View.INVISIBLE);
        morebtnchecked.setVisibility(View.INVISIBLE);
        acceptbtn.setVisibility(View.INVISIBLE);
        filtry.add(getString(R.string.all));
        filtry.add(getString(R.string.parts));
        filtry.add(getString(R.string.short_songs));
        filtry.add(getString(R.string.long_songs));
        int darktheme =0;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput("options"))); //считываение настроек
            int i=0;

            String src="";
            while ((src = br.readLine()) != null){
                switch (i) {
                    case 4:
                        darktheme=Integer.parseInt(src);
                        break;
                }
                i++;
            }

        } catch (FileNotFoundException e) {
            darktheme=0;
            e.printStackTrace();
        } catch (IOException e) {
            darktheme=0;
            e.printStackTrace();
        }
        if (darktheme==1)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); //темная тема
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        if (intent.getStringExtra("playlistsong") != null) {
            playlistsongs = intent.getStringExtra("playlistsong");
            mymethod();
            howmanycheckedtxt.setVisibility(View.VISIBLE);
            acceptbtn.setVisibility(ImageButton.VISIBLE);
            morebtnchecked.setVisibility(ImageButton.INVISIBLE);
            acceptbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (playlistsongs.equals(getString(R.string.favourite))) {
                        addtoizbr(adapter2.getCheckedpos());
                        onBackPressed();
                    } else {
                        ArrayList<String> allready = new ArrayList<>();
                        try {
                            BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(playlistsongs))); // проверяем наличие песен в плейлисте
                            String src = "";
                            while ((src = br.readLine()) != null) {
                                allready.add(src);
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(openFileOutput(playlistsongs, MODE_APPEND)));
                            ArrayList<String> songs = adapter2.getCheckedpos();
                            for (int i = 0; i < songs.size(); i++) {
                                if (allready.contains(songs.get(i)) == false) {
                                    bw.write(songs.get(i));
                                    bw.newLine();
                                } else {
                                    Toast.makeText(MainActivity.this,  getResources().getString(R.string.song) + songs.get(i) + getResources().getString(R.string.already_in_fav), Toast.LENGTH_SHORT).show();

                                }
                            }
                            bw.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        onBackPressed();
                    }
                }
            });
        } else if (intent.getStringExtra("audiochoose") != null) {
            mymethod();
            morebtnchecked.setVisibility(View.INVISIBLE);
            howmanycheckedtxt.setVisibility(View.INVISIBLE);
            longclick = false;
            menutopopen = false;
            audiochoose = true;
        } else
            playlistsongs = "sadfsadf";
        adapter2 = new MyAdapter(this, nameofsongsdin, namesofsongs, "Main", longclick, new ArrayList<String>(), typesofsongs, false, "noplaylist", audiochoose);
        listt.setAdapter(adapter2);
        searchtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filter = charSequence.toString();
                ArrayList<String> temp = new ArrayList<String>();
                ArrayList<String> temp2 = adapter2.getCheckedpos();
                for (int t = 0; t < nameofsongsdin.length; t++) {
                    if (nameofsongsdin[t].toLowerCase().contains(charSequence.toString().toLowerCase()))
                        temp.add(nameofsongsdin[t]);
                }
                adapter2 = new MyAdapter(MainActivity.this, temp.toArray(new String[0]), namesofsongs, "Main", longclick, temp2, typesofsongs, false, "noplaylist", audiochoose);
                listt.setAdapter(adapter2);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        List<String> testDeviceIds = Arrays.asList("6318028ABE795F15CFBC0D366196F8A9", AdRequest.DEVICE_ID_EMULATOR, "4A6C605A724A6F45710C372E31DD989E");
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }
            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
        menubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        maintextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        playlisttextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PlaylistActivity.class);
                startActivity(intent);
                finish();
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        aboutustextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
                startActivity(intent);
            }
        });
        addsongbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, addsongactivity.class);
                startActivity(intent);
            }
        });
        helpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(intent);
            }
        });
        optionstextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, OptionsActivity.class);
                intent.putExtra("имя", "none");
                intent.putExtra("isfromplaylist", "false");
                startActivity(intent);
                finish();

            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playlistsongs.equals("sadfsadf") == false)
                    onBackPressed();
                onBackPressed();
            }
        });
        morebtnchecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter2.dialog(0);
            }
        });
        randomsongbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                progressbar();
                String b = namesofsongs[(int) (Math.random() * namesofsongs.length)];

                Intent intent = new Intent(MainActivity.this, textofsongwindoww.class);
                intent.putExtra("имя", b);
                intent.putExtra("isfromplaylist", "false");
                if (b.equals("Хвала на вышынях Богу(G)"))
                    intent.putExtra("extra", "Gloria");
                else
                    intent.putExtra("extra", "none");
                startActivity(intent);
            }
        });
        //проверяем наличие обновы
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("update");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = String.valueOf(dataSnapshot.getValue());

                // Log.d("MyLog", "Value is: " + value);
                if (!value.equals(String.valueOf(VERSION_CODE_FIREBASE))) {
                    String title = resourses.getString(R.string.update_is_available);
                    String message = resourses.getString(R.string.please_update);
                    String button1String = resourses.getString(R.string.update);
                    String button2String = resourses.getString(R.string.skip_update);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle(title);  // заголовок
                    builder.setMessage(message); // сообщение
                    builder.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.LibBib.spevn"));
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    builder.setCancelable(false);
                    builder.create();
                    builder.show();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w("MyLog", "Failed to read value.", error.toException());
            }
        });
        //свайпы влево и вправо
        listt.setOnTouchListener(new View.OnTouchListener() {
            float x1 = Float.NaN, y1 = Float.NaN, x2 = Float.NaN, y2 = Float.NaN;
            static final int delta = 40;
            //int ItemPosition;
            boolean first = false;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.EDGE_LEFT:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!first) {
                            x1 = event.getX();
                            y1 = event.getY();
                            first = true;
                            return false;
                        }
                    case MotionEvent.ACTION_UP:
                        first = false;
                        x2 = event.getX();
                        y2 = event.getY();
                        int itemPosition = listt.pointToPosition((int) x2, (int) y2);

                        if (x2 - x1 > delta) {
                            filtr--;
                            if (filtr == -1)
                                filtr = 0;
                            else {
                                switch (filtr) {
                                    case 0:
                                        allsongs(new View(MainActivity.this));
                                        break;
                                    case 1:
                                        xsongs(new View(MainActivity.this));
                                        break;
                                    case 2:
                                        shortsongs(new View(MainActivity.this));
                                        break;
                                    case 3:
                                        longsongs(new View(MainActivity.this));
                                        break;
                                }
                            }
                        } else if (x1 - x2 > delta) {
                            filtr++;
                            if (filtr == 4)
                                filtr = 3;
                            else {
                                switch (filtr) {
                                    case 0:
                                        allsongs(new View(MainActivity.this));
                                        break;
                                    case 1:
                                        xsongs(new View(MainActivity.this));
                                        break;
                                    case 2:
                                        shortsongs(new View(MainActivity.this));
                                        break;
                                    case 3:
                                        longsongs(new View(MainActivity.this));
                                        break;
                                }
                            }
                        }
                    default:
                        return false;
                }
                return false;
            }
        });
        prefs = getSharedPreferences("com.LibBib.spevn", MODE_PRIVATE);
        if (prefs.getBoolean("firstrun", true)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = (LinearLayout) getLayoutInflater().inflate(R.layout.whatsnewdialog, null);
            builder.setView(view);
            TextView close = view.findViewById(R.id.textView37);
            Dialog dialog = builder.create();
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            prefs.edit().putBoolean("firstrun", false).apply();
        }
    }
    public void allsongs(View view) {
        filtr = 0;
        allsongs.setTextColor(C2);
        xsongs.setTextColor(C4);
        longsongs.setTextColor(C4);
        shortsongs.setTextColor(C4);
        nameofsongsdin = namesofsongs;
        ArrayList<String> temp = new ArrayList<String>();
        ArrayList<String> temp2 = adapter2.getCheckedpos();
        for (int t = 0; t < nameofsongsdin.length; t++) {
            if (nameofsongsdin[t].toLowerCase().contains(filter.toString().toLowerCase()))
                temp.add(nameofsongsdin[t]);
        }
        adapter2 = new MyAdapter(MainActivity.this, temp.toArray(new String[0]), namesofsongs, "Main", longclick, temp2, typesofsongs, false, "noplaylist", audiochoose);
        listt.setAdapter(adapter2);
    }
    public void xsongs(View view) {
        filtr = 1;
        nameofsongsdin = new String[]{};
        ArrayList<String> temp = new ArrayList<>();
        for (int i = 0; i < namesofsongs.length; i++) {
            if (typesofsongs.get(namesofsongs[i]).toLowerCase().contains(getString(R.string.parts).toLowerCase()))
                temp.add(namesofsongs[i]);
        }
        nameofsongsdin = temp.toArray(new String[0]);
        ArrayList<String> temp3 = new ArrayList<String>();
        ArrayList<String> temp2 = adapter2.getCheckedpos();
        for (int t = 0; t < nameofsongsdin.length; t++) {
            if (nameofsongsdin[t].toLowerCase().contains(filter.toString().toLowerCase()))
                temp3.add(nameofsongsdin[t]);
        }
        adapter2 = new MyAdapter(MainActivity.this, temp3.toArray(new String[0]), namesofsongs, "Main", longclick, temp2, typesofsongs, false, "noplaylist", audiochoose);
        listt.setAdapter(adapter2);
        xsongs.setTextColor(C2);
        allsongs.setTextColor(C4);
        longsongs.setTextColor(C4);
        shortsongs.setTextColor(C4);
    }
    public void shortsongs(View view) {
        filtr = 2;
        nameofsongsdin = new String[]{};
        ArrayList<String> temp = new ArrayList<>();
        for (int i = 0; i < namesofsongs.length; i++) {
            if (typesofsongs.get(namesofsongs[i]).toLowerCase().contains(getString(R.string.short_songs).toLowerCase()))
                temp.add(namesofsongs[i]);
        }
        nameofsongsdin = temp.toArray(new String[0]);
        ArrayList<String> temp3 = new ArrayList<String>();
        ArrayList<String> temp2 = adapter2.getCheckedpos();
        for (int t = 0; t < nameofsongsdin.length; t++) {
            if (nameofsongsdin[t].toLowerCase().contains(filter.toString().toLowerCase()))
                temp3.add(nameofsongsdin[t]);
        }
        adapter2 = new MyAdapter(MainActivity.this, temp3.toArray(new String[0]), namesofsongs, "Main", longclick, temp2, typesofsongs, false, "noplaylist", audiochoose);
        listt.setAdapter(adapter2);
        shortsongs.setTextColor(C2);
        allsongs.setTextColor(C4);
        longsongs.setTextColor(C4);
        xsongs.setTextColor(C4);
    }
    public void longsongs(View view) {
        filtr = 3;
        nameofsongsdin = new String[]{};
        ArrayList<String> temp = new ArrayList<>();
        for (int i = 0; i < namesofsongs.length; i++) {
            if (typesofsongs.get(namesofsongs[i]).toLowerCase().contains(getString(R.string.long_songs).toLowerCase()))
                temp.add(namesofsongs[i]);
        }
        nameofsongsdin = temp.toArray(new String[0]);
        ArrayList<String> temp3 = new ArrayList<String>();
        ArrayList<String> temp2 = adapter2.getCheckedpos();
        for (int t = 0; t < nameofsongsdin.length; t++) {
            if (nameofsongsdin[t].toLowerCase().contains(filter.toString().toLowerCase()))
                temp3.add(nameofsongsdin[t]);
        }
        adapter2 = new MyAdapter(MainActivity.this, temp3.toArray(new String[0]), namesofsongs, "Main", longclick, temp2, typesofsongs, false, "noplaylist", audiochoose);
        listt.setAdapter(adapter2);
        longsongs.setTextColor(C2);
        allsongs.setTextColor(C4);
        xsongs.setTextColor(C4);
        shortsongs.setTextColor(C4);
    }
    @Override
    protected void onResume() {
        int darktheme=0;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput("options"))); //считываение настроек
            int i=0;

            String src="";
            while ((src = br.readLine()) != null){
                switch (i) {
                    case 4:
                        darktheme=Integer.parseInt(src);
                        break;
                }
                i++;
            }

        } catch (FileNotFoundException e) {
            darktheme=0;
            e.printStackTrace();
        } catch (IOException e) {
            darktheme=0;
            e.printStackTrace();
        }
        if (darktheme==1)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); //темная тема
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onResume();
        mAdView.resume();
        progressBar.setVisibility(ProgressBar.INVISIBLE);
    }
    @Override
    protected void onPause() {
        mAdView.pause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mAdView.destroy();
        super.onDestroy();
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (menutopopen) {
                mymethoddestroy();
                adapter2.changeid(R.layout.fon);
                updatetextchecked(0);
                if (playlistsongs.equals("sadfsadf") == false) {
                    playlistsongs = "sadfsadf";
                    onBackPressed();
                } else {
                }
            } else {
                super.onBackPressed();
            }
        }
    }
}