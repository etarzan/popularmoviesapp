package com.rajarshisharma.popularmovies.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.rajarshisharma.popularmovies.data.contracts.FavoritesContract;
import com.rajarshisharma.popularmovies.data.contracts.MovieContract;
import com.rajarshisharma.popularmovies.data.contracts.ReviewsContract;
import com.rajarshisharma.popularmovies.data.contracts.TrailersContract;

/**
 * Created by etarzan on 28/9/16.
 */

public class MovieDBProvider extends ContentProvider {

    static final int MOVIE = 100;
    static final int MOVIE_ID = 101;
    static final int TRAILERS = 200;
    static final int TRAILERS_ID = 201;
    static final int REVIEWS = 300;
    static final int REVIEWS_ID = 301;
    static final int FAVOURITES = 500;
    static final int FAVOURITES_ID = 501;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDBHelper mOpenHelper;

    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ContractBase.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.MOVIE_URI, MOVIE);
        matcher.addURI(authority, MovieContract.MOVIE_URI + "/*", MOVIE_ID);

        matcher.addURI(authority, TrailersContract.TRAILER_URI, TRAILERS);
        matcher.addURI(authority, TrailersContract.TRAILER_URI + "/*", TRAILERS_ID);
        
        matcher.addURI(authority, ReviewsContract.REVIEW_URI, REVIEWS);
        matcher.addURI(authority, ReviewsContract.REVIEW_URI + "/*", REVIEWS_ID);
        
        matcher.addURI(authority, FavoritesContract.FAVORITES_URI, FAVOURITES);
        System.out.println(authority+"!!!!"+FavoritesContract.FAVORITES_URI+"!!!!"+FAVOURITES);
        matcher.addURI(authority, FavoritesContract.FAVORITES_URI + "/*", FAVOURITES_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDBHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {

            case MOVIE:
                return MovieContract.CONTENT_TYPE;
            case MOVIE_ID:
                return MovieContract.CONTENT_ITEM_TYPE;
            case FAVOURITES:
                return FavoritesContract.CONTENT_TYPE;
            case FAVOURITES_ID:
                return FavoritesContract.CONTENT_ITEM_TYPE;
            case TRAILERS:
                return TrailersContract.CONTENT_TYPE;
            case TRAILERS_ID:
                return TrailersContract.CONTENT_ITEM_TYPE;
            case REVIEWS:
                return ReviewsContract.CONTENT_TYPE;
            case REVIEWS_ID:
                return ReviewsContract.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case MOVIE_ID: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.TABLE_NAME,
                        projection,
                        MovieContract.MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                break;
            }
            case FAVOURITES: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FavoritesContract.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case FAVOURITES_ID: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FavoritesContract.TABLE_NAME,
                        projection,
                        FavoritesContract.MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                break;
            }
            case TRAILERS: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        TrailersContract.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case TRAILERS_ID: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        TrailersContract.TABLE_NAME,
                        projection,
                        TrailersContract.MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                break;
            }
            case REVIEWS: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ReviewsContract.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case REVIEWS_ID: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ReviewsContract.TABLE_NAME,
                        projection,
                        ReviewsContract.MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIE: {
                long _id = db.insert(MovieContract.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieContract.buildMoviesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case FAVOURITES: {
                long _id = db.insert(FavoritesContract.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = FavoritesContract.buildMoviesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case TRAILERS: {
                long _id = db.insert(TrailersContract.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = TrailersContract.buildMoviesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case REVIEWS: {
                long _id = db.insert(ReviewsContract.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = ReviewsContract.buildMoviesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
            case MOVIE:
                rowsDeleted = db.delete(
                        MovieContract.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE_ID:
                rowsDeleted = db.delete(MovieContract.TABLE_NAME,
                        MovieContract.MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            case FAVOURITES:
                rowsDeleted = db.delete(
                        FavoritesContract.TABLE_NAME, selection, selectionArgs);
                break;
            case FAVOURITES_ID:
                rowsDeleted = db.delete(FavoritesContract.TABLE_NAME,
                        FavoritesContract.MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            case TRAILERS:
                rowsDeleted = db.delete(
                        TrailersContract.TABLE_NAME, selection, selectionArgs);
                break;
            case TRAILERS_ID:
                rowsDeleted = db.delete(TrailersContract.TABLE_NAME,
                        TrailersContract.MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            case REVIEWS:
                rowsDeleted = db.delete(
                        ReviewsContract.TABLE_NAME, selection, selectionArgs);
                break;
            case REVIEWS_ID:
                rowsDeleted = db.delete(ReviewsContract.TABLE_NAME,
                        ReviewsContract.MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MOVIE:
                rowsUpdated = db.update(MovieContract.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case MOVIE_ID: {
                rowsUpdated = db.update(MovieContract.TABLE_NAME,
                        values,
                        MovieContract.MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            }
            case FAVOURITES:
                rowsUpdated = db.update(FavoritesContract.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case FAVOURITES_ID: {
                rowsUpdated = db.update(FavoritesContract.TABLE_NAME,
                        values,
                        FavoritesContract.MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            }
            case TRAILERS:
                rowsUpdated = db.update(TrailersContract.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case TRAILERS_ID: {
                rowsUpdated = db.update(TrailersContract.TABLE_NAME,
                        values,
                        TrailersContract.MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            }
            case REVIEWS:
                rowsUpdated = db.update(ReviewsContract.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case REVIEWS_ID: {
                rowsUpdated = db.update(ReviewsContract.TABLE_NAME,
                        values,
                        ReviewsContract.MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            }

           default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount = 0;
        switch (match) {
            case MOVIE:{
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(MovieContract.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case FAVOURITES:{
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(FavoritesContract.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;}
            case TRAILERS:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(TrailersContract.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;

            case REVIEWS:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(ReviewsContract.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;

            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
