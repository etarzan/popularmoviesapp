package com.rajarshisharma.popularmovies.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.rajarshisharma.popularmovies.R;

/**
 * Created by Rajarshi.Sharma on 3/17/2016.
 */
public class ApplicationHelperUtils {

    public static boolean isNetworkAvailable(Activity activity){
        ConnectivityManager connectionMgr= (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectionMgr.getActiveNetworkInfo();
        if(networkInfo !=null && networkInfo.isConnected())
            return  true;
        else
            return false;
    }

    public static String getPreferredSorting(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_sort_key),
                context.getString(R.string.pref_sort_default));
    }
}
