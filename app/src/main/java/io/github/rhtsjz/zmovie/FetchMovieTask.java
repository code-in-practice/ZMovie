package io.github.rhtsjz.zmovie;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import io.github.rhtsjz.zmovie.data.MovieContract;

/**
 * Created by smile on 1/23/16.
 */
public class FetchMovieTask extends AsyncTask<String, Void, Void> {

    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

    private final Context mContext;

    public FetchMovieTask(Context mContext) {
        this.mContext = mContext;
    }

    private void getMovieDataFromJson (String movieJsonStr) throws JSONException {
        final String POSTER_PATH = "poster_path";
        final String OVERVIEW = "overview";
        final String RELEASE_DATE = "release_date";
        final String ORIGINAL_TITLE = "original_title";
        final String ORIGINAL_LANGUAGE = "original_language";
        final String ID = "id";
        final String TITLE = "title";
        final String BACKDROP_PATH = "backdrop_path";
        final String POPULARITY = "popularity";
        final String VOTE_COUNT = "vote_count";
        final String VIDEO = "video";
        final String VOTE_AVERAGE = "vote_average";

        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = movieJson.getJSONArray("results");
        Vector<ContentValues> contentValuesVector = new Vector<>(movieArray.length());
        for (int i=0; i<movieArray.length(); i++) {
            JSONObject movieObject = movieArray.getJSONObject(i);
            String poster_path = movieObject.getString(TITLE);
            String overview = movieObject.getString(OVERVIEW);
            String release_date = movieObject.getString(RELEASE_DATE);
            String original_title = movieObject.getString(ORIGINAL_TITLE);
            String original_language = movieObject.getString(ORIGINAL_LANGUAGE);
            int id = movieObject.getInt(ID);
            String title = movieObject.getString(TITLE);
            String backdrop_path = movieObject.getString(BACKDROP_PATH);
            double popularity = movieObject.getDouble(POPULARITY);
            int vote_count = movieObject.getInt(VOTE_COUNT);
            boolean video = movieObject.getBoolean(VIDEO);
            double vote_average = movieObject.getDouble(VOTE_AVERAGE);

            ContentValues contentValues = new ContentValues();
            contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, poster_path);
            contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, overview);
            contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, release_date);
            contentValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, original_title);
            contentValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE, original_language);
            contentValues.put(MovieContract.MovieEntry.COLUMN_ID, id);
            contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, title);
            contentValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, backdrop_path);
            contentValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, popularity);
            contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, vote_count);
            contentValues.put(MovieContract.MovieEntry.COLUMN_VIDEO, video);
            contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, vote_average);

            contentValuesVector.add(contentValues);
        }

        int inserted = 0;
        if(contentValuesVector.size() >0) {
            ContentValues[] contentValuesArray = new ContentValues[contentValuesVector.size()];
            contentValuesVector.toArray(contentValuesArray);

            int deleted =  mContext.getContentResolver().delete(
                    MovieContract.MovieEntry.CONTENT_URI,null, null);
            Log.d(LOG_TAG, "FetchMovieTask before Complete. " + deleted + " Deleted");
            inserted = mContext.getContentResolver().bulkInsert(
                    MovieContract.MovieEntry.CONTENT_URI, contentValuesArray);
        }
        Log.d(LOG_TAG, "FetchMovieTask Complete. " + inserted + " Inserted");

    }

    @Override
    protected Void doInBackground(String... params) {

        try {

            final String base_url = "http://api.themoviedb.org/3/movie/top_rated?";
            final String api_key = "32508c30b60f00a312e76658177130a1";
            Uri builtUri = Uri.parse(base_url).buildUpon()
                    .appendQueryParameter("api_key", api_key)
                    .appendQueryParameter("language", "zh")
                    .build();

            URL url = new URL(builtUri.toString());

            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream to a string
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if(inputStream == null) {
                return  null;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ( (line=reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if(buffer.length() == 0) {
                return null;
            }

            String movieJsonStr = buffer.toString();
            getMovieDataFromJson(movieJsonStr);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }
}
