package com.rajarshisharma.popularmovies.data.contracts;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import com.rajarshisharma.popularmovies.data.ContractBase;

/**
 * Created by etarzan on 28/9/16.
 */

public final class TrailersContract implements BaseColumns {
    public static final String TRAILER_URI = "trailers";

    public static final String TABLE_NAME = "trailers";
    public static final String NAME = "name";
    public static final String SIZE = "size";
    public static final String SOURCE = "source";
    public static final String TYPE = "type";
    public static final String MOVIE_ID = "id";


    public static final Uri CONTENT_URI =
            ContractBase.BASE_CONTENT_URI.buildUpon().appendPath(TRAILER_URI).build();

    public static final String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + ContractBase.CONTENT_AUTHORITY + "/" + TRAILER_URI;
    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + ContractBase.CONTENT_AUTHORITY + "/" + TRAILER_URI;

    public static Uri buildMoviesUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

    //content://CONTENT_AUTHORITY/trailers/MovieId
    public static Uri buildMoviesUriWithMovieId(String MovieId) {
        return CONTENT_URI.buildUpon().appendPath(MovieId).build();
    }

    public static Uri buildMovieUri() {
        return CONTENT_URI.buildUpon().build();
    }

    public static String getIDFromUri(Uri uri) {
        return uri.getPathSegments().get(1);
    }


}

