package com.example.bubblechathead;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by multimedia on 29/5/17.
 */
public class SharedPrefrenceClass {
    String TAG = "SharedPrefrenceClass";
    SharedPreferences sharedPreference;
    final String MyPREFERENCES = "Testing Bubble Chat Head";
    public SharedPrefrenceClass(Context cxt) {
        sharedPreference = cxt.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    public String saveShadowColor(String shadowColor){
      //  Log.d(TAG, "saveShadowColor: called");
        try{
            SharedPreferences.Editor editor;
            editor = sharedPreference.edit();
            editor.putString("shadow_color", shadowColor);
            editor.commit();
            return null;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public String retrieveShadowColor(){
       // Log.d(TAG, "retrieveShadowColor: called");
        try{
            String savedShadowColor=sharedPreference.getString("shadow_color","");
            return savedShadowColor;
        }catch (Exception e) {
            e.printStackTrace();
                return "";
        }
    }
    public int deleteShadowColor() {
       // Log.d(TAG, "deleteShadowColor: called");
        try {
            SharedPreferences.Editor editor;
            editor = sharedPreference.edit();
            editor.remove("shadow_color");
            editor.commit();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
