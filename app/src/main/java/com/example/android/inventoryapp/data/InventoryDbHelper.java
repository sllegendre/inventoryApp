package com.example.android.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Patterns;

import com.example.android.inventoryapp.data.InventoryContract.InventoryItem;

import java.io.ByteArrayOutputStream;

/**
 * Database helper for Inventory app. Manages database creation and version management.
 */

public class InventoryDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = InventoryDbHelper.class.getSimpleName();

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "inventory.db";

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

        // Create a String that contains the SQL statement to create the inventory table
        String SQL_CREATE_INVENTORY_TABLE = "CREATE TABLE " + InventoryItem.TABLE_NAME + " ("
                + InventoryItem._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryItem.COLUMN_ITEM_NAME + " TEXT NOT NULL, "
                + InventoryItem.COLUMN_ITEM_PRICE + " INTEGER NOT NULL, "
                + InventoryItem.COLUMN_ITEM_QUANTITY + " INTEGER NOT NULL DEFAULT 0,"
                + InventoryItem.COLUMN_ITEM_IMAGE + " BLOB,"
                + InventoryItem.COLUMN_SUPPLIER_NAME + " STRING NOT NULL,"
                + InventoryItem.COLUMN_SUPPLIER_MAIL + " STRING NOT NULL)"
                + ";";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_INVENTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Is this ever explained in the video? The version is static
        // - how could I possibly increase that?
        // And why would I do it in a method?
    }

    // Utility functions

    // convert from bitmap to byte array
    public static byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap convertByteArrayToBitmap(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    /**
     * Returns whether or not the given price is valid
     */
    public static boolean isValidPrice(Integer price) {
        if (price >= 0) {
            return true;
        }
        return false;
    }

    /**
     * Returns whether or not the given quantity is valid
     */
    public static boolean isValidQty(int qty) {
        if (qty >= 0) {
            return true;
        }
        return false;
    }

    /**
     * Returns whether or not the given e-mail address is valid
     */
    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
