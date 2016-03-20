package com.rajarshisharma.popularmoviesstage1;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Rajarshi.Sharma on 3/16/2016.
 */
public class MovieGridAdapter extends ArrayAdapter<Movie> {

    /**
     * Constructor passes on params to the Super class i.e. ArrayAdapter
     * @param activity
     * @param movies
     */
    public MovieGridAdapter(Activity activity, List<Movie> movies) {
        super(activity,0, movies);
    }

    /**
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Movie movie = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.grid_item, parent, false);
        }
        //selecting a larger size for use in the grid layout
        String size = "w342"; //"w92", "w154", "w185", "w342", "w500", "w780", or "original"
        String poster_path = ApplicationConstants.POSTER_URL_BASE + size + movie.getPosterPath();
        ImageView imageView = (ImageView) convertView.findViewById(R.id.movie_poster);
        Picasso.with(getContext()).load(poster_path).error(R.drawable.error).into((ImageView) imageView);

        return convertView;
    }

}
