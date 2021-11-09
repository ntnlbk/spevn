package com.LibBib.spevn;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class AdapterForPlaylist extends ArrayAdapter<String> {

    String[] namesofplaylists;
    private LayoutInflater inflater;
    private final String[] resource, loh;
    private final Context context;
    private Map<String, Integer> chislo;

    Button mainbtn;
    Boolean dialog;


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View mainView;
        if(dialog) {
            mainView = inflater.inflate(R.layout.fonplaylistindialog, parent, false);

        }
        else
             mainView = inflater.inflate(R.layout.fonplaylist, parent, false);

        TextView textView = (TextView)mainView.findViewById(R.id.textView4);

        TextView chislotext = (TextView)mainView.findViewById(R.id.chislotext);

        textView.setText(namesofplaylists[position]);
        chislotext.setText("Песен: "+ chislo.get(namesofplaylists[position]));
        ImageButton morebtn = (ImageButton)mainView.findViewById(R.id.imageButton);

        mainbtn=(Button)mainView.findViewById(R.id.mainbtn);

        morebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog(position);
            }
        });
        if(dialog==false) {
            mainbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((InterfacePlaylist) context).openplaylist(namesofplaylists[position]);

                }
            });
        } else if (dialog){
            mainbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MyIntefrace)context).addsongstoplaylist(loh, namesofplaylists[position]);
                }
            });
        }

        return mainView;
    }

    public AdapterForPlaylist(Context context, String[] resource, String[] namesofplaylists, Map chislo, Boolean dialog, String[] loh) {
        super(context, R.layout.fonplaylist, resource);
        this.resource=resource;
        this.context=context;
        this.namesofplaylists=namesofplaylists;
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.chislo=chislo;
        this.dialog=dialog;
        this.loh = loh;

    }

    public  void dialog(final int position){
        View promptsView = inflater.inflate(R.layout.moredialogplaylist, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogStyle);
        builder.setView(promptsView);

        Button delete = (Button) promptsView.findViewById(R.id.delete);
        Button redact = (Button) promptsView.findViewById(R.id.redact);

        final Dialog dialog = builder.create();
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((InterfacePlaylist)context).delete(namesofplaylists[position]);
                dialog.dismiss();

            }
        });
        redact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((InterfacePlaylist)context).redact(namesofplaylists[position]);
                dialog.dismiss();
            }
        });




        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.y = 2100;

        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00E5E5E5")));
        dialog.show();
    }

}
