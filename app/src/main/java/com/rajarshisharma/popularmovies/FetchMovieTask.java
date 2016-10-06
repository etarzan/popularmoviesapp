package com.rajarshisharma.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

import com.rajarshisharma.popularmovies.data.contracts.MovieContract;
import com.rajarshisharma.popularmovies.util.ApplicationHelperUtils;


public class FetchMovieTask extends AsyncTask<String, Void, Void> {

    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

    private final Context mContext;

    public FetchMovieTask(Context context) {
        mContext = context;
    }

    /**
     * Take the String representing the complete movie list in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     * <p>
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    private void getMovieDataFromJson(String movieJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String MOVIE_ID = "id";
        final String ORGLANG = "original_language";
        final String ORGTITLE = "original_title";
        final String OVER = "overview";
        final String RELDATE = "release_date";
        final String POSTERPATH = "poster_path";
        final String POPULARITY = "popularity";
        final String VOTAVG = "vote_average";
        final String TITLE = "title";
        final String BACKDROP_PATH = "backdrop_path";
        final String VOTE_COUNT = "vote_count";
        final String PAGE = "page";


        final String RESULT = "results";
        final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185";
        final String BACKDROP_BASE_URL = "http://image.tmdb.org/t/p/w500";

        try {
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(RESULT);

            String page = movieJson.getString(PAGE);

            Vector<ContentValues> cVVector = new Vector<ContentValues>(movieArray.length());

            String movie_id;
            String orgLang;
            String orgTitle;
            String overview;
            String relDate;
            String postURL;
            String popularity;
            String votAvg;
            String vote_count;
            String backdropURL;
            String title;

            for (int i = 0; i < movieArray.length(); i++) {



                JSONObject movieInfo = movieArray.getJSONObject(i);

                movie_id = movieInfo.getString(MOVIE_ID);
                orgLang = movieInfo.getString(ORGLANG);
                orgTitle = movieInfo.getString(ORGTITLE);
                overview = movieInfo.getString(OVER);
                relDate = movieInfo.getString(RELDATE);

                postURL = Uri.parse(POSTER_BASE_URL).buildUpon().
                        appendEncodedPath(movieInfo.getString(POSTERPATH)).build().toString();
                popularity = movieInfo.getString(POPULARITY);
                votAvg = movieInfo.getString(VOTAVG);
                vote_count = movieInfo.getString(VOTE_COUNT);
                title = movieInfo.getString(TITLE);

                backdropURL = Uri.parse(BACKDROP_BASE_URL).buildUpon().
                        appendEncodedPath(movieInfo.getString(BACKDROP_PATH)).build().toString();


                ContentValues movieValues = new ContentValues();


                movieValues.put(MovieContract.PAGE, page);
                movieValues.put(MovieContract.POSTER_PATH, postURL);
                movieValues.put(MovieContract.OVERVIEW, overview);
                movieValues.put(MovieContract.RELEASE_DATE, relDate);
                movieValues.put(MovieContract.MOVIE_ID, movie_id);
                movieValues.put(MovieContract.ORIGINAL_TITLE, orgTitle);
                movieValues.put(MovieContract.ORIGINAL_LANGUAGE, orgLang);
                movieValues.put(MovieContract.TITLE, title);
                movieValues.put(MovieContract.BACKDROP_PATH, backdropURL);
                movieValues.put(MovieContract.POPULARITY, popularity);
                movieValues.put(MovieContract.VOTE_COUNT, vote_count);
                movieValues.put(MovieContract.VOTE_AVERAGE, votAvg);
                movieValues.put(MovieContract.SORT_BY, ApplicationHelperUtils.getPreferredSorting(mContext));
                cVVector.add(movieValues);
            }
            int inserted = 0;
            // add to database
            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
               inserted = mContext.getContentResolver().bulkInsert(MovieContract.CONTENT_URI, cvArray);
            }
            Log.d(LOG_TAG, "FetchPopularMovie Task Complete. " + inserted + " Inserted");

        } catch (JSONException e) {
            //Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    @Override
    protected Void doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;

        try {
            // Construct the URL for the movieAPI query
            // Possible parameters are avaiable at OWM's forecast API page, at

            final String MOVIE_BASE_URL =
                    "http://api.themoviedb.org/3/discover/movie?";
            final String SORT_PARAM = "sort_by";
            final String APPID_PARAM = "api_key";
            final String PAGE_PARAM = "page";

            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_PARAM, params[0])
                    .appendQueryParameter(APPID_PARAM, BuildConfig.OPEN_MOVIEDB_API_KEY)
                    .appendQueryParameter(PAGE_PARAM, params[1])
                    .build();

            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            movieJsonStr = buffer.toString();
            getMovieDataFromJson(movieJsonStr);
        } catch (IOException e) {
            //Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the movie data, there's no point in attemping
            // to parse it.
            return null;
        } catch (JSONException e) {
            //Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    //Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        // This will only happen if there was an error getting or parsing the forecast.
        return null;
    }
}