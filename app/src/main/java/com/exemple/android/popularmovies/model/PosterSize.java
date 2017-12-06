package com.exemple.android.popularmovies.model;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class PosterSize {

    public static final String W92 = "w92";
    public static final String W154 = "w154";
    public static final String W185 = "w185";
    public static final String W342 = "w342";
    public static final String W500 = "w500";
    public static final String W780 = "w780";
    public static final String ORIGINAL = "original";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({W92, W154, W185, W342, W500, W780, ORIGINAL})
    public @interface Size{}
}
