package com.LibBib.spevn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class spisokactivity extends AppCompatActivity {

    private ListView spisok;
    private EditText search;
    private Button delete;
    private String[] namesofsongs, texts;
    private String[]temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spisokactivity);

        spisok = (ListView)findViewById(R.id.namess);

        delete = (Button)findViewById(R.id.delete);
        namesofsongs = getResources().getStringArray(R.array.names);
        texts = getResources().getStringArray(R.array.texts);
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput("spisok")));
            String str = "";

            int i=0;
            while ((str = br.readLine()) != null) {

                i++;
            }
             String[] names = new String[i];
            i=0;
            br.close();
            // открываем поток для чтения
            BufferedReader br1 = new BufferedReader(new InputStreamReader(
                    openFileInput("spisok")));
            while ((str = br1.readLine()) != null) {
                names[i]=str;
                i++;
            }
            temp=names;
            final ArrayAdapter<String> adapter = new ArrayAdapter <String> (spisokactivity.this, R.layout.fon, names);
            spisok.setAdapter(adapter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                            openFileOutput("spisok", MODE_PRIVATE)));

                    bw.write("");

                    bw.close();
                    finish();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        spisok.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String nameoftext = temp[position];
                showtext(nameoftext);
            }
        });


    }
    private void showtext(String b) {
        for(int i =0; i< namesofsongs.length; i++) {
            if (namesofsongs[i].equals(b)){
                Intent intent = new Intent(this, textofsongwindoww.class);
                intent.putExtra("текст", texts[i]);
                startActivity(intent);

            }
        }


    }
}
