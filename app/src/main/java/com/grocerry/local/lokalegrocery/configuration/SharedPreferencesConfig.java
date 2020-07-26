package com.grocerry.local.lokalegrocery.configuration;

import android.content.Context;
import android.content.SharedPreferences;

import com.grocerry.local.lokalegrocery.R;


/**
 * Created by Prabhakar.K on 7/22/2020.
 */

public class SharedPreferencesConfig {

    private SharedPreferences sharedPreferences;
    private Context context;

    public SharedPreferencesConfig(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.user_register_preference),Context.MODE_PRIVATE);
    }

    public void writeLoginStatus(boolean status){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getResources().getString(R.string.user_login_status_preferrence),status);
        editor.commit();
    }

    public boolean readLoginStatus(){
        boolean status = false;
        status = sharedPreferences.getBoolean(context.getResources().getString(R.string.user_login_status_preferrence),false);
        return status;
    }

    public void storeDataInData(String key,String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public String getCustomerInfo(String key){
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.user_register_preference),Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");// getting default value of String
    }

    public void clearSharedPreferenceObject(){
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.user_register_preference),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
