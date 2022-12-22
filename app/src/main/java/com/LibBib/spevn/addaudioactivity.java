package com.LibBib.spevn;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.Html;
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

import static com.LibBib.spevn.addsongactivity.hasConnection;

public class addaudioactivity extends AppCompatActivity {


    private ImageView backbtn;
    private TextView ytext, choosetext, cancelmusbtn;
    private Uri musUri;
    private String musfilename="", author, link="";
    private Button sendbtn;
    private String nameofsong="", name="";
    private EditText nameedit, authoredit, linkedit;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addaudioactivity);

        backbtn=(ImageView)findViewById(R.id.backbtn3);
        ytext=(TextView)findViewById(R.id.musfileaddbtn2);
        choosetext=(TextView)findViewById(R.id.choosesong);
        cancelmusbtn=(TextView)findViewById(R.id.canseltext3);
        sendbtn=(Button)findViewById(R.id.sendbtn2);
        nameedit=(EditText)findViewById(R.id.nameedit2) ;
        authoredit=(EditText)findViewById(R.id.authoredit2) ;
        linkedit=(EditText)findViewById(R.id.linkedit2) ;
        progressBar=(ProgressBar)findViewById(R.id.progressBar4) ;
        ytext.setText(Html.fromHtml(getResources().getString(R.string.you_can_add_audiofile1) + " <font color='#FFC700'> " + getResources().getString(R.string.you_can_add_audiofile2) + "</font>"));


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        choosetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(addaudioactivity.this, MainActivity.class);
                intent.putExtra("audiochoose", "true");
                startActivityForResult(intent, 1);
            }
        });
        ytext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent = Intent.createChooser(intent, "Choose a file");
                startActivityForResult(intent, 2);
            }
        });

        cancelmusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(musUri!=null){
                    musUri=null;
                    cancelmusbtn.setVisibility(View.INVISIBLE);
                    musfilename="";

                    ytext.setText(Html.fromHtml(getResources().getString(R.string.you_can_add_audiofile1) + " <font color='#FFC700'> " + getResources().getString(R.string.you_can_add_audiofile2) + "</font>"));
                }
            }
        });
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameedit.getText().toString();
                author = authoredit.getText().toString();
                link=linkedit.getText().toString();

                if(!hasConnection(addaudioactivity.this))
                    Toast.makeText(addaudioactivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                else if(!name.equals("") && (!link.equals("") || !musfilename.equals("")) && !nameofsong.equals("")){

                    progressBar.setVisibility(View.VISIBLE);
                    String filename = name+ System.currentTimeMillis();

                    FileOutputStream fos;
                    try {
                        fos = openFileOutput(filename, MODE_PRIVATE);

                        fos.write(("Пользователь " + name + " предложил аудиозапись к песне " + nameofsong).getBytes());


                        if(!author.equals(""))
                            fos.write(("\n" + "Автор: " + author).getBytes());
                        if(!link.equals(""))
                            fos.write(("\n" + "Ссылка на аудиофайл: " + link).getBytes());

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
                            Toast.makeText(addaudioactivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                              if(musUri!=null){

                                StorageReference musRef = storageRef.child("musfiles/").child(musfilename + musUri.getLastPathSegment());
                                UploadTask uploadTask = musRef.putFile(musUri);
                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(addaudioactivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                    }
                                });
                            }
                            Toast.makeText(addaudioactivity.this, getResources().getString(R.string.success_thaks), Toast.LENGTH_SHORT).show();
                            File file = new File(mainfile.getPath());
                            file.delete();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });

                } else{
                    Toast.makeText(addaudioactivity.this, getResources().getString(R.string.enter_your_name), Toast.LENGTH_SHORT).show();
                }




            }
        });
    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 1:
                if (data == null) {
                    return;
                }
                nameofsong = data.getStringExtra("name");
                choosetext.setText(nameofsong);
                choosetext.setTextColor(getResources().getColor(R.color.C2));
                super.onActivityResult(requestCode, resultCode, data);
                break;
            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        musUri = data.getData();


                        try {

                        } catch (Exception e) {
                            //     Log.d("MyLog","Error: " + e);
                            //         Toast.makeText(addsongactivity.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                        }

                        Cursor returnCursor =
                                getContentResolver().query(musUri, null, null, null, null);
                        returnCursor.moveToFirst();
                        musfilename = returnCursor.getString(returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        ytext.setText(musfilename);
                        cancelmusbtn.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }
    }
}