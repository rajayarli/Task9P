package com.example.task9p;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.task9p.data.DatabaseHelper;
import com.example.task9p.model.Item;
import com.example.task9p.data.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ShowAllActivity extends AppCompatActivity {
    ListView itemsListView ;  // ListView to display all items

    ArrayList<String> itemArrayList;  // ArrayList to store item descriptions
    ArrayAdapter<String> adapter;     // Adapter to connect the ArrayList with the ListView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_all);  // Set the layout for this activity

        // Find the ListView from the layout
        itemsListView = findViewById(R.id.itemsListView);
        // Initialize the ArrayList
        itemArrayList = new ArrayList<>();
        // Initialize database helper
        DatabaseHelper db = new DatabaseHelper(this);

        // Fetch all items from the database
        List<Item> itemList = db.fetchAllItems();
        // Loop through each item and add its description to the ArrayList
        for (Item item :itemList)
        {
            itemArrayList.add(item.getDescription());
        }

        // Initialize ArrayAdapter with the item descriptions ArrayList and set it to the ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemArrayList);
        itemsListView.setAdapter(adapter);

        // Set an item click listener for the ListView items
        itemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try{
                    // Create an intent for the RemoveItemActivity
                    Intent intent = new Intent(ShowAllActivity.this,RemoveItemActivity.class);
                    // Loop through the item list to find the clicked item
                    for (Item item :itemList)
                    {
                        // If the clicked item's description matches with an item's description,
                        // get the item's id and put it as an extra in the intent
                        if(item.getDescription().equals((String) itemsListView.getItemAtPosition(position))){
                            int ITEM_ID = item.getItem_id();
                            intent.putExtra("position",ITEM_ID);
                        }
                    }
                    // Start the RemoveItemActivity with the intent
                    startActivity(intent);
                }
                catch (Exception e){
                    // Log the error message in case of an exception
                    Log.d("REACHED",e.getMessage());
                }

            }
        });

    }
}
