package com.rajarshisharma.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rajarshi.Sharma on 3/16/2016.
 */
public class Movie implements Parcelable {

    //the following are the attributes for each movie
    private int id;
    private String originalTitle;
    private String avgRating;
    private String posterPath;
    private String overview;
    private String releaseDate;


    /**
     * Constructor
     * @param id
     * @param originalTitle
     * @param avgRating
     * @param posterPath
     * @param overview
     * @param releaseDate
     */
    public Movie(int id, String originalTitle, String avgRating, String posterPath, String overview, String releaseDate) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.avgRating = avgRating;
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }
    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getAvgRating() {
        return avgRating;
    }
    public void setAvgRating(String avgRating) {
        this.avgRating = avgRating;
    }

    public String getPosterPath() {
        return posterPath;
    }
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }
    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }


    protected Movie(Parcel in) {
        id = in.readInt();
        originalTitle = in.readString();
        avgRating = in.readString();
        posterPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(originalTitle);
        dest.writeString(this.avgRating);
        dest.writeString(this.posterPath);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
    }
}
