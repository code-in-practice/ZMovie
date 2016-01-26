package io.github.rhtsjz.zmovie;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by smile on 1/23/16.
 */
public class MovieAdapter extends CursorAdapter{

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    public MovieAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    /*
        Remember that these views are reused as needed.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.list_item_movie, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String title = cursor.getString(MovieFragment.COL_MOVIE_TITLE);
        TextView textView = (TextView) view.findViewById(R.id.list_item_movie_title_textview);
        textView.setText(title);

        ImageView imageView = (ImageView) view.findViewById(R.id.list_item_movie_poster_imageview);

        String poster_path = cursor.getString(MovieFragment.COL_BACKDROP_PATH);
        String url_base = "http://image.tmdb.org/t/p/w500";
        String poster_url = poster_path;
        if(poster_url.startsWith("/")){
            poster_url = url_base + poster_url;
        }
        Log.d(LOG_TAG, "poster_url: " + poster_url);
        Picasso.with(context).load(poster_url).into(imageView);
    }

}
