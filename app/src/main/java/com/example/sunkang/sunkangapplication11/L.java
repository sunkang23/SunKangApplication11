package com.example.sunkang.sunkangapplication11;

import android.util.Log;

/**
 * Created by sunkang on 2017/6/23.
 */

public class L {
    private static final String TAG = "suk_imooc";
    private static boolean debug = true;
    public static void e (String msg){
        if(debug){
            Log.e(TAG,msg);
        }

    }
}
