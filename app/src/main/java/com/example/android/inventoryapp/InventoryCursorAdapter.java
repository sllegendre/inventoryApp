package com.example.android.inventoryapp;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.data.InventoryContract;

import static android.R.attr.id;

/**
 * Created by SLLegendre on 7/16/2017.
 */

public class InventoryCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link InventoryCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }


    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the inventory data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current item can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Get the unique resource identifier for this item
        final Uri currentItemUri = ContentUris.withAppendedId(InventoryContract.InventoryItem.CONTENT_URI, id);

        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.item_name);
        TextView priceTextView = (TextView) view.findViewById(R.id.item_price);
        TextView qtyTextView = (TextView) view.findViewById(R.id.item_quantity);

        // Find the columns of item attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryItem.COLUMN_ITEM_NAME);
        int priceColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryItem.COLUMN_ITEM_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryItem.COLUMN_ITEM_QUANTITY);

        // Read the item attributes from the Cursor for the current tiem
        String itemName = cursor.getString(nameColumnIndex);
        int itemPrice = cursor.getInt(priceColumnIndex);
        final int itemQty = cursor.getInt(quantityColumnIndex);

        // Update the TextViews with the attributes for the current item
        nameTextView.setText(itemName);
        priceTextView.setText(String.valueOf(itemPrice));
        qtyTextView.setText(String.valueOf(itemQty));

        // Set onClick listener on the sale button and give that bad boy a name
        Button saleButton = (Button) view.findViewById(R.id.sale_button);
        saleButton.setText(R.string.action_sale);
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get a content resolver
                int quantity = itemQty; //TODO find out what java madness is happening here
                ContentResolver contentResolver = v.getContext().getContentResolver();
                ContentValues values = new ContentValues();
                if (quantity >= 1) {
                    values.put(InventoryContract.InventoryItem.COLUMN_ITEM_QUANTITY, Integer.toString(quantity--));
                    contentResolver.update(currentItemUri, values, null, null);
                } else {
                    // Tell the user, they cannot do dat
                    Toast.makeText(v.getContext(), "Quantity cannot be negative", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
