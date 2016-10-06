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
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
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


        int fav = context.getContentResolver().query(
                    FavoritesContract.buildMoviesUriWithMovieId(
                        cursor.getString(
                                MainActivityFragment.COL_MOVIE_ID)),
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



    }

    public final class ViewHolder {

        final ImageView imageView;
        final ImageView favIcon;
        final TextView userRating;

        public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.grid_item_poster);
            favIcon = (ImageView) view.findViewById(R.id.fav_icon);
            userRating = (TextView) view.findViewById(R.id.vote_text);
        }
    }
}
