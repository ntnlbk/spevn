package com.LibBib.spevn;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MyAdapter extends ArrayAdapter<String> implements Filterable {



    private final Context context;
    private String[] resource;
    private String[]  texts, alwaysthesame;
    private String nameofplaylist;
    private ArrayList<String> checkedpos = new ArrayList<>();
    private ArrayList<Integer> positions = new ArrayList<>();
    private ArrayList<String> nameschecked = new ArrayList<>();
    private Boolean longtap = false;
    private Boolean temp = false;
    private Boolean isfromplaylist =false;
    private RelativeLayout layout;
    private Activity activity;
    private CheckBox checkBox;
    private int id = R.layout.fon;
    private int checked;
    private String c;
    private LayoutInflater inflater;
    long mills =100L;
    ImageButton imageButton;
    private Map<String, String> types=new HashMap<String, String>();




    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(checkedpos.contains(resource[position])){
            id=R.layout.fon3;
            convertView = inflater.inflate(id, parent, false);
            id=R.layout.fon2;
        }
        else {

            if (convertView == null || id != R.layout.fon2) {
                convertView = inflater.inflate(id, parent, false);


            } else if (id == R.layout.fon2) {
                convertView = inflater.inflate(id, parent, false);

            }
        }



        final View mainView = inflater.inflate(R.layout.activity_main, null);

        layout=(RelativeLayout) mainView.findViewById(R.id.relative);

        TextView textView = (TextView) convertView.findViewById(R.id.textView4);
        TextView type = (TextView) convertView.findViewById(R.id.textView5);
        imageButton = (ImageButton) convertView.findViewById(R.id.imageButton);
        Button btn = (Button)convertView.findViewById(R.id.mainbtn);
         checkBox = (CheckBox)convertView.findViewById(R.id.checkBox);



            textView.setText(resource[position]);
            type.setText(types.get(resource[position]));
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    showtext(resource[position], mainView);

                }
            });

            btn.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    if (vibrator.hasVibrator()) {
                        vibrator.vibrate(mills);
                    }
                    longtap = true;

                    id = R.layout.fon2;

                    notifyDataSetChanged();
                    ((MyIntefrace) context).mymethod();
                    return false;
                }
            });
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog(position);


                }
            });
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        checked++;
                        checkedpos.add(resource[position]);
                    } else {
                        checked--;

                        checkedpos.remove(resource[position]);
                    }
                    ((MyIntefrace) context).updatetextchecked(checked);
                }
            });


            return convertView;

    }

    public MyAdapter(Context context, String[] resource, String[] alwaysthesame, String c, boolean longclick, ArrayList<String> checkedpos, Map<String, String> types, boolean isfromplaylist, String nameofplaylist) {
        super(context, R.layout.fon, resource);
        if (longclick)
            id = R.layout.fon2;
        this.context = context;
        this.resource = resource;
        this.alwaysthesame = alwaysthesame;
        this.c = c;
        this.inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.checkedpos=checkedpos;
        checked=checkedpos.size();
        this.types=types;
        this.isfromplaylist=isfromplaylist;
        this.nameofplaylist=nameofplaylist;
    }

    public ArrayList<String> getCheckedpos(){
        return checkedpos;
    }
    public Boolean getLongtap(){return longtap;}


    private void showtext(String b, View mainView) {

                Intent intent = new Intent(context, textofsongwindoww.class);
                intent.putExtra("имя", b);
                if(isfromplaylist){
                    intent.putExtra("isfromplaylist", "true");
                    intent.putExtra("nameofplaylist", nameofplaylist);
                }
                else{
                    intent.putExtra("isfromplaylist", "false");
                }
                if(b.equals("Хвала на вышынях Богу(G)"))
                    intent.putExtra("extra", "Gloria");
                else {
                    if(b.equals("Слухай, Ізраэлю!"))
                    intent.putExtra("extra", "Слухай");
                    else
                    intent.putExtra("extra", "none");}


                    context.startActivity(intent);

                ((MyIntefrace)context).progressbar();

    }
    public void changeid(int idm){
        checkedpos.clear();
        checked=0;
        id=idm;
        notifyDataSetChanged();
    }

    public void dialog(final int positiononlyfromplaylist) {
        if (c.equals("Main")) {
            View promptsView = inflater.inflate(R.layout.moredialog, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogStyle);
            builder.setView(promptsView);

            Button addtoplalist = (Button) promptsView.findViewById(R.id.addtoplaylist);
            Button addtoizbr = (Button) promptsView.findViewById(R.id.addtoizbr);
            final Dialog dialog = builder.create();
            addtoplalist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkedpos.size() == 0) {
                        nameschecked.add(resource[positiononlyfromplaylist]);

                        ((MyIntefrace) context).addtoplalist(nameschecked);
                    } else {
                        for(int i=0; i< checkedpos.size(); i++)
                            nameschecked.add(checkedpos.get(i));
                        ((MyIntefrace) context).addtoplalist(nameschecked);
                    }
                    nameschecked.clear();
                    checkedpos.clear();
                    dialog.dismiss();
                }
            });

            addtoizbr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkedpos.size() == 0) {
                        nameschecked.add(resource[positiononlyfromplaylist]);

                        ((MyIntefrace) context).addtoizbr(nameschecked);
                    } else {
                        for(int i=0; i< checkedpos.size(); i++)
                            nameschecked.add(checkedpos.get(i));
                        ((MyIntefrace) context).addtoizbr(nameschecked);
                    }
                    nameschecked.clear();
                    checkedpos.clear();
                    dialog.dismiss();
                }
            });


            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.y = 2100;

            dialog.getWindow().setAttributes(params);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00E5E5E5")));
            dialog.show();
        } else if (c.equals("Playlist")){
            View promptsView = inflater.inflate(R.layout.dialogforsongsinplaylist, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogStyle);
            builder.setView(promptsView);

            Button addroplaylistfromplaylist = (Button) promptsView.findViewById(R.id.addtoplalistfromplaylist);
            Button deletefromplaylist = (Button) promptsView.findViewById(R.id.deletefromplaylist);
            final Dialog dialog = builder.create();
            addroplaylistfromplaylist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkedpos.size() == 0) {
                        if(positiononlyfromplaylist<=resource.length) {
                            nameschecked.add(resource[positiononlyfromplaylist]);
                        }
                        ((MyIntefrace) context).addtoplalist(nameschecked);
                    } else {
                        for(int i=0; i< checkedpos.size(); i++)
                            nameschecked.add(checkedpos.get(i));
                        ((MyIntefrace) context).addtoplalist(nameschecked);
                    }
                    nameschecked.clear();
                    checkedpos.clear();

                    dialog.dismiss();
                }
            });

            deletefromplaylist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkedpos.size() == 0 && positiononlyfromplaylist !=222222) {
                        nameschecked.add(resource[positiononlyfromplaylist]);
                        ((PlaylistSongs) context).delete(nameschecked);
                        positions.clear();
                    } else {
                        for( int i=0; i< checkedpos.size(); i++){
                            nameschecked.add(checkedpos.get(i));
                        }
                        ((PlaylistSongs) context).delete(nameschecked);
                    }
                    nameschecked.clear();
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

}
