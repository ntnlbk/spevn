package com.LibBib.spevn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.OpenableColumns;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class addsongactivity extends AppCompatActivity {

    private ImageView backbtn;

    private TextView addtextfilebtn, addmusfilebtn, canceltxtbtn, cancelmusbtn;

    private Button sendbtn;

    private EditText nameedit, authoredit, textedit, nameofsongedit, linkedit;

    private ProgressBar progressBar;

    private Uri textUri = null;
    private Uri musUri = null;
    private  String  name, text, author, nameofsong, link;
    private String musfilename="";
    private String textfilename="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addsongactivity);

        backbtn=(ImageView)findViewById(R.id.backbtn1);
        addtextfilebtn = (TextView)findViewById(R.id.textfileaddbtn);
        addmusfilebtn = (TextView)findViewById(R.id.musfileaddbtn);
        sendbtn=(Button)findViewById(R.id.sendbtn);
        canceltxtbtn=(TextView)findViewById(R.id.canseltext);
        nameedit=(EditText)findViewById(R.id.nameedit);
        authoredit=(EditText)findViewById(R.id.authoredit);
        textedit=(EditText)findViewById(R.id.textedit);
        nameofsongedit=(EditText)findViewById(R.id.nameofsongedit);
        linkedit=(EditText)findViewById(R.id.linkedit);
        cancelmusbtn=(TextView)findViewById(R.id.canselmus);
        progressBar=(ProgressBar)findViewById(R.id.progressBar5);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        addtextfilebtn.setText(Html.fromHtml("или вы можете <font color='#FFC700'> прикрепить текстовый файл </font>"));  //делаем выделенные слова желтыми
        addmusfilebtn.setText(Html.fromHtml("или вы можете <font color='#FFC700'> прикрепить аудиофайл </font>"));

        addtextfilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent = Intent.createChooser(intent, "Choose a file");
                startActivityForResult(intent, 1);



            }
        });
        addmusfilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent = Intent.createChooser(intent, "Choose a file");
                startActivityForResult(intent, 2);
            }
        });

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameedit.getText().toString();
                text = textedit.getText().toString();
                author = authoredit.getText().toString();
                link=linkedit.getText().toString();
                nameofsong=nameofsongedit.getText().toString();
                if(!hasConnection(addsongactivity.this))
                    Toast.makeText(addsongactivity.this, "Нет подключения к интернету", Toast.LENGTH_SHORT).show();
                else if(!name.equals("") && !nameofsong.equals("")){
                    progressBar.setVisibility(View.VISIBLE);
                    String filename = name+ System.currentTimeMillis();

                    FileOutputStream fos;
                    try {
                        fos = openFileOutput(filename, MODE_PRIVATE);

                        fos.write(("Пользователь " + name + " предложил песню " + nameofsong).getBytes());

                        if(!text.equals(""))
                            fos.write(( "\n" + text).getBytes());
                        if(!author.equals(""))
                            fos.write(("\n" + "Автор: " + author).getBytes());
                        if(!link.equals(""))
                            fos.write(("\n" + "Ссылка на аудиофайл: " + link).getBytes());
                        if(!textfilename.equals(""))
                            fos.write(("\n" + "Приложен текстовый файл: " + textfilename).getBytes());
                        if(!musfilename.equals(""))
                            fos.write(("\n" + "Приложен  аудиофайл: " + musfilename).getBytes());
                        fos.close();
                   //     Toast.makeText(this, "Файл сохранен", Toast.LENGTH_SHORT).show();
                    }
                    catch(IOException ex) {
                 //       Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    Uri mainfile = Uri.fromFile(new File("/data/data/com.LibBib.spevn/files/" + filename));

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();
                    StorageReference usersongsRef = storageRef.child("usersongs/").child( filename);

                    UploadTask uploadTask = usersongsRef.putFile(mainfile);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(addsongactivity.this, "Ошибка", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                             if(textUri!=null){

                        StorageReference textRef = storageRef.child("textfiles/").child(textfilename + textUri.getLastPathSegment());
                        UploadTask uploadTask = textRef.putFile(textUri);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(addsongactivity.this, "Ошибка", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            }
                        });
                    }  if(musUri!=null){

                                 StorageReference musRef = storageRef.child("musfiles/").child(musfilename + musUri.getLastPathSegment());
                                 UploadTask uploadTask = musRef.putFile(musUri);
                                 uploadTask.addOnFailureListener(new OnFailureListener() {
                                     @Override
                                     public void onFailure(@NonNull Exception e) {
                                         Toast.makeText(addsongactivity.this, "Ошибка", Toast.LENGTH_SHORT).show();

                                     }
                                 }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                     @Override
                                     public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                     }
                                 });
                             }
                            Toast.makeText(addsongactivity.this, "Данные успешно отправлены, спасибо!", Toast.LENGTH_SHORT).show();
                             File file = new File(mainfile.getPath());
                             file.delete();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });

                } else{
                    Toast.makeText(addsongactivity.this, "Введите ваше имя и название песни", Toast.LENGTH_SHORT).show();
                }


            }
        });

        canceltxtbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(textUri!=null){
                    textUri=null;
                    canceltxtbtn.setVisibility(View.INVISIBLE);
                    textfilename="";
                    addtextfilebtn.setText(Html.fromHtml("или вы можете <font color='#FFC700'> прикрепить текстовый файл </font>"));
                }
            }
        });
        cancelmusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(musUri!=null){
                    musUri=null;
                    cancelmusbtn.setVisibility(View.INVISIBLE);
                    musfilename="";
                    addmusfilebtn.setText(Html.fromHtml("или вы можете <font color='#FFC700'> прикрепить аудиофайл </font>"));
                }
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK ) {
                    if(data != null)  {
                        textUri = data.getData();



                        try {

                        } catch (Exception e) {
                       //     Log.d("MyLog","Error: " + e);
                   //         Toast.makeText(addsongactivity.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                        }

                        Cursor returnCursor =
                                getContentResolver().query(textUri, null, null, null, null);
                        returnCursor.moveToFirst();
                        textfilename=returnCursor.getString(returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        addtextfilebtn.setText(textfilename);
                        canceltxtbtn.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case 2:
                if (resultCode == Activity.RESULT_OK ) {
                    if(data != null)  {
                        musUri = data.getData();



                        try {

                        } catch (Exception e) {
                            //     Log.d("MyLog","Error: " + e);
                            //         Toast.makeText(addsongactivity.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                        }

                        Cursor returnCursor =
                                getContentResolver().query(musUri, null, null, null, null);
                        returnCursor.moveToFirst();
                        musfilename=returnCursor.getString(returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        addmusfilebtn.setText(musfilename);
                        cancelmusbtn.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
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