package io.github.rhtsjz.zmovie.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by smile on 1/20/16.
 */

public class TestUriMather extends AndroidTestCase {
    /*
     Note that this class utilizes
     constants that are declared with package protection inside of the UriMatcher, which is why
     the test must be in the same data package as the Android app code.  Doing the test this way is
     a nice compromise between data hiding and testability.
     */

    private static final Uri TEST_MOVIE_DIR = MovieContract.MovieEntry.CONTENT_URI;


    public void testUriMatcher() {
        UriMatcher uriMatcher = MovieProvider.buildUriMatcher();

        assertEquals("Error: The MOVIE URI was matched incorrectly.",
                uriMatcher.match(TEST_MOVIE_DIR), MovieProvider.MOVIE);
    }
}
