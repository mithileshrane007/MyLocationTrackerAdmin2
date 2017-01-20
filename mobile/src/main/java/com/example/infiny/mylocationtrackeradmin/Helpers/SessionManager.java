package com.example.infiny.mylocationtrackeradmin.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by infiny on 21/12/16.
 */

public class SessionManager {
    private static final String CLICKED = "clicked";
    // Sharedpref file name
    private static final String PREF_NAME = "Near";
    private static final String LOGIN_PREF = "login";
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;
    //Keys names
    private String CURRENT_LOC_LAT="latit";
    private String CURRENT_LOC_LONG="long";
    private String STORE_TIME="str_time";
    private String COMPANY_ID="company_id";



    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void setLocation(double latit,double longi)
    {

        editor.putFloat(CURRENT_LOC_LAT, ((float) latit));
        editor.putFloat(CURRENT_LOC_LONG, ((float) longi));
        editor.commit();
    }
    public LatLng getLocation()
    {
        LatLng latLng=new LatLng(pref.getFloat(CURRENT_LOC_LAT,0f),pref.getFloat(CURRENT_LOC_LONG,0f));
        return latLng;
    }


    public void clear()
    {
        editor.clear();
        editor.commit();
    }


    public void storeStartTime(long starttime) {
        editor.putLong(STORE_TIME,starttime);
        editor.commit();
    }

    public void storeCompanyID(String company_id) {
        editor.putString(COMPANY_ID,company_id);
        editor.commit();
    }
    public String getCompanyID() {
        return pref.getString(COMPANY_ID,"");
    }

    public long getStoredTime() {
        return pref.getLong(STORE_TIME,0);
    }

    public boolean getClicked() {
        return pref.getBoolean(CLICKED,false);
    }

    public void setClicked(boolean clicked) {
        editor.putBoolean(CLICKED,clicked);
        editor.commit();
    }

    public boolean isLogin() {
        return pref.getBoolean(LOGIN_PREF,false);
    }

    public void setLogin(boolean isLogin) {
        editor.putBoolean(LOGIN_PREF,isLogin);
        editor.commit();
    }


}
