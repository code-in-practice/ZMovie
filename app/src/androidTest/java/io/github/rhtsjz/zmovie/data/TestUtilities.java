package io.github.rhtsjz.zmovie.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

import java.util.Map;
import java.util.Set;

/**
 * Created by smile on 1/20/16.
 */
public class TestUtilities extends AndroidTestCase{

    static void validateCursor(String error, Cursor cursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, cursor.moveToFirst());
        validateCurrentRecord(error, cursor, expectedValues);
        cursor.close();
    }

    static void validateCurrentRecord(String error,
                                      Cursor valueCursor, ContentValues expectedValues){
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for(Map.Entry<String, Object> entry: valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
            "' did not match the expected value '" +
            expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static ContentValues createMatrixMovieValues(){
        ContentValues testValues = new ContentValues();
        testValues.put(MovieContract.MovieEntry.COLUMN_TITLE,
                "黑客帝国2：重装上阵 The Matrix Reloaded");
        testValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH,
                "http://img6.douban.com/view/photo/photo/public/p905751402.jpg");
        return testValues;
    }
}
