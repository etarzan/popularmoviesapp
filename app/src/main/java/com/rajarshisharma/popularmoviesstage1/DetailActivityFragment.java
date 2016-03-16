package com.rajarshisharma.popularmoviesstage1;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    Movie mMovie;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceStateBundle) {
        Intent incomingIntent = getActivity().getIntent();
        View detailView = inflater.inflate(R.layout.movie_detail_layout, container, false);
        if(incomingIntent != null && incomingIntent.hasExtra(ApplicationConstants.EXTRA_OBJECT)){
            mMovie =  incomingIntent.getParcelableExtra(ApplicationConstants.EXTRA_OBJECT);
            ((TextView)detailView.findViewById(R.id.movie_title)).setText(mMovie.getOriginalTitle());
            ((TextView)detailView.findViewById(R.id.relase_date)).setText(mMovie.getReleaseDate());
            ((TextView)detailView.findViewById(R.id.vote_average)).setText(Double.toString(mMovie.getAvgRating()) + "/10");
            ((TextView)detailView.findViewById(R.id.movie_desc)).setText(mMovie.getOverview());

            String size = "w185"; //"w92", "w154", "w185", "w342", "w500", "w780", or "original"
            String poster_path = ApplicationConstants.POSTER_URL_BASE + size + mMovie.getPosterPath();
            ImageView imageView = (ImageView) detailView.findViewById(R.id.movie_poster_detail);
            Picasso.with(getContext()).load(poster_path).into((ImageView) imageView);
        }
        return detailView;
    }
}
