package io.github.rhtsjz.zmovie.data;

import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

/**
 * Created by zsj on 16-1-18.
 */
public class TestMovieContract extends AndroidTestCase {
    private static String LOG_TAG = TestMovieContract.class.getSimpleName();
    private static final long TEST_ID = 1;

    public void testMovie(){
        Uri movieUri = MovieContract.MovieEntry.buildMovieUri(TEST_ID);
        assertNotNull("Error: Null Uri returned.", movieUri);

        Log.d(LOG_TAG, movieUri.toString());

        String movieId = movieUri.getLastPathSegment();

        assertEquals("Error: movie ID not properly appended to the end of the Uri",
                TEST_ID+"", movieUri.getLastPathSegment());

        assertEquals("Error: movie Uri doesn't match our expected result",
                movieUri.toString(),
                "content://io.github.rhtsjz.zmovie/movie/1");
        Log.d(LOG_TAG, "test finished successfully.");
    }
}
