package com.rajarshisharma.popularmoviesstage1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.rajarshisharma.popularmoviesstage1.util.ApplicationHelperUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private MovieGridAdapter movieGridAdapter;
    private final String LOG_TAG = MainActivity.class.getName();

    List<Movie> movies = new ArrayList<Movie>();

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        movieGridAdapter = new MovieGridAdapter(getActivity(), movies);

        GridView gridView = (GridView) rootView.findViewById(R.id.movie_grid_view);
        gridView.setAdapter(movieGridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = movieGridAdapter.getItem(position);
                Intent intent =new Intent(getActivity(),DetailActivity.class);
                intent.putExtra(ApplicationConstants.EXTRA_OBJECT,movie);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(ApplicationHelperUtils.isNetworkAvailable(getActivity())) {
            new FetchMovieTask().execute();
        } else{
                Log.v(LOG_TAG,"No Internet connection");
                Toast.makeText(getActivity(),
                        "Cannot Contact Server. Please check your Internet Connection",
                        Toast.LENGTH_SHORT)
                        .show();

            }

        }

        private class FetchMovieTask extends AsyncTask<String, Void, List<Movie>> {

            private final String LOG_TAG = FetchMovieTask.class.getSimpleName();


            @Override
            protected List<Movie> doInBackground(String... params) {
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                String movieJSONData = null;

                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String sortOrder = sharedPref.getString(getString(R.string.pref_sort_key),
                        getString(R.string.pref_sort_default));

                final String BASE_URL = "http://api.themoviedb.org/3/movie/" + sortOrder + "?api_key=";
                final String API_KEY = BuildConfig.OPEN_MOVIEDB_API_KEY;

                try {

                    URL url = new URL(BASE_URL + API_KEY);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();


                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuilder builder = new StringBuilder();
                    if (inputStream == null) {
                        return null;
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line).append("\n");
                    }

                    if (builder.length() == 0) {
                        return null;
                    }

                    movieJSONData = builder.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (final IOException e) {
                            Log.e(LOG_TAG, "Error closing stream", e);
                        }
                    }
                }


            try {
                List<Movie> formattedData =getFormattedMovieData(movieJSONData);
                if(formattedData !=null )
                    return formattedData;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            if(movies != null) {
                movieGridAdapter.clear();
                movieGridAdapter.addAll(movies);
            }else{
                Toast.makeText(getActivity(),
                        "Movies List could not be updated as per preference. Please check Internet connection",
                        Toast.LENGTH_LONG)
                        .show();

            }
        }
        /**
         * Returns a list with Movie Objects to be displayed in the Grid view
         * @param movieData
         * @return
         * @throws JSONException
         */
        private List<Movie> getFormattedMovieData(String movieData) throws JSONException {
            if (movieData !=null) {
                JSONObject jsonObject = new JSONObject(movieData);
                JSONArray jsonArray = jsonObject.getJSONArray("results");

                List<Movie> movies = new ArrayList<Movie>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObj = jsonArray.getJSONObject(i);

                    Movie movie = new Movie(
                            (int) jsonObj.get("id"),
                            jsonObj.get("original_title").toString(),
                            (double) jsonObj.get("vote_average"),
                            jsonObj.get("poster_path").toString(),
                            jsonObj.get("overview").toString(),
                            jsonObj.get("release_date").toString()
                    );

                    movies.add(movie);
                }

                return movies;
            }
            else {
                return null;
            }
        }
    }
}
