package com.example.android.inventoryapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by SLLegendre on 7/16/2017.
 */

public class InventoryContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private InventoryContract() {
    }

    /* Foundations for URI */
    public static final String CONTENT_AUTHORITY = "com.example.android.inventoryapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_INVENTORY = "inventory";

    /**
     * Inner class that defines constant values for the database table.
     * Each entry in the table represents a single item in stock.
     */
    public static final class InventoryItem implements BaseColumns {

        /* The content URI to access the data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY);

        /* The MIME type of the {@link #CONTENT_URI} for a list of inventory. */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single item.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        /** Table constants for table: inventory **/

        public final static String TABLE_NAME = "inventory";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_ITEM_NAME ="name";
        public final static String COLUMN_ITEM_QUANTITIY ="quantity";
        public final static String COLUMN_ITEM_PRICE ="price";

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
         * Returns whether or not the given price is valid
         */
        public static boolean isValidPrice(Integer price) {
            if (price >= 0) {
                return true;
            }
            return false;        }
    }

}
