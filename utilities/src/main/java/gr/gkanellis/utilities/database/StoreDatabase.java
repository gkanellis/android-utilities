package gr.gkanellis.utilities.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Closeable;
import java.util.Locale;


public class StoreDatabase implements Closeable {

    private static final String TAG = "StoreDatabase";

    private static final String FIELD_KEY = "pair_key";
    private static final String FIELD_VALUE = "pair_value";
    private static final String CREATE_TABLE_PATTERN = "CREATE TABLE IF NOT EXISTS %s " +
            "(%s TEXT NOT NULL, %s TEXT DEFAULT NULL, PRIMARY KEY(%s));";
    private static final String DATABASE_NAME = "stores.db";

    private static StoreDatabase sInstance;

    private SQLiteDatabase mSqLiteDatabase;

    private StoreDatabase(@NonNull SQLiteDatabase database) {
        mSqLiteDatabase = database;
    }

    public boolean create(@NonNull String table) {
        try {
            mSqLiteDatabase.execSQL(String.format(Locale.ENGLISH,
                    CREATE_TABLE_PATTERN, table, FIELD_KEY, FIELD_VALUE, FIELD_KEY));
        } catch (SQLiteException e) {
            Log.e(TAG, "Failed to create database table", e);
            return false;
        }
        return true;
    }

    public boolean clear(@NonNull String table) {
        int rows = mSqLiteDatabase.delete(table, null, null);
        return rows > 0;
    }

    public boolean remove(@NonNull String table, @NonNull String key) {
        int id = mSqLiteDatabase.delete(table, FIELD_KEY + "=?", new String[]{key});
        return id > 0;
    }

    public boolean contains(@NonNull String table, @NonNull String key) {
        String value = getValue(table, key);
        return value != null;
    }

    public String getValue(@NonNull String table, @NonNull String key) {
        try {
            try (Cursor cursor = mSqLiteDatabase.query(table, new String[]{FIELD_VALUE},
                    FIELD_KEY + "=?", new String[]{key}, null, null, null)) {
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(FIELD_VALUE);
                    return cursor.getString(columnIndex);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to get value from database", e);
        }
        return null;
    }

    public boolean putValue(@NonNull String table, @NonNull String key, @Nullable Object value) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_KEY, key);
        if (value == null) {
            contentValues.putNull(FIELD_VALUE);
        } else {
            contentValues.put(FIELD_VALUE, String.valueOf(value));
        }
        long id = mSqLiteDatabase.insertWithOnConflict(table, null, contentValues,
                SQLiteDatabase.CONFLICT_REPLACE);
        return id > 0;
    }

    @Override
    public void close() {
        mSqLiteDatabase.close();
    }

    public static StoreDatabase getInstance(@NonNull Context context) {
        if (sInstance == null) {
            synchronized (StoreDatabase.class) {
                try {
                    SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase(DATABASE_NAME,
                            Context.MODE_PRIVATE, null);
                    sInstance = new StoreDatabase(sqLiteDatabase);
                } catch (Exception e) {
                    throw new IllegalStateException("Failed to create/open database", e);
                }
            }
        }
        return sInstance;
    }

}
