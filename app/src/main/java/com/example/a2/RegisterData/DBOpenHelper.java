package com.example.a2.RegisterData;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Database defined internal extend SQLiteOpenHelper
 */

public class DBOpenHelper extends SQLiteOpenHelper {

    /**
     * Declare a database variable db that comes with AndroidSDK
     */
    private SQLiteDatabase db;

    /**
     * Create a constructor of this class, the parameter is the context, which is
     * the path of the package where this class located
     * Specify the context, database name, factory default null, version number starts from 1.
     * super(context,"db_test",null,1);
     * Set the database to a writable state, unless the memory is full,
     * then it will be automatically set to read-only mode
     * db = getReadableDatabase();
     */
    public DBOpenHelper(Context context) {
        super(context, "db_test", null, 1);
        db = getReadableDatabase();
    }

    /**
     * Override the abstract methods in the subclass DBOpenHelper,
     * which were declared in the abstract class SQLiteOpenHelper
     * onCreate() Create a database table
     * onUpgrade() Add, delete, and modify the database
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS user(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }

    /**
     * Customized addition, deletion, modification and inspection methods
     * These methods are created here, and not necessarily used in the future
     * add()
     * delete()
     * update()
     * getAllData()
     */
    public void add(String name, String password) {
        db.execSQL("INSERT INTO user (name,password) VALUES(?,?)", new Object[]{name, password});
    }

    public void delete(String name, String password) {
        db.execSQL("DELETE FROM user WHERE name = AND password =" + name + password);
    }

    public void updata(String password) {
        db.execSQL("UPDATE user SET password = ?", new Object[]{password});
    }

    /**
     * Method of query table of user
     * A list of ArrayList class is defined to store the query content
     * Use Cursor to query data from the table
     * The first parameter："table name", and following 5 nulls,
     * finally, the sorting method of the query content: "name DESC".
     * Create a while loop to let the cursor swim from the head to the end of the table.
     * Store the data in the list container during the tour
     *
     * @return
     */
    public ArrayList<User> getAllData() {

        ArrayList<User> list = new ArrayList<User>();
        Cursor cursor = db.query("user", null, null, null, null, null, "name DESC");
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String password = cursor.getString(cursor.getColumnIndex("password"));
            list.add(new User(name, password));
        }
        return list;
    }
}
