package com.example.sqlitelab;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sqlitelab.FeedReaderContract.FeedEntry;

import java.util.ArrayList;
import java.util.List;

public class FeedDao {

    private final FeedReaderDbHelper dbHelper;

    public FeedDao(FeedReaderDbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public long insert(String title, String subtitle) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_TITLE, title);
        values.put(FeedEntry.COLUMN_NAME_SUBTITLE, subtitle);
        return db.insert(FeedEntry.TABLE_NAME, null, values);
    }

    public List<String> findAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                FeedEntry._ID,
                FeedEntry.COLUMN_NAME_TITLE,
                FeedEntry.COLUMN_NAME_SUBTITLE
        };
        String sortOrder = FeedEntry._ID + " ASC";

        List<String> rows = new ArrayList<>();
        try (Cursor cursor = db.query(FeedEntry.TABLE_NAME, projection,
                null, null, null, null, sortOrder)) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(FeedEntry._ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_TITLE));
                String subtitle = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_SUBTITLE));
                rows.add(id + " | " + title + " | " + subtitle);
            }
        }
        return rows;
    }

    public List<Long> findIdsByTitle(String title) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = { FeedEntry._ID };
        String selection = FeedEntry.COLUMN_NAME_TITLE + " = ?";
        String[] selectionArgs = { title };

        List<Long> ids = new ArrayList<>();
        try (Cursor cursor = db.query(FeedEntry.TABLE_NAME, projection,
                selection, selectionArgs, null, null, null)) {
            while (cursor.moveToNext()) {
                ids.add(cursor.getLong(cursor.getColumnIndexOrThrow(FeedEntry._ID)));
            }
        }
        return ids;
    }

    public int updateTitle(String oldTitle, String newTitle) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_TITLE, newTitle);
        String selection = FeedEntry.COLUMN_NAME_TITLE + " = ?";
        String[] selectionArgs = { oldTitle };
        return db.update(FeedEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    public int deleteByTitle(String title) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = FeedEntry.COLUMN_NAME_TITLE + " = ?";
        String[] selectionArgs = { title };
        return db.delete(FeedEntry.TABLE_NAME, selection, selectionArgs);
    }
}
