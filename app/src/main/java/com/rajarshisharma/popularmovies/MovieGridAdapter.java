package com.rajarshisharma.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rajarshisharma.popularmovies.data.contracts.FavoritesContract;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


/**
 * Created by Rajarshi.Sharma on 3/16/2016.
 */
public class MovieGridAdapter extends CursorAdapter {



    public MovieGridAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_movie, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    /*
       This is where we fill-in the views with the contents of the cursor.
    */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final ViewHolder viewHolder = (ViewHolder) view.getTag();

        final String url = cursor.getString(MainActivityFragment.COL_POSTER_PATH);
        Picasso
                .with(context)
                .load(url)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .fit()
                .centerCrop()
                .into(viewHolder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        Picasso
                                .with(context)
                                .load(url)
                                .error(R.mipmap.ic_launcher)
                                .fit()
                                .centerCrop()
                                .into(viewHolder.imageView, new Callback() {
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
        viewHolder.imageView.setAdjustViewBounds(true);


        String date = cursor.getString(MainActivityFragment.COL_RELEASE_DATE);
        int pos = date.indexOf('-');
        viewHolder.year.setText(date.substring(0, pos >= 0 ? pos : 0));
        int fav = context.getContentResolver().query(
                FavoritesContract.buildMoviesUriWithMovieId(cursor.getString(MainActivityFragment.COL_MOVIE_ID)),
                null, null, null, null).getCount();
        if (fav == 1) {
            viewHolder.favIcon.setImageResource(R.drawable.ic_star_bookmark);
        } else {
            viewHolder.favIcon.setImageResource(R.drawable.ic_star_border_bookmark);
        }
        String rating = cursor.getString(MainActivityFragment.COL_VOTE_AVERAGE);
        double vote = Double.parseDouble(rating);
        rating = String.valueOf((double) Math.round(vote * 10d) / 10d);

        viewHolder.userRating.setText(rating + "/10");

        String popularity = cursor.getString(MainActivityFragment.COL_POPULARITY);
        pos = popularity.indexOf(".");
        viewHolder.pop_text.setText(popularity.substring(0, pos >= 0 ? pos : 0));
    }

    public static class ViewHolder {

        final ImageView imageView;
        final TextView year;
        final ImageView favIcon;
        final TextView userRating;
        final TextView pop_text;

        public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.grid_item_poster);
            year = (TextView) view.findViewById(R.id.year);
            favIcon = (ImageView) view.findViewById(R.id.vote_icon);
            userRating = (TextView) view.findViewById(R.id.vote_text);
            pop_text = (TextView) view.findViewById(R.id.pop_text);
        }
    }
}
