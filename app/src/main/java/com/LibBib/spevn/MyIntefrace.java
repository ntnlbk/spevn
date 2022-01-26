package com.LibBib.spevn;

import android.widget.CheckBox;
import android.widget.ImageButton;

import java.util.ArrayList;

public interface MyIntefrace {

    void mymethod();
    void mymethoddestroy();

    void progressbar();

        void updatetextchecked(int i);
        void addtoizbr(ArrayList<String> positions);
        void addtoplalist(ArrayList<String> namesofsongstoadd);
        void addsongstoplaylist(String[] namesofsongstoadd, String nameofplaylist);
        void audiochoose(String name);

}
