package com.rajarshisharma.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rajarshisharma.popularmovies.data.contracts.FavoritesContract;
import com.rajarshisharma.popularmovies.data.contracts.MovieContract;
import com.rajarshisharma.popularmovies.data.contracts.ReviewsContract;
import com.rajarshisharma.popularmovies.data.contracts.TrailersContract;

/**
 * Created by etarzan on 28/9/16.
 */

public class MovieDBHelper  extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 1;

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE__TABLE = "CREATE TABLE " + MovieContract.TABLE_NAME + " (" +
                MovieContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.PAGE + "  TEXT," +
                MovieContract.POSTER_PATH + "  TEXT," +
                MovieContract.OVERVIEW + "  TEXT," +
                MovieContract.RELEASE_DATE + "  TEXT," +
                MovieContract.MOVIE_ID + "  TEXT," +
                MovieContract.ORIGINAL_TITLE + "  TEXT," +
                MovieContract.ORIGINAL_LANGUAGE + "  TEXT," +
                MovieContract.TITLE + "  TEXT," +
                MovieContract.BACKDROP_PATH + "  TEXT," +
                MovieContract.POPULARITY + "  TEXT," +
                MovieContract.VOTE_COUNT + "  TEXT," +
                MovieContract.VOTE_AVERAGE + "  TEXT," +
                MovieContract.FAVOURED + " INTEGER NOT NULL DEFAULT 0," +
                MovieContract.SHOWED + " INTEGER NOT NULL DEFAULT 0," +
                MovieContract.DOWNLOADED + " INTEGER NOT NULL DEFAULT 0," +
                MovieContract.SORT_BY + " TEXT,"
                + "UNIQUE (" + MovieContract.MOVIE_ID + ") ON CONFLICT REPLACE)";

        final String SQL_CREATE__TABLE_FAVOURITES = "CREATE TABLE " + FavoritesContract.TABLE_NAME + " (" +
                FavoritesContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavoritesContract.PAGE + "  TEXT," +
                FavoritesContract.POSTER_PATH + "  TEXT," +
                FavoritesContract.ADULT + "  TEXT," +
                FavoritesContract.OVERVIEW + "  TEXT," +
                FavoritesContract.RELEASE_DATE + "  TEXT," +
                FavoritesContract.MOVIE_ID + "  TEXT," +
                FavoritesContract.ORIGINAL_TITLE + "  TEXT," +
                FavoritesContract.ORIGINAL_LANGUAGE + "  TEXT," +
                FavoritesContract.TITLE + "  TEXT," +
                FavoritesContract.BACKDROP_PATH + "  TEXT," +
                FavoritesContract.POPULARITY + "  TEXT," +
                FavoritesContract.VOTE_COUNT + "  TEXT," +
                FavoritesContract.VOTE_AVERAGE + "  TEXT," +
                FavoritesContract.FAVOURED + " INTEGER NOT NULL DEFAULT 0," +
                FavoritesContract.SHOWED + " INTEGER NOT NULL DEFAULT 0," +
                FavoritesContract.DOWNLOADED + " INTEGER NOT NULL DEFAULT 0," +
                FavoritesContract.SORT_BY + " TEXT,"
                + "UNIQUE (" + FavoritesContract.MOVIE_ID + ") ON CONFLICT REPLACE)";

        final String SQL_CREATE__TABLE_TRAILERS = "CREATE TABLE " + TrailersContract.TABLE_NAME + " (" +
                TrailersContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TrailersContract.NAME + "  TEXT," +
                TrailersContract.SIZE + "  TEXT," +
                TrailersContract.SOURCE + "  TEXT," +
                TrailersContract.TYPE + "  TEXT," +
                TrailersContract.MOVIE_ID + "  TEXT ," +
                "UNIQUE (" + TrailersContract.SOURCE + ") ON CONFLICT REPLACE)";

        final String SQL_CREATE__TABLE_REVIEWS = "CREATE TABLE " + ReviewsContract.TABLE_NAME + " (" +
                ReviewsContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ReviewsContract.PAGE + "  TEXT," +
                ReviewsContract.TOTAL_PAGE + "  TEXT," +
                ReviewsContract.TOTAL_RESULTS + "  TEXT," +
                ReviewsContract.ID_REVIEWS + "  TEXT," +
                ReviewsContract.AUTHOR + "  TEXT ," +
                ReviewsContract.CONTENT + "  TEXT," +
                ReviewsContract.URL + "  TEXT," +
                ReviewsContract.MOVIE_ID + "  TEXT )";

        sqLiteDatabase.execSQL(SQL_CREATE__TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE__TABLE_FAVOURITES);
        sqLiteDatabase.execSQL(SQL_CREATE__TABLE_TRAILERS);
        sqLiteDatabase.execSQL(SQL_CREATE__TABLE_REVIEWS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoritesContract.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TrailersContract.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ReviewsContract.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }
}
