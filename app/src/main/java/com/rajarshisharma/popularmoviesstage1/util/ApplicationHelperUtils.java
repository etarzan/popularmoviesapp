package com.rajarshisharma.popularmoviesstage1.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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
}
