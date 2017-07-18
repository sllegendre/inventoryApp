package com.example.android.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.inventoryapp.data.InventoryContract.InventoryItem;

/**
 * Database helper for Inventory app. Manages database creation and version management.
 */

public class InventoryDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = InventoryDbHelper.class.getSimpleName();

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "stock.db";

    /**
     * Database version. If you change the database schema, increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link InventoryDbHelper}.
     *
     * @param context of the app
     */
    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_INVENTORY_TABLE = "CREATE TABLE " + InventoryItem.TABLE_NAME + " ("
                + InventoryItem._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryItem.COLUMN_ITEM_NAME + " TEXT NOT NULL, "
                + InventoryItem.COLUMN_ITEM_PRICE + " INTEGER NOT NULL, "
                + InventoryItem.COLUMN_ITEM_QUANTITIY + " INTEGER NOT NULL DEFAULT 0);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_INVENTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Is this ever explained in the video? The version is static
        // - how could I possibly increase that?
        // And why would I do it in a method?
    }
}
