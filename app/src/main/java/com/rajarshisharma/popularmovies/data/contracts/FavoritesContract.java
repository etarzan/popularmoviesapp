package com.rajarshisharma.popularmovies.data.contracts;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import com.rajarshisharma.popularmovies.data.ContractBase;

/**
 * Created by etarzan on 28/9/16.
 */

public final class FavoritesContract implements BaseColumns {
    public static final String FAVORITES_URI = "favorites";

    public static final String TABLE_NAME = "favourites";
    public static final String PAGE = "page";
    public static final String POSTER_PATH = "poster_path";
    public static final String ADULT = "adult";
    public static final String OVERVIEW = "overview";
    public static final String RELEASE_DATE = "release_date";
    public static final String MOVIE_ID = "id";
    public static final String ORIGINAL_TITLE = "original_title";
    public static final String ORIGINAL_LANGUAGE = "original_language";
    public static final String TITLE = "title";
    public static final String BACKDROP_PATH = "backdrop_path";
    public static final String POPULARITY = "popularity";
    public static final String VOTE_COUNT = "vote_count";
    public static final String VOTE_AVERAGE = "vote_average";
    public static final String FAVOURED = "favoured";
    public static final String SHOWED = "shown";
    public static final String DOWNLOADED = "downloaded";
    public static final String SORT_BY = "sort_by";

    public static final Uri CONTENT_URI = ContractBase.BASE_CONTENT_URI
            .buildUpon()
            .appendPath(FAVORITES_URI)
            .build();

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                    "/" + ContractBase.CONTENT_AUTHORITY
                    + "/" + FAVORITES_URI;
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
                    + "/" + ContractBase.CONTENT_AUTHORITY
                    + "/" + FAVORITES_URI;

    public static Uri buildMoviesUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

    //content://CONTENT_AUTHORITY/favourites/MovieId
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

