package com.example.task9p;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lostandfound.data.DatabaseHelper;

public class RemoveItemActivity extends AppCompatActivity {
    TextView details;   // TextView to display item details
    Button remove;     // Button to trigger item removal
    DatabaseHelper db; // Database helper object to manage database operations

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.remove_item); // Set the layout file for this activity

        details = findViewById(R.id.details);  // Find the details TextView from the layout
        remove = findViewById(R.id.remove);    // Find the remove Button from the layout
        db = new DatabaseHelper(this);         // Initialize database helper

        // Get the passed Intent object which contains additional data
        Intent intent = getIntent();
        // Extract the item id from the intent extras. We will use this to fetch the specific item
        int ITEM_ID = intent.getIntExtra("position",0);

        // Fetch item from database and update the details TextView
        try{
            String detail= db.fetchItem(ITEM_ID);
            details.setText(detail);
        }catch(Exception e){
            Log.d("REACHED",e.getMessage()); // Log the error message in case of an exception
        }

        // Set onClick listener for the remove button
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    // Call database helper's deleteEntry method to remove the item from database
                    db.deleteEntry(Integer.toString(ITEM_ID));
                    // After removal, navigate to the MainActivity
                    Intent intent1 = new Intent(RemoveItemActivity.this,MainActivity.class);
                    startActivity(intent1);
                }catch (Exception e){
                    // Log the error message in case of an exception
                    Log.d("REACHED",e.getMessage());
                }

            }
        });
    }
}
