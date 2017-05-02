package com.example.jendrik.moerder.GUI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.example.jendrik.moerder.R;

public class TextFieldHelper {

    public static boolean stringIsEmpty(String s){
        boolean allEmpty = true;
        for(int i = 0; i < s.length(); i++){
            if(s.charAt(i) != ' '){
                allEmpty = false;
            }
        }
        if(s.isEmpty())
            allEmpty = true;
        if(s.equals(null))
            allEmpty = true;
        return allEmpty;
    }
}
