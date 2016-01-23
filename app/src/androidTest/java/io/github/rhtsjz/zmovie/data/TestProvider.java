package io.github.rhtsjz.zmovie.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

/**
 * Created by smile on 1/22/16.
 */
public class TestProvider extends AndroidTestCase{

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    /*
       This helper function deletes all records from both database tables using the ContentProvider.
       It also queries the ContentProvider to make sure that the database has been successfully
       deleted
     */

    public void deleteAllRecordsFromProvider(){
        mContext.getContentResolver().delete(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Movie table during delete", 0, cursor.getCount());
        cursor.close();
    }


    private void deleteAllRecords() {
        deleteAllRecordsFromProvider();
    }

    // Since we want each test to start with a clean slate, run deleteAllRecords
    // in setUp (called by the test runner before each test).
    @Override
    public void setUp() throws Exception{
        super.setUp();
        deleteAllRecords();
    }

    /*
        This test checks to make sure that the content provider is registered correctly.
     */
    public void testProviderRegistry(){
        PackageManager packageManager = mContext.getPackageManager();

        // We define the component name based on the package name from the context and the
        // MovieProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                MovieProvider.class.getName());

        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = packageManager.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: MovieProvider registered with authority: " +
                    providerInfo.authority + " instead of authority: " +
                    MovieContract.CONTENT_AUTHORITY,
                    providerInfo.authority, MovieContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            // I guess the provider isn't registered correctly.
            assertTrue("Error: MovieProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    /*
        This test doesn't touch the database.  It verifies that the ContentProvider returns
        the correct type for each type of URI that it can handle.
     */
    public void testGetType(){
        // content://io.github.rhtsjz.zmovie/movie/
        String type = mContext.getContentResolver().getType(MovieContract.MovieEntry.CONTENT_URI);
        // vnd.android.cursor.dir/io.github.rhtsjz.zmovie/movie
        assertEquals("Error: the MovieEntry CONTENT_URI should return MovieEntry.CONTENT_TYPE",
                MovieContract.MovieEntry.CONTENT_TYPE, type);
    }

    /*
        This test uses the database directly to insert and then uses the ContentProvider to
        read out the data.
     */
    public void testBasicMovieQuery(){
        MovieDbHelper dbHelper = new MovieDbHelper(
                mContext,MovieDbHelper.DATABASE_NAME,null,MovieDbHelper.DATABASE_VERSION);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = TestUtilities.createMatrixMovieValues();
        long movieId = db.insert(
                MovieContract.MovieEntry.TABLE_NAME,
                null,
                contentValues
        );

        assertTrue("Unable to Insert MovieEntry into the Database", movieId != -1);
        db.close();

        // Test the basic content provider query
        Cursor cursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicMovieQuery", cursor, contentValues);
    }


    /*
        This test uses the provider to insert and then update the data.
     */
    public void testUpdateMovie(){
        // Create a new map of values, where column names are the keys
        ContentValues values = TestUtilities.createMatrixMovieValues();

        Uri movieUri = mContext.getContentResolver().insert(
                MovieContract.MovieEntry.CONTENT_URI, values);
        long movieId = ContentUris.parseId(movieUri);

        // Verify we got a row back.
        assertTrue(movieId != -1);
        Log.d(LOG_TAG, "New row id: " + movieId);

        ContentValues updateValues = new ContentValues(values);
        updateValues.put(MovieContract.MovieEntry._ID, movieId);
        updateValues.put(MovieContract.MovieEntry.COLUMN_TITLE, "黑客帝国 The Matrix");

        // Create a cursor with observer to make sure that the content provider is notifying
        // the observers as expected
        Cursor cursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        TestUtilities.TestContentObserver testContentObserver =
                TestUtilities.getTestContentObserver();
        cursor.registerContentObserver(testContentObserver);

        int count = mContext.getContentResolver().update(
                MovieContract.MovieEntry.CONTENT_URI,
                updateValues,
                MovieContract.MovieEntry._ID + "=?",
                new String[] {Long.toString(movieId)});
        assertEquals(count, 1);

        testContentObserver.waitForNotificationOrFail();

        cursor.unregisterContentObserver(testContentObserver);
        cursor.close();

        // A cursor is your primary interface to the query results.
        Cursor queryCursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                MovieContract.MovieEntry._ID + " = " + movieId,
                null,
                null
        );
        TestUtilities.validateCursor("testUpdateMovie. Error validating movie entry update.",
                queryCursor, updateValues);

        queryCursor.close();
    }

    public void testInsertReadProvider(){
        ContentValues testValues = TestUtilities.createMatrixMovieValues();

        // Register a content observer for our insert.  This time, directly with the content resolver
        TestUtilities.TestContentObserver tco =
                TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(
                MovieContract.MovieEntry.CONTENT_URI, true, tco);

        Uri movieUri = mContext.getContentResolver().insert(
                MovieContract.MovieEntry.CONTENT_URI,
                testValues);


        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long movieId = ContentUris.parseId(movieUri);

        assertTrue(movieId != -1);

        Cursor cursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating MovieEntry.",
                cursor, testValues);

    }

    public void testDeleteRecords(){
        testInsertReadProvider();

        // Register a content observer for our movie delete
        TestUtilities.TestContentObserver movieObserver =
                TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(
                MovieContract.MovieEntry.CONTENT_URI, true, movieObserver);

        deleteAllRecordsFromProvider();

        movieObserver.waitForNotificationOrFail();

        mContext.getContentResolver().unregisterContentObserver(movieObserver);

    }

    static final int BULK_INSERT_COUNT = 2;
    public void testBulkInsert(){
        ContentValues[] bulkInsertContentValues = new ContentValues[BULK_INSERT_COUNT];
        bulkInsertContentValues[0] = TestUtilities.createMatrixMovieValues();
        bulkInsertContentValues[1] = TestUtilities.createMatrixMovieValues();

        // Register a content observer for our bulk insert.
        TestUtilities.TestContentObserver movieObserver =
                TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(
                MovieContract.MovieEntry.CONTENT_URI, true, movieObserver);

        int insertCount = mContext.getContentResolver().bulkInsert(
                MovieContract.MovieEntry.CONTENT_URI,
                bulkInsertContentValues);
        movieObserver.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(movieObserver);

        assertEquals(insertCount, BULK_INSERT_COUNT);

        Cursor cursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                MovieContract.MovieEntry._ID + " DESC"
        );
        assertEquals(cursor.getCount(), BULK_INSERT_COUNT);

        cursor.moveToFirst();
        for (int i=0; i<BULK_INSERT_COUNT; i++, cursor.moveToNext()){
            TestUtilities.validateCurrentRecord("testBulkInsert. Error validating MovieEntry " + i,
                    cursor, bulkInsertContentValues[i]);
        }
    }
}
