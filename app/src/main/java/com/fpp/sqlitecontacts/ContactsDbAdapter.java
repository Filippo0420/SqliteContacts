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
    public static final String KEY_NAME = "name";
    public static final String NAME_OPTIONS = "TEXT NOT NULL";
    public static final int NAME_COLUMN = 1;
    public static final String KEY_SURNAME = "surname";
    public static final String SURNAME_OPTIONS = "TEXT NOT NULL";
    public static final int SURNAME_COLUMN = 2;
    public static final String KEY_PHONE = "phone";
    public static final String PHONE_OPTIONS = "TEXT NOT NULL";
    public static final int PHONE_COLUMN = 3;
    public static final String KEY_MAIL = "mail";
    public static final String MAIL_OPTIONS = "TEXT NOT NULL";
    public static final int MAIL_COLUMN = 4;

    private static final String DB_CREATE_CONTACTS_TABLE =
            "CREATE TABLE " + DB_CONTACTS_TABLE + "( " +
                    KEY_ID + " " + ID_OPTIONS + ", " +
                    KEY_NAME + " " + NAME_OPTIONS + ", " +
                    KEY_SURNAME + " " + SURNAME_OPTIONS + ", " +
                    KEY_PHONE + " " + PHONE_OPTIONS + ", " +
                    KEY_MAIL + " " + MAIL_OPTIONS +
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

    public long insertContacts(String name, String surname, String phone, String mail) {
        ContentValues newContactsValues = new ContentValues();
        newContactsValues.put(KEY_NAME, name);
        return db.insert(DB_CONTACTS_TABLE, null, newContactsValues);
    }

    public boolean updateContacts(Contacts task) {
        long id = task.getId();
        String name = task.getName();
        String surname = task.getSurname();
        String phone = task.getPhone();
        String mail = task.getMail();
        return updateContacts(id, name, surname, phone, mail);
    }

    public boolean updateContacts(long id, String name, String surname, String phone, String mail) {
        String where = KEY_ID + "=" + id;
        ContentValues updateContactsValues = new ContentValues();
        updateContactsValues.put(KEY_NAME, name);
        updateContactsValues.put(KEY_SURNAME, surname);
        updateContactsValues.put(KEY_PHONE, phone);
        updateContactsValues.put(KEY_MAIL, mail);
        return db.update(DB_CONTACTS_TABLE, updateContactsValues, where, null) > 0;
    }

    public boolean deleteContacts(long id){
        String where = KEY_ID + "=" + id;
        return db.delete(DB_CONTACTS_TABLE, where, null) > 0;
    }

    public Cursor getAllContacts() {
        String[] columns = {KEY_ID, KEY_NAME, KEY_SURNAME, KEY_PHONE, KEY_MAIL};
        return db.query(DB_CONTACTS_TABLE, columns, null, null, null, null, null);
    }

    public Contacts getContacts(long id) {
        String[] columns = {KEY_ID, KEY_NAME, KEY_SURNAME, KEY_PHONE, KEY_MAIL};
        String where = KEY_ID + "=" + id;
        Cursor cursor = db.query(DB_CONTACTS_TABLE, columns, where, null, null, null, null);
        Contacts task = null;
        if(cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(NAME_COLUMN);
            String surname = cursor.getString(SURNAME_COLUMN);
            String phone = cursor.getString(PHONE_COLUMN);
            String mail = cursor.getString(MAIL_COLUMN);
            task = new Contacts(id, name, surname, phone, mail);
        }
        return task;
    }

}
