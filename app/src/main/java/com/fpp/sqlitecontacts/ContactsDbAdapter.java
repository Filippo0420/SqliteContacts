package com.fpp.sqlitecontacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ContactsDbAdapter {
    private static final String DEBUG_TAG = "SqLiteContactsManager";

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "database.db";
    private static final String DB_CONTACTS_TABLE = "contacts";

    public static final String KEY_ID = "_id";
    public static final String ID_OPTIONS = "INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final int ID_COLUMN = 0;
    public static final String KEY_DESCRIPTION = "description";
    public static final String DESCRIPTION_OPTIONS = "TEXT NOT NULL";
    public static final int DESCRIPTION_COLUMN = 1;
    public static final String KEY_COMPLETED = "completed";
    public static final String COMPLETED_OPTIONS = "INTEGER DEFAULT 0";
    public static final int COMPLETED_COLUMN = 2;

    private static final String DB_CREATE_CONTACTS_TABLE =
            "CREATE TABLE " + DB_CONTACTS_TABLE + "( " +
                    KEY_ID + " " + ID_OPTIONS + ", " +
                    KEY_DESCRIPTION + " " + DESCRIPTION_OPTIONS + ", " +
                    KEY_COMPLETED + " " + COMPLETED_OPTIONS +
                    ");";
    private static final String DROP_CONTACTS_TABLE =
            "DROP TABLE IF EXISTS " + DB_CONTACTS_TABLE;

    private SQLiteDatabase db;
    private Context context;
    private DatabaseHelper dbHelper;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_CONTACTS_TABLE);

            Log.d(DEBUG_TAG, "Database creating...");
            Log.d(DEBUG_TAG, "Table " + DB_CONTACTS_TABLE + " ver." + DB_VERSION + " created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_CONTACTS_TABLE);

            Log.d(DEBUG_TAG, "Database updating...");
            Log.d(DEBUG_TAG, "Table " + DB_CONTACTS_TABLE + " updated from ver." + oldVersion + " to ver." + newVersion);
            Log.d(DEBUG_TAG, "All data is lost.");

            onCreate(db);
        }
    }

    public ContactsDbAdapter(Context context) {
        this.context = context;
    }

    public ContactsDbAdapter open(){
        dbHelper = new DatabaseHelper(context, DB_NAME, null, DB_VERSION);
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            db = dbHelper.getReadableDatabase();
        }
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long insertContacts(String description) {
        ContentValues newContactsValues = new ContentValues();
        newContactsValues.put(KEY_DESCRIPTION, description);
        return db.insert(DB_CONTACTS_TABLE, null, newContactsValues);
    }

    public boolean updateContacts(Contacts task) {
        long id = task.getId();
        String description = task.getDescription();
        boolean completed = task.isCompleted();
        return updateContacts(id, description, completed);
    }

    public boolean updateContacts(long id, String description, boolean completed) {
        String where = KEY_ID + "=" + id;
        int completedTask = completed ? 1 : 0;
        ContentValues updateContactsValues = new ContentValues();
        updateContactsValues.put(KEY_DESCRIPTION, description);
        updateContactsValues.put(KEY_COMPLETED, completedTask);
        return db.update(DB_CONTACTS_TABLE, updateContactsValues, where, null) > 0;
    }

    public boolean deleteContacts(long id){
        String where = KEY_ID + "=" + id;
        return db.delete(DB_CONTACTS_TABLE, where, null) > 0;
    }

    public Cursor getAllContacts() {
        String[] columns = {KEY_ID, KEY_DESCRIPTION, KEY_COMPLETED};
        return db.query(DB_CONTACTS_TABLE, columns, null, null, null, null, null);
    }

    public Contacts getContacts(long id) {
        String[] columns = {KEY_ID, KEY_DESCRIPTION, KEY_COMPLETED};
        String where = KEY_ID + "=" + id;
        Cursor cursor = db.query(DB_CONTACTS_TABLE, columns, where, null, null, null, null);
        Contacts task = null;
        if(cursor != null && cursor.moveToFirst()) {
            String description = cursor.getString(DESCRIPTION_COLUMN);
            boolean completed = cursor.getInt(COMPLETED_COLUMN) > 0 ? true : false;
            task = new Contacts(id, description, completed);
        }
        return task;
    }

}
