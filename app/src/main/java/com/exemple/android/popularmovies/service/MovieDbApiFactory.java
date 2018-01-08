package com.exemple.android.popularmovies.service;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.exemple.android.popularmovies.R;
import com.exemple.android.popularmovies.model.PosterSize;

import java.io.IOException;
import java.util.Locale;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDbApiFactory {

    private static String TAG = MovieDbApiFactory.class.getSimpleName();

    private static String MOVIEDB_BASE_URL = "https://api.themoviedb.org/3/";

    private static String MOVIEDB_POSTER_BASE_URL = "http://image.tmdb.org/t/p/";

    private static String VIDEO_BASE_URL = "https://www.youtube.com/watch?v=";

    private static String API_KEY_PARAM = "api_key";
    private static String LANGUAGE_PARAM = "language";

    public static MovieDbService getMovieDbService(final Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MOVIEDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(createClienteWithApiKey(context))
                .build();

        return retrofit.create(MovieDbService.class);
    }

    public static String getPosterUrl(String posterPath, @PosterSize.Size String posterSize) {
        return MOVIEDB_POSTER_BASE_URL + posterSize + posterPath;
    }

    public static Uri buildVideoUri(String movieKey){
        String fullUrl = VIDEO_BASE_URL + movieKey;
        return Uri.parse(fullUrl);
    }

    private static String getSystemLocale() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = Resources.getSystem().getConfiguration().getLocales().get(0);
        } else {
            locale = Resources.getSystem().getConfiguration().locale;
        }
        return locale.toString().replace("_", "-");
    }

    /**
     * This method return an Cliente with fixed API Key and language on each request
     *
     * Get this code from
     * @link https://futurestud.io/tutorials/retrofit-2-how-to-add-query-parameters-to-every-request
     */
    private static OkHttpClient createClienteWithApiKey(final Context context){
        return new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                HttpUrl originalUrl = originalRequest.url();

                String apiKey = context.getString(R.string.moviedb_api_key);
                String systemLocale = getSystemLocale();

                HttpUrl newUrl = originalUrl.newBuilder()
                        .addQueryParameter(API_KEY_PARAM, apiKey)
                        .addQueryParameter(LANGUAGE_PARAM, systemLocale)
                        .build();

                Log.d(TAG, newUrl.toString());

                Request newRequest = originalRequest.newBuilder().url(newUrl).build();

                return chain.proceed(newRequest);
            }
        }).build();
    }
}
