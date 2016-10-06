package com.rajarshisharma.popularmovies.data;

import android.net.Uri;

/**
 * Created by etarzan on 28/9/16.
 */

public class ContractBase {
    //base URIs for data management
    public static final String CONTENT_AUTHORITY = "com.rajarshisharma.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


}
