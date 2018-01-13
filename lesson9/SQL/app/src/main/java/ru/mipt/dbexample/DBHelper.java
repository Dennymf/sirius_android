package ru.mipt.dbexample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "values";
    public static final int DB_VER = 2;

    private static final String CREATE_DB = "CREATE TABLE test (id INTEGER PRIMARY KEY AUTOINCREMENT, value TEXT);";


    private static final class Factory implements SQLiteDatabase.CursorFactory {
        @Override
        public Cursor newCursor(SQLiteDatabase sqLiteDatabase, SQLiteCursorDriver sqLiteCursorDriver, String s, SQLiteQuery sqLiteQuery) {
            return new SQLiteCursor(sqLiteCursorDriver, s, sqLiteQuery);
        }
    }

    public DBHelper(Context context) {
        this(context, DB_NAME, new Factory(), DB_VER);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int j) {

    }

    public int getCount() {
        SQLiteDatabase db = getReadableDatabase();
        final String regionQuery = "select Count(*) as count from test";
        Cursor cur = null;
        int result = 0;
        if (db != null) {
            try {
                cur = db.rawQuery(regionQuery, null);
                cur.moveToFirst();
                result = cur.getInt(cur.getColumnIndexOrThrow("count"));
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (cur != null) {
                    cur.close();
                }
                db.close();
            }
        }
        return result;
    }

    public void addValue(String value) {
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            ContentValues v = new ContentValues();
            v.put("value", value);
            db.insert("test", null, v);
            db.close();
        }
    }

    public void editValue(int pos, String value) {
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            ContentValues v = new ContentValues();
            v.put("value", value);
            db.update("test", v, "id = ?", new String[]{String.valueOf(pos)});
            db.close();
        }
    }

    public String getValue(int pos) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = null;
        int id = 0;
        String value = null;
        if (db != null) {
            try {
                cur = db.query("test", new String[]{"id", "value"}, "id=?", new String[]{String.valueOf(pos)}, null, null, null);
                cur.moveToFirst();
                id = cur.getInt(cur.getColumnIndexOrThrow("id"));
                value = cur.getString(cur.getColumnIndexOrThrow("value"));
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (cur != null) {
                    cur.close();
                }
                db.close();
            }
        }
        return value;
    }

    public void removeLast() {
        int size = getCount();
        if (size > 0) {
            SQLiteDatabase db = getWritableDatabase();
            if (db != null) {
                db.delete("test", "id=?", new String[]{String.valueOf(size)});
                db.close();
            }
        }
    }
}
