package com.rajarshisharma.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.*;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rajarshisharma.popularmovies.data.contracts.FavoritesContract;
import com.rajarshisharma.popularmovies.data.contracts.MovieContract;
import com.rajarshisharma.popularmovies.data.contracts.ReviewsContract;
import com.rajarshisharma.popularmovies.data.contracts.TrailersContract;
import com.rajarshisharma.popularmovies.util.ApplicationHelperUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment  implements LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    private static final String MOVIE_SHARE_HASHTAG = " #PopularMovieApp";
    private static final int DETAIL_LOADER = 0;
    private static final int TRAILER_LOADER = 1;
    private static final int REVIEW_LOADER = 2;
    private static final int GENRE_LOADER = 3;
    private static final int FAVOURITE_LOADER = 4;
    private static final String[] MOVIE_COLUMNS = {

            MovieContract.TABLE_NAME + "." + MovieContract._ID,
            MovieContract.TABLE_NAME + "." + MovieContract.PAGE,
            MovieContract.TABLE_NAME + "." + MovieContract.POSTER_PATH,
            MovieContract.TABLE_NAME + "." + MovieContract.OVERVIEW,
            MovieContract.TABLE_NAME + "." + MovieContract.RELEASE_DATE,
            MovieContract.TABLE_NAME + "." + MovieContract.MOVIE_ID,
            MovieContract.TABLE_NAME + "." + MovieContract.ORIGINAL_TITLE,
            MovieContract.TABLE_NAME + "." + MovieContract.ORIGINAL_LANGUAGE,
            MovieContract.TABLE_NAME + "." + MovieContract.TITLE,
            MovieContract.TABLE_NAME + "." + MovieContract.BACKDROP_PATH,
            MovieContract.TABLE_NAME + "." + MovieContract.POPULARITY,
            MovieContract.TABLE_NAME + "." + MovieContract.VOTE_COUNT,
            MovieContract.TABLE_NAME + "." + MovieContract.VOTE_AVERAGE,
            MovieContract.TABLE_NAME + "." + MovieContract.FAVOURED,
            MovieContract.TABLE_NAME + "." + MovieContract.SHOWED,
            MovieContract.TABLE_NAME + "." + MovieContract.DOWNLOADED,
            MovieContract.TABLE_NAME + "." + MovieContract.SORT_BY

    };
    private static final String[] TRAILER_COLUMNS = {

            TrailersContract.TABLE_NAME + "." + TrailersContract._ID,
            TrailersContract.TABLE_NAME + "." + TrailersContract.NAME,
            TrailersContract.TABLE_NAME + "." + TrailersContract.SIZE,
            TrailersContract.TABLE_NAME + "." + TrailersContract.SOURCE,
            TrailersContract.TABLE_NAME + "." + TrailersContract.TYPE,
            TrailersContract.TABLE_NAME + "." + TrailersContract.MOVIE_ID
    };
    private static final String[] REVIEW_COLUMNS = {

            ReviewsContract.TABLE_NAME + "." +ReviewsContract._ID,
            ReviewsContract.TABLE_NAME + "." +ReviewsContract.PAGE,
            ReviewsContract.TABLE_NAME + "." +ReviewsContract.TOTAL_PAGE,
            ReviewsContract.TABLE_NAME + "." +ReviewsContract.TOTAL_RESULTS,
            ReviewsContract.TABLE_NAME + "." +ReviewsContract.ID_REVIEWS,
            ReviewsContract.TABLE_NAME + "." +ReviewsContract.AUTHOR,
            ReviewsContract.TABLE_NAME + "." +ReviewsContract.CONTENT,
            ReviewsContract.TABLE_NAME + "." +ReviewsContract.URL,
            ReviewsContract.TABLE_NAME + "." +ReviewsContract.MOVIE_ID

    };

    private static final String[] FAVORITE_MOVIE_COLUMNS = {

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
            FavoritesContract.FAVOURED,
            FavoritesContract.SHOWED,
            FavoritesContract.DOWNLOADED,
            FavoritesContract.SORT_BY
    };
    public static String trailerURIBase = "https://www.youtube.com/watch?v=";
    public static int COL_ID = 0;
    public static int COL_PAGE = 1;
    public static int COL_POSTER_PATH = 2;
    public static int COL_OVERVIEW = 3;
    public static int COL_RELEASE_DATE = 4;
    public static int COL_MOVIE_ID = 5;
    public static int COL_ORIGINAL_TITLE = 6;
    public static int COL_ORIGINAL_LANG = 7;
    public static int COL_TITLE = 8;
    public static int COL_BACKDROP_PATH = 9;
    public static int COL_POPULARITY = 10;
    public static int COL_VOTE_COUNT = 11;
    public static int COL_VOTE_AVERAGE = 12;
    public static int COL_FAVOURED = 13;
    public static int COL_SHOWN = 14;
    public static int COL_DOWNLOADED = 15;
    public static int COL_SORT_BY = 16;
    public static int COL_TRAILER_ID = 0;
    public static int COL_TRAILER_NAME = 1;
    public static int COL_TRAILER_SIZE = 2;
    public static int COL_TRAILER_SOURCE = 3;
    public static int COL_TRAILER_TYPE = 4;
    public static int COL_TRAILER_MOVIE_ID = 5;
    public static int COL_REVIEW_ID = 0;
    public static int COL_REVIEW_PAGE = 1;
    public static int COL_REVIEW_TOTAL_PAGE = 2;
    public static int COL_REVIEW_TOTAL_RESULTS = 3;
    public static int COL_REVIEW_ID_REVIEWS = 4;
    public static int COL_REVIEW_AUTHOR = 5;
    public static int COL_REVIEW_CONTENT = 6;
    public static int COL_REVIEW_URL = 7;
    public static int COL_REVIEW_MOVIE_ID = 8;
    public static int COL_GENRE_ID = 0;
    public static int COL_GENRE_NAME = 1;
    public static int COL_GENRE_ID_GENRE = 3;
    public static int COL_GENRE_MOVIE_ID = 4;
    private static String showed = "0";
    private static ContentValues movieValues;
    String orgLang;
    String orgTitle;
    String overview;
    String relDate;
    String postURL;
    String popularity;
    String votAvg;
    String favourite;
    String movieId;
    String backdropURL;
    String trailerName;
    String trailerSize;
    String trailerSource;
    String trailerType;
    String trailerID;
    private View rootView;
    private String movie_Id;
    private TrailerMovieAdapter trailerListAdapter;
    private ReviewMovieAdapter reviewListAdapter;
    private ListView listViewTrailers;
    private ListView listViewReviews;
    private ListView listViewGenres;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String genre = "Genre : ";

    public DetailActivityFragment() {
        setHasOptionsMenu(true);
        trailerURIBase = "https://www.youtube.com/watch?v=";
        movieValues = new ContentValues();
    }

    private void updateMovieList() {
        FetchTrailerReviewTask weatherTask = new FetchTrailerReviewTask(getActivity());
        weatherTask.execute(movie_Id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            movie_Id = arguments.getString(Intent.EXTRA_TEXT);
        }
        rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        // The ArrayAdapter will take data from a source and
        // use it to populate the ListView it's attached to.
        trailerListAdapter = new TrailerMovieAdapter(getActivity(), null, 0);
        listViewTrailers = (ListView) rootView.findViewById(R.id.listview_trailer);
        listViewTrailers.setAdapter(trailerListAdapter);

        reviewListAdapter = new ReviewMovieAdapter(getActivity(), null, 0);
        listViewReviews = (ListView) rootView.findViewById(R.id.listview_review);
        listViewReviews.setAdapter(reviewListAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.detail_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (ApplicationHelperUtils.isNetworkAvailable(getActivity())) {
                    getActivity().getContentResolver().delete(TrailersContract.buildMoviesUriWithMovieId(movie_Id), null, null);
                    getActivity().getContentResolver().delete(ReviewsContract.buildMoviesUriWithMovieId(movie_Id), null, null);
                    updateMovieList();
                    //Log.v(LOG_TAG, "refreshed");
                } else {
                    Toast.makeText(getContext(), "Network Not Available!", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        FloatingActionButton play = (FloatingActionButton) rootView.findViewById(R.id.play);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerURIBase));
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        getLoaderManager().initLoader(TRAILER_LOADER, null, this);
        getLoaderManager().initLoader(REVIEW_LOADER, null, this);
        getLoaderManager().initLoader(GENRE_LOADER, null, this);
        getLoaderManager().initLoader(FAVOURITE_LOADER, null, this);

        super.onActivityCreated(savedInstanceState);
    }

    void onSortingChanged() {
        String movieId = movie_Id;
        if (null != movieId) {
            getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
            getLoaderManager().restartLoader(TRAILER_LOADER, null, this);
            getLoaderManager().restartLoader(REVIEW_LOADER, null, this);
            getLoaderManager().restartLoader(GENRE_LOADER, null, this);
            getLoaderManager().restartLoader(FAVOURITE_LOADER, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (null != movie_Id) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            switch (id) {
                case DETAIL_LOADER:
                    String sorting = ApplicationHelperUtils.getPreferredSorting(getActivity());
                    if (sorting.equalsIgnoreCase(getResources().getString(R.string.pref_sort_favourite))) {
                        return new CursorLoader(getActivity(),
                                FavoritesContract.buildMovieUri(),
                                FAVORITE_MOVIE_COLUMNS,
                                FavoritesContract.MOVIE_ID + " = ?",
                                new String[]{movie_Id},
                                null);
                    }
                    return new CursorLoader(
                            getActivity(),
                            MovieContract.buildMoviesUriWithMovieId(movie_Id),
                            MOVIE_COLUMNS,
                            null,
                            null,
                            null
                    );
                case FAVOURITE_LOADER:
                    return new CursorLoader(
                            getActivity(),
                            FavoritesContract.buildMovieUri(),
                            FAVORITE_MOVIE_COLUMNS,
                            null,
                            null,
                            null
                    );
                case TRAILER_LOADER:
                    return new CursorLoader(
                            getActivity(),
                            TrailersContract.buildMoviesUriWithMovieId(movie_Id),
                            TRAILER_COLUMNS,
                            null,
                            null,
                            null
                    );
                case REVIEW_LOADER:
                    return new CursorLoader(
                            getActivity(),
                            ReviewsContract.buildMoviesUriWithMovieId(movie_Id),
                            REVIEW_COLUMNS,
                            null,
                            null,
                            null
                    );

            }
        }
        return null;
    }

    private void hide() {
        rootView.findViewById(R.id.TrailerText).setVisibility(View.GONE);
        rootView.findViewById(R.id.ReviewsText).setVisibility(View.GONE);
        rootView.findViewById(R.id.listview_trailer).setVisibility(View.GONE);
        rootView.findViewById(R.id.listview_review).setVisibility(View.GONE);
        rootView.findViewById(R.id.hide).setVisibility(View.GONE);
        rootView.findViewById(R.id.show).setVisibility(View.VISIBLE);
    }

    private void show() {
        rootView.findViewById(R.id.TrailerText).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.ReviewsText).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.listview_trailer).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.listview_review).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.hide).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.show).setVisibility(View.GONE);
    }

    private void defaultShow() {
        rootView.findViewById(R.id.divisor).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.share).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.play).setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor data) {
        swipeRefreshLayout.setRefreshing(false);
        //Log.v(LOG_TAG, "In onLoadFinished");
        if (!data.moveToFirst()) {
            return;
        }
        switch (loader.getId()) {
            case DETAIL_LOADER:
                defaultShow();
                orgLang = data.getString(COL_ORIGINAL_LANG);

                orgTitle = data.getString(COL_ORIGINAL_TITLE);
                ((TextView) rootView.findViewById(R.id.orgTitle))
                        .setText(orgTitle);

                overview = data.getString(COL_OVERVIEW);
                ((TextView) rootView.findViewById(R.id.overview))
                        .setText(overview);

                relDate = data.getString(COL_RELEASE_DATE);

                ((TextView) rootView.findViewById(R.id.relDate))
                        .setText("Release Date: " + relDate);

                postURL = data.getString(COL_POSTER_PATH);
                ImageView poster = (ImageView) rootView.findViewById(R.id.poster);
                Picasso
                        .with(getActivity())
                        .load(postURL)
                        .fit()
                        .into(poster);

                movieId = data.getString(COL_MOVIE_ID);
                popularity = data.getString(COL_POPULARITY);
                double pop = Double.parseDouble(popularity);
                popularity = String.valueOf((double) Math.round(pop * 10d) / 10d);

                ((TextView) rootView.findViewById(R.id.popularity))
                        .setText("Popularity : " + popularity);

                votAvg = data.getString(COL_VOTE_AVERAGE);
                double vote = Double.parseDouble(votAvg);
                votAvg = String.valueOf((double) Math.round(vote * 10d) / 10d);
                ((TextView) rootView.findViewById(R.id.vote))
                        .setText(votAvg);
                backdropURL = data.getString(COL_BACKDROP_PATH);
                final ImageView backdrop = (ImageView) rootView.findViewById(R.id.backdropImg);
                Picasso
                        .with(getActivity())
                        .load(backdropURL)
                        .fit()
                        .centerCrop()
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(backdrop, new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError() {
                                Picasso
                                        .with(getActivity())
                                        .load(backdropURL)
                                        .fit()
                                        .centerCrop()
                                        .error(R.mipmap.ic_launcher)
                                        .into(backdrop, new Callback() {
                                            @Override
                                            public void onSuccess() {
                                            }

                                            @Override
                                            public void onError() {
                                                //Log.v("Error Loading Images", "'");
                                            }
                                        });
                            }
                        });
                backdrop.setAdjustViewBounds(true);

                final String downloaded = data.getString(COL_DOWNLOADED);
                showed = data.getString(COL_SHOWN);
                FloatingActionButton show;
                if (showed.equalsIgnoreCase("1")) {
                    rootView.findViewById(R.id.hide).setVisibility(View.VISIBLE);
                    show = (FloatingActionButton) rootView.findViewById(R.id.hide);
                    show();
                } else {
                    rootView.findViewById(R.id.show).setVisibility(View.VISIBLE);
                    show = (FloatingActionButton) rootView.findViewById(R.id.show);
                    hide();
                }
                show.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ContentValues sh = new ContentValues();
                        if (showed.equalsIgnoreCase("1")) {
                            sh.put(MovieContract.SHOWED, "0");
                            Toast.makeText(getContext(), "Hiding Trailers and Reviews", Toast.LENGTH_SHORT).show();
                            hide();
                        } else {
                            sh.put(MovieContract.SHOWED, "1");
                            if (downloaded.equalsIgnoreCase("0")) {
                                if (ApplicationHelperUtils.isNetworkAvailable(getActivity())) {
                                    updateMovieList();
                                    sh.put(MovieContract.DOWNLOADED, "1");
                                    Toast.makeText(getContext(), "Showing Trailers and review", Toast.LENGTH_SHORT).show();
                                    show();
                                } else {
                                    Toast.makeText(getContext(), "Please Check your connectivity", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                        getContext().getContentResolver().update(
                                MovieContract.CONTENT_URI.buildUpon().appendPath(movieId).build(),
                                sh, null, new String[]{movieId});
                    }
                });

                FloatingActionButton play = (FloatingActionButton) rootView.findViewById(R.id.play);

                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (downloaded.equalsIgnoreCase("0")) {
                            ContentValues sh = new ContentValues();
                            if (ApplicationHelperUtils.isNetworkAvailable(getActivity())) {
                                updateMovieList();
                                sh.put(MovieContract.DOWNLOADED, "1");
                                getContext().getContentResolver().update(
                                        MovieContract.CONTENT_URI.buildUpon().appendPath(movieId).build(),
                                        sh, null, new String[]{movieId});
                                Toast.makeText(getContext(), "Click One More Time to Play", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Please Check your connectivity", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerURIBase));
                            startActivity(intent);
                        }
                    }
                });
                FloatingActionButton share = (FloatingActionButton) rootView.findViewById(R.id.share);
                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (downloaded.equalsIgnoreCase("0")) {
                            ContentValues sh = new ContentValues();
                            if (ApplicationHelperUtils.isNetworkAvailable(getActivity())) {
                                updateMovieList();
                                sh.put(MovieContract.DOWNLOADED, "1");
                                getContext().getContentResolver().update(
                                        MovieContract.CONTENT_URI.buildUpon().appendPath(movieId).build(),
                                        sh, null, new String[]{movieId});
                                Toast.makeText(getContext(), "Click One More Time to Share", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Please Check your connectivity", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_TEXT, "Check out this movie called "+
                                    orgTitle + " on YouTube at: " + trailerURIBase + MOVIE_SHARE_HASHTAG);
                            intent.setType("text/plain");
                            startActivity(Intent.createChooser(intent, getString(R.string.share_links)));
                        }
                    }
                });
                if (movieValues.size() == 0) {
                    movieValues.put(MovieContract.PAGE, data.getString(COL_PAGE));
                    movieValues.put(MovieContract.POSTER_PATH, postURL);
                    movieValues.put(MovieContract.OVERVIEW, overview);
                    movieValues.put(MovieContract.RELEASE_DATE, relDate);
                    movieValues.put(MovieContract.MOVIE_ID, movie_Id);
                    movieValues.put(MovieContract.ORIGINAL_TITLE, orgTitle);
                    movieValues.put(MovieContract.ORIGINAL_LANGUAGE, orgLang);
                    movieValues.put(MovieContract.TITLE, data.getString(COL_TITLE));
                    movieValues.put(MovieContract.BACKDROP_PATH, backdropURL);
                    movieValues.put(MovieContract.POPULARITY, popularity);
                    movieValues.put(MovieContract.VOTE_COUNT, data.getString(COL_VOTE_COUNT));
                    movieValues.put(MovieContract.VOTE_AVERAGE, votAvg);
                    movieValues.put(MovieContract.FAVOURED, "1");
                    movieValues.put(MovieContract.SHOWED, data.getString(COL_SHOWN));
                    movieValues.put(MovieContract.DOWNLOADED, data.getString(COL_DOWNLOADED));
                    movieValues.put(MovieContract.SORT_BY, data.getString(COL_SORT_BY));
                }
                break;
            case FAVOURITE_LOADER:
                FloatingActionButton fab;
                boolean favorite = false;
                if (data.moveToFirst()) {
                    do {
                        if (data.getString(COL_MOVIE_ID).equalsIgnoreCase(movie_Id)) {
                            favorite = true;
                        }
                    }
                    while (data.moveToNext());
                }
                if (favorite) {
                    rootView.findViewById(R.id.bookmark).setVisibility(View.VISIBLE);
                    rootView.findViewById(R.id.border_bookmark).setVisibility(View.GONE);
                    fab = (FloatingActionButton) rootView.findViewById(R.id.bookmark);
                } else {
                    rootView.findViewById(R.id.border_bookmark).setVisibility(View.VISIBLE);
                    rootView.findViewById(R.id.bookmark).setVisibility(View.GONE);
                    fab = (FloatingActionButton) rootView.findViewById(R.id.border_bookmark);
                }
                final boolean finalFavoured = favorite;
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (finalFavoured) {
                            getActivity().getContentResolver().delete(FavoritesContract.buildMoviesUriWithMovieId(movie_Id), null, null);
                            Toast.makeText(getContext(), "Removed from favorites list", Toast.LENGTH_SHORT).show();
                            rootView.findViewById(R.id.bookmark).setVisibility(View.GONE);
                            rootView.findViewById(R.id.border_bookmark).setVisibility(View.VISIBLE);
                        } else {
                            getActivity().getContentResolver().insert(FavoritesContract.buildMovieUri(), movieValues);
                            Toast.makeText(getContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
                            rootView.findViewById(R.id.bookmark).setVisibility(View.VISIBLE);
                            rootView.findViewById(R.id.border_bookmark).setVisibility(View.GONE);
                        }
                    }
                });
                break;
            case TRAILER_LOADER:
                int iter = 0;
                if (data.moveToFirst()) {
                    do {
                        trailerListAdapter.swapCursor(data);
                        iter++;
                        if (iter == 1) {
                            trailerURIBase += data.getString(DetailActivityFragment.COL_TRAILER_SOURCE);
                        }
                    }
                    while (data.moveToNext());
                }
                break;
            case REVIEW_LOADER:
                //Log.e(LOG_TAG, "Review");
                if (data.moveToFirst()) {
                    do {
                        reviewListAdapter.swapCursor(data);
                    }
                    while (data.moveToNext());
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown Loader");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        trailerListAdapter.swapCursor(null);
        reviewListAdapter.swapCursor(null);

    }


}
