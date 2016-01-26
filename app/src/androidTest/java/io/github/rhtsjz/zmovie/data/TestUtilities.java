package io.github.rhtsjz.zmovie.data;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import junit.framework.Test;

import java.util.Map;
import java.util.Set;

import io.github.rhtsjz.zmovie.utils.PollingCheck;

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
        testValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH,
                "/lIv1QinFqz4dlp5U4lQ6HaiskOZ.jpg");
        testValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW,
            "19岁少年安德鲁（迈尔斯·特勒 Miles Teller 饰）成长在单亲家庭，一心想成为顶级爵士乐鼓手。" +
            "某晚他在学校练习时被魔鬼导师弗莱彻（J·K·西蒙斯 J.K. Simmons 饰）相中，进入正规乐队，" +
            "同时也开始为追求完美付出代价。安德鲁越是刻苦练习，与外部世界越是隔膜。" +
            "唯一理解他的是弗莱彻，但后者的暴躁与喜怒无常扭曲了这段师生关系，" +
            "更让安德鲁耳濡目染，连带自身的性格亦发生变化。" +
            "最后当安德鲁终于登上纽约音乐厅的舞台，他才惊恐的发现原来弗莱彻一直等着将他打入尘埃...... " +
            "《爆裂鼓手》讲述一名少年在严师督教下，以非常规手段挑战自己的极限、追逐爵士乐鼓手梦的热血故事。" +
            "主人公热爱打鼓，但过度的投入让他失去对音乐的初衷，进而演变为生命的负荷以及师徒间近乎疯魔的对决。" +
            "电影不只有音乐人的苦痛，更让人看到传统励志背后的残酷真相。 " +
            "《爆裂鼓手》获得第30届圣丹斯电影节最高荣誉评审团奖。");

        testValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, "2014-10-10");
        testValues.put(MovieContract.MovieEntry.COLUMN_ID, 244786);
        testValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, "Whiplash");
        testValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE, "en");
        testValues.put(MovieContract.MovieEntry.COLUMN_TITLE, "爆裂鼓手");
        testValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH,
            "/6bbZ6XyvgfjhQwbplnUh1LSj1ky.jpg");
        testValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, 9.640308);
        testValues.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, 1574);
        testValues.put(MovieContract.MovieEntry.COLUMN_VIDEO, 0);
        testValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, 8.35);

        return testValues;
    }

    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHandlerThread;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver(){
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHandlerThread = ht;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail(){
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();

            mHandlerThread.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }


}
