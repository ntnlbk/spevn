package com.LibBib.spevn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.material.navigation.NavigationView;

public class PodborkiActivity extends AppCompatActivity{

    private DrawerLayout drawer;
    private String[] longsongs = {"","",""};
    private String[] shortsongs = {"","",""};
    private String[] xsongs = {"","",""};
    private String[] allsongs, textofsongs;
    private ImageButton menubtn;

    private ListView longlist, shortlist, xlist;


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = getIntent();
            if (intent.getStringExtra("back").equals("main")) {
                Intent intent2 = new Intent(PodborkiActivity.this, MainActivity.class);
                startActivity(intent2);
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podborki);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);


        longlist=(ListView)findViewById(R.id.longlist);
       shortlist=(ListView)findViewById(R.id.shortlist);
       xlist=(ListView)findViewById(R.id.xlist);
        drawer = (DrawerLayout) findViewById(R.id.relative_layout);
        menubtn=(ImageButton)findViewById(R.id.menubtn2);

        allsongs=getResources().getStringArray(R.array.names);
        textofsongs=getResources().getStringArray(R.array.texts);

        longsongs[0]=allsongs[8];
        longsongs[1]=allsongs[75];
        longsongs[2]=allsongs[55];


        shortsongs[0]=allsongs[5];
        shortsongs[1]=allsongs[25];
        shortsongs[2]=allsongs[43];

        xsongs[0]=allsongs[4];
        xsongs[1]=allsongs[12];
        xsongs[2]=allsongs[54];

        final ArrayAdapter<String> adapterlong = new ArrayAdapter <String> (this, R.layout.fon, longsongs);
        final ArrayAdapter<String> adaptershort = new ArrayAdapter <String> (this, R.layout.fon, shortsongs);
        final ArrayAdapter<String> adapterx = new ArrayAdapter <String> (this, R.layout.fon, xsongs);


        longlist.setAdapter(adapterlong);
        shortlist.setAdapter(adaptershort);
        xlist.setAdapter(adapterx);

        menubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        longlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String nameoftext = adapterlong.getItem(i);
                showtext(nameoftext);
            }
        });
        shortlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String nameoftext = adaptershort.getItem(i);
                showtext(nameoftext);
            }
        });

        xlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String nameoftext = adapterx.getItem(i);
                showtext(nameoftext);
            }
        });

    }




    private void showtext(String b) {
        for(int i =0; i< allsongs.length; i++) {
            if (allsongs[i]==b){
                Intent intent = new Intent(this, textofsongwindoww.class);
                intent.putExtra("текст", textofsongs[i]);
                startActivity(intent);

            }
        }


    }
}