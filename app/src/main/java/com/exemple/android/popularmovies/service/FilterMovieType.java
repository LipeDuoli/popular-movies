package com.exemple.android.popularmovies.service;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class FilterMovieType {

    public static final int POPULAR = 2;
    public static final int TOP_RATED = 4;
    public static final int FAVORIRED = 8;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({POPULAR, TOP_RATED, FAVORIRED})
    public @interface MovieType{}

}
