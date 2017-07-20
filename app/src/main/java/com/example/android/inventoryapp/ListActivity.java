package com.example.android.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.inventoryapp.data.InventoryContract;

public class ListActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = ListActivity.class.getSimpleName();

    /**
     * Identifier for the data loader
     */
    private static final int INVENTORY_LOADER = 0;

    // Define a projection that specifies the columns from the table we care about.
    public final String[] RELEVANT_COLS = {
            InventoryContract.InventoryItem._ID,
            InventoryContract.InventoryItem.COLUMN_ITEM_NAME,
            InventoryContract.InventoryItem.COLUMN_ITEM_PRICE,
            InventoryContract.InventoryItem.COLUMN_ITEM_QUANTITY,
            InventoryContract.InventoryItem.COLUMN_ITEM_IMAGE
    };

    /**
     * Adapter for the ListView
     */
    InventoryCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_container);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // Find the ListView which will be populated with the data
        ListView itemsListView = (ListView) findViewById(R.id.list_view);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        TextView emptyView = (TextView) findViewById(R.id.empty);
        emptyView.setText(R.string.empty_list_text);
        itemsListView.setEmptyView(emptyView);

        // Setup an Adapter to create a list item for each row of data in the Cursor.
        // There is no data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new InventoryCursorAdapter(this, null);
        itemsListView.setAdapter(mCursorAdapter);

        // Setup the item click listener
        itemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Create new intent to go to {@link EditorActivity}
                Log.v(LOG_TAG, "the OnItemClick method was executed");
                Intent intent = new Intent(ListActivity.this, EditorActivity.class);

                // Form the content URI that represents the specific item that was clicked on,
                // by appending the "id" (passed as input to this method) onto the
                // content uri
                Uri currentItemUri = ContentUris.withAppendedId(InventoryContract.InventoryItem.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentItemUri);

                // Launch the {@link EditorActivity} to display the data for the current item.
                startActivity(intent);
            }
        });

        // Kick off the loader
        getLoaderManager().initLoader(INVENTORY_LOADER, null, this);
    }

    /**
     * Helper method to insert hardcoded item data into the database. For debugging purposes only.
     */
    private void insertItem() {
        // Create a ContentValues object where column names are the keys, dummy values
        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryItem.COLUMN_ITEM_NAME, "Flux Capicitator");
        values.put(InventoryContract.InventoryItem.COLUMN_ITEM_PRICE, 9000);
        values.put(InventoryContract.InventoryItem.COLUMN_ITEM_QUANTITY, 80);
        values.put(InventoryContract.InventoryItem.COLUMN_SUPPLIER_NAME, "Kermit the Frog");
        values.put(InventoryContract.InventoryItem.COLUMN_SUPPLIER_MAIL, "kermit@frogpond.com");

        // Insert a new row into the provider using the ContentResolver.
        Uri newUri = getContentResolver().insert(InventoryContract.InventoryItem.CONTENT_URI, values);
    }

    /**
     * Helper method to delete all items in the database.
     */
    private void deleteAllItems() {
        int rowsDeleted = getContentResolver().delete(InventoryContract.InventoryItem.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from database");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertItem();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllItems();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // Define a projection that specifies the columns from the table we care about.
        String[] projection = RELEVANT_COLS;

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                InventoryContract.InventoryItem.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i(LOG_TAG, "data rows: " + data.getCount());
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
