package com.rajarshisharma.popularmovies.activity.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rajarshisharma.popularmovies.task.FetchMovieTask;
import com.rajarshisharma.popularmovies.model.Movie;
import com.rajarshisharma.popularmovies.adapter.MovieGridAdapter;
import com.rajarshisharma.popularmovies.R;
import com.rajarshisharma.popularmovies.data.contracts.FavoritesContract;
import com.rajarshisharma.popularmovies.data.contracts.MovieContract;
import com.rajarshisharma.popularmovies.util.ApplicationHelperUtils;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    private static final String SELECTED_KEY = "selected_position";
    private static final int MOVIE_LOADER = 0;
    private static final String[] MOVIE_COLUMNS = {

            MovieContract.TABLE_NAME + "." + MovieContract._ID,
            MovieContract.PAGE,
            MovieContract.POSTER_PATH,
            MovieContract.OVERVIEW,
            MovieContract.RELEASE_DATE,
            MovieContract.MOVIE_ID,
            MovieContract.ORIGINAL_TITLE,
            MovieContract.ORIGINAL_LANGUAGE,
            MovieContract.TITLE,
            MovieContract.BACKDROP_PATH,
            MovieContract.POPULARITY,
            MovieContract.VOTE_COUNT,
            MovieContract.VOTE_AVERAGE,
            MovieContract.FAVOURED
    };
    private static final String[] FAVOURITE_MOVIE_COLUMNS = {

            FavoritesContract.TABLE_NAME + "." + FavoritesContract._ID,
            FavoritesContract.PAGE,
            FavoritesContract.POSTER_PATH,
            FavoritesContract.OVERVIEW,
            FavoritesContract.RELEASE_DATE,
            FavoritesContract.MOVIE_ID,
            FavoritesContract.ORIGINAL_TITLE,
            FavoritesContract.ORIGINAL_LANGUAGE,
            FavoritesContract.TITLE,
            FavoritesContract.BACKDROP_PATH,
            FavoritesContract.POPULARITY,
            FavoritesContract.VOTE_COUNT,
            FavoritesContract.VOTE_AVERAGE,
            FavoritesContract.FAVOURED
    };
    public static final int COL_POSTER_PATH = 2;
    public static final int COL_RELEASE_DATE = 5;
    public static final int COL_VOTE_AVERAGE =12 ;
    public static final int COL_POPULARITY = 11;
    /*   public static int COL_ID = 0;
       public static int COL_PAGE = 1;
       public static int COL_POSTER_PATH = 2;
       public static int COL_ADULT = 3;
       public static int COL_OVERVIEW = 4;

       public static int COL_RELEASE_DATE = 5;*/
    public static int COL_MOVIE_ID = 5;
    /* public static int COL_ORIGINAL_TITLE = 7;
     public static int COL_ORIGINAL_LANG = 8;
     public static int COL_TITLE = 9;
     public static int COL_BACKDROP_PATH = 10;
     public static int COL_POPULARITY = 11;
     public static int COL_VOTE_COUNT = 12;
     public static int COL_VOTE_AVERAGE = 13;
     public static int COL_FAVOURED = 14;
 */
    private static boolean firstTime = true;
    private MovieGridAdapter movieGridAdapter;
    private int mPosition = ListView.INVALID_POSITION;
    private GridView gridView;
    private ArrayList<Movie> movies;
    //private static final int MAX_PAGE=100;
    private int PAGE_LOADED = 0;
    private View rootView;

    public MainActivityFragment() {

    }

    private void updateMovieList() {
        FetchMovieTask fetchMovieTask = new FetchMovieTask(getActivity());
        String sortingOrder = ApplicationHelperUtils.getPreferredSorting(getActivity());
        if (!sortingOrder.equalsIgnoreCase(getResources().getString(R.string.pref_sort_favourite))) {

            fetchMovieTask.execute(sortingOrder, String.valueOf(PAGE_LOADED + 1));

        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", movies);

        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (firstTime == true) {
            if (!ApplicationHelperUtils.isNetworkAvailable(getActivity())) {
                Toast.makeText(getContext(), "Network Not Available!", Toast.LENGTH_LONG).show();
            }
            updateMovieList();
            ContentValues movieValues = new ContentValues();
            movieValues.put(FavoritesContract.PAGE, "0");
            movieValues.put(FavoritesContract.POSTER_PATH, "0");
            movieValues.put(FavoritesContract.OVERVIEW, "0");
            movieValues.put(FavoritesContract.RELEASE_DATE, "0");
            movieValues.put(FavoritesContract.MOVIE_ID, "0");
            movieValues.put(FavoritesContract.ORIGINAL_TITLE, "0");
            movieValues.put(FavoritesContract.ORIGINAL_LANGUAGE, "0");
            movieValues.put(FavoritesContract.TITLE, "0");
            movieValues.put(FavoritesContract.BACKDROP_PATH, "0");
            movieValues.put(FavoritesContract.POPULARITY, "0");
            movieValues.put(FavoritesContract.VOTE_COUNT, "0");
            movieValues.put(FavoritesContract.VOTE_AVERAGE, "0");
            movieValues.put(FavoritesContract.SORT_BY, "0");
            getActivity().getContentResolver().insert(FavoritesContract.buildMovieUri(), movieValues);
            firstTime = !firstTime;
        }

        if (savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            movies = new ArrayList<Movie>();
        } else {
            movies = savedInstanceState.getParcelableArrayList("movies");
        }
    }

    @Override
    public void onResume() {
        super.onStart();
        String sorting = ApplicationHelperUtils.getPreferredSorting(getActivity());
        updateMovieList();
        getLoaderManager().restartLoader(MOVIE_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // The CursorAdapter will take data from a source and
        // use it to populate the grid it's attached to.
        movieGridAdapter =
                new MovieGridAdapter(
                        getActivity(), null, 0);
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) rootView.findViewById(R.id.movie_grid_view);
        gridView.setAdapter(movieGridAdapter);


        gridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                final int size = gridView.getWidth();
                int numCol = (int) Math.round((double) size / (double) getResources().getDimensionPixelSize(R.dimen.movie_poster_width));
                gridView.setNumColumns(numCol);
            }
        });

        gridView.setOnItemClickListener(this);

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {

            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }



        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

//    void onSortingChanged() {
//        String sorting = ApplicationHelperUtils.getPreferredSorting(getActivity());
//        updateMovieList();
//        getLoaderManager().restartLoader(MOVIE_LOADER, null, this);
//    }
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String sortOrder = MovieContract._ID + " ASC";
        Uri movie = MovieContract.buildMovieUri();
        Uri fav = FavoritesContract.buildMovieUri();
        String sorting = ApplicationHelperUtils.getPreferredSorting(getActivity());
        if (sorting.equalsIgnoreCase(getResources().getString(R.string.pref_sort_favourite))) {
            return new CursorLoader(getActivity(),
                    fav,
                    FAVOURITE_MOVIE_COLUMNS,
                    FavoritesContract.FAVOURED + " = ?",
                    new String[]{"1"},
                    sortOrder);
        }
        return new CursorLoader(getActivity(),
                movie,
                MOVIE_COLUMNS,
                MovieContract.SORT_BY + " = ?",
                new String[]{sorting},
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        movieGridAdapter.swapCursor(cursor);
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            gridView.smoothScrollToPosition(mPosition);
        }
        try {
            TextView info = (TextView) rootView.findViewById(R.id.empty);
            if (movieGridAdapter.getCount() == 0) {
                String sorting = ApplicationHelperUtils.getPreferredSorting(getActivity());
                if (sorting.equalsIgnoreCase(getResources().getString(R.string.pref_sort_favourite))) {
                    info.setText("Favourite List is Empty!");
                }
                info.setVisibility(View.VISIBLE);
            } else {
                info.setVisibility(View.GONE);
            }
        } catch (Exception e) {
        }
        gridView.setAdapter(movieGridAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        movieGridAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null) {
                    ((Callback) getActivity())
                            .onItemSelected(cursor.getString(COL_MOVIE_ID));
                }
                mPosition = position;
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         *
         * @param movieUri
         */
        void onItemSelected(String movieUri);
    }

}
