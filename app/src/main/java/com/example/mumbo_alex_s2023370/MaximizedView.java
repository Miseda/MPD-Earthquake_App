//STUDENT INFORMATION
//NAME: ALEX MISEDA MUMBO
//STUDENT ID: S2023370

package com.example.mumbo_alex_s2023370;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class MaximizedView extends AppCompatActivity {

    // Define variables to hold the views for displaying earthquake information
    TextView nameView, descriptionView, dateView;

    // Define an EarthquakeCard object to hold the selected earthquake item
    EarthquakeCard earthQuakeItem = new EarthquakeCard("", "", "");

    // Method to initialize the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout for the activity
        setContentView(R.layout.maximized_view);

        // Show the back button on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Find the views for displaying earthquake information
        nameView = findViewById(R.id.earthquakeNameTextView);
        dateView= findViewById(R.id.earthquakeDateValueTextView);
        descriptionView = findViewById(R.id.earthquakeDescriptionTextView);

        // Get the earthquake data passed from the previous activity and create an EarthquakeCard object
        Intent intent = getIntent();
        if(intent != null) {
            String name ="" , desc ="", date ="";
            name = intent.getStringExtra("name");
            desc = intent.getStringExtra("description");
            date = intent.getStringExtra("date");
            earthQuakeItem.setDate(date);
            earthQuakeItem.setName(name);
            earthQuakeItem.setDescription(desc);

            // Set the views to display the earthquake information
            nameView.setText(name);
            dateView.setText(date);
            descriptionView.setText(desc);

            // Set the activity title to the name of the selected earthquake
            getSupportActionBar().setTitle(name);
        }

    }

    // Method to handle action bar item clicks
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle the back button click
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}