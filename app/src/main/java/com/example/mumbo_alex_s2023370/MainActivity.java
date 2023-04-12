//STUDENT INFORMATION
//NAME: ALEX MISEDA MUMBO
//STUDENT ID: S2023370

package com.example.mumbo_alex_s2023370;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;

public class MainActivity extends AppCompatActivity implements EarthquakeDataFetcher.CompletionResponse {

    // Declare variables

    RecyclerView recyclerView;
    EditText searchBox;
    EditText dateBox;
    ImageButton searchButton;
    Button magnitudeButton;
    Button depthButton;
    List<EarthquakeCard> earthQuakeItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the layout
        setContentView(R.layout.activity_main);

        // --- Setting up the list of items
        recyclerView = findViewById(R.id.listOfItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchBox = findViewById(R.id.search_edit_text);
        searchButton = findViewById(R.id.search_button);
        dateBox = findViewById(R.id.date_edit_text);
        dateBox.addTextChangedListener(mTextWatcher);
        magnitudeButton = findViewById(R.id.largest_magnitude_button);
        depthButton = findViewById(R.id.deepest_shallowest_button);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // Set onClickListener on the search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearch();
            }
        });

        // --- Load data from website
        new EarthquakeDataFetcher(this).execute();

        magnitudeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                EarthquakeCard maxMagEntry = null;
                EarthquakeCard minMagEntry = null;
                double maxMag = Double.MIN_VALUE;
                double minMag = Double.MAX_VALUE;
                for (EarthquakeCard entry : earthQuakeItems) {
                    String magString = entry.getStrength();
                    if (magString != null && !magString.isEmpty()) {
                        double magnitude = Double.parseDouble(magString.trim());
                        if (magnitude > maxMag) {
                            maxMag = magnitude;
                            maxMagEntry = entry;
                        }
                        if (magnitude < minMag) {
                            minMag = magnitude;
                            minMagEntry = entry;
                        }
                    }
                }
                if (maxMagEntry == null || minMagEntry == null) {
                    Toast.makeText(MainActivity.this, "No results found for magnitude search", Toast.LENGTH_SHORT).show();
                } else {
                    List<EarthquakeCard> searchResults = new ArrayList<>();
                    searchResults.add(maxMagEntry);
                    searchResults.add(minMagEntry);
                    EarthquakeAdapter adapter = new EarthquakeAdapter(searchResults, MainActivity.this);
                    recyclerView.setAdapter(adapter);
                }
                depthButton.setVisibility(View.GONE);
                magnitudeButton.setVisibility(View.GONE);
            }
        });

        depthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                double minDepth = Double.MAX_VALUE;
                double maxDepth = Double.MIN_VALUE;

                EarthquakeCard minDepthEntry = null;
                EarthquakeCard maxDepthEntry = null;

                for (EarthquakeCard entry : earthQuakeItems) {
                    String depthString = entry.getDepth();
                    if (depthString != null) {
                        // Remove any non-numeric characters from the depth string
                        String depthNumberString = depthString.replaceAll("[^0-9.]", "");
                        if (!depthNumberString.isEmpty()) {
                            double depth = Double.parseDouble(depthNumberString);
                            if (depth < minDepth) {
                                minDepth = depth;
                                minDepthEntry = entry;
                            }
                            if (depth > maxDepth) {
                                maxDepth = depth;
                                maxDepthEntry = entry;
                            }
                        }
                    }
                }

                if (minDepthEntry != null && maxDepthEntry != null) {
                    List<EarthquakeCard> depthResults = new ArrayList<>();
                    depthResults.add(maxDepthEntry);
                    depthResults.add(minDepthEntry);
                    EarthquakeAdapter adapter = new EarthquakeAdapter(depthResults, MainActivity.this);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(MainActivity.this, "No depth data available", Toast.LENGTH_SHORT).show();
                }
                depthButton.setVisibility(View.GONE);
                magnitudeButton.setVisibility(View.GONE);
            }
        });
    }

    public List<EarthquakeCard> dummy(){
        ArrayList<EarthquakeCard> earthquakeItems = new ArrayList<>();

        // Create 5 earthquake items
        for (int i = 0; i < 5; i++) {
            EarthquakeCard earthquakeItem = new EarthquakeCard(
                    "Earthquake " + (i+1),
                    "This is a description for earthquake " + (i+1),
                    "2022-03-25"
            );
            earthquakeItems.add(earthquakeItem);
        }

        return earthquakeItems;
    }

    // Called when the activity is destroyed
    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(mRunnable);
        super.onDestroy();
    }

    // Called when an options item is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle back button
            // Clearing the search results and show the original list of items
            EarthquakeAdapter adapter = new EarthquakeAdapter(earthQuakeItems,this);
            recyclerView.setAdapter(adapter);
            searchBox.setText("");
            dateBox.setText("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            depthButton.setVisibility(View.VISIBLE);
            magnitudeButton.setVisibility(View.VISIBLE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Handler and runnable to perform a search after a delay
    private final Handler mHandler = new Handler();
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doSearch();
        }
    };

    // TextWatcher to perform a search when the user stops typing for a certain amount of time
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            mHandler.removeCallbacks(mRunnable);
            mHandler.postDelayed(mRunnable, 500); // delay for 500ms
        }
    };

    // Method to perform a search
    public void doSearch() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Get the search query and date entered by the user
        String query = searchBox.getText().toString().trim();
        String date = dateBox.getText().toString().trim();

        // Check if both search query and date are empty
        if (query.isEmpty() && date.isEmpty()) {
            Toast.makeText(this, "Please enter a search term or date", Toast.LENGTH_SHORT).show();
            getSupportActionBar().setDisplayHomeAsUpEnabled(false); // hide back button again
            return;
        }

        // Create a list to hold the filtered earthquake items
        List<EarthquakeCard> filteredItems = new ArrayList<>();
        for (EarthquakeCard item : earthQuakeItems) {
            // Check if the search query is not empty and the item's description contains the search query
            if (!query.isEmpty() && item.getDescription().toLowerCase().contains(query.toLowerCase())) {
                filteredItems.add(item);
            }
            // Check if the date is not empty
            if (!date.isEmpty()) {
                SimpleDateFormat sdfIn = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.ENGLISH);
                SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                try {
                    Date itemDate = sdfIn.parse(item.getDate());
                    String itemDateString = sdfOut.format(itemDate);
                    // Check if the formatted date matches the search date
                    if (itemDateString.equals(date)) {
                        filteredItems.add(item);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        // Check if no results were found
        if (filteredItems.isEmpty()) {
            Toast.makeText(this, "No results found", Toast.LENGTH_SHORT).show();
        } else {
            // Create an adapter for the filtered earthquake items and set it on the recycler view
            EarthquakeAdapter adapter = new EarthquakeAdapter(filteredItems, this);
            recyclerView.setAdapter(adapter);
        }
    }


    // Method is called to received the response from the website.
    @Override
    public void callback(String response) {

        parseXMLFromStringAndInflate(response);
    }

    // Method to extract the relevant data from the XML response string.
    public void parseXMLFromStringAndInflate(String xmlString){

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xmlString));

            EarthquakeCard earthquakeItem = null;

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();

                if (eventType == XmlPullParser.START_TAG) {
                    if (tagName.equalsIgnoreCase("item")) {
                        earthquakeItem = new EarthquakeCard("", "", "");
                    } else if (earthquakeItem != null) {
                        switch (tagName.toLowerCase()) {
                            case "title":
                                earthquakeItem.setName(parser.nextText());
                                break;
                            case "description":
                                earthquakeItem.setDescription(parser.nextText());
                                break;
                            case "pubdate":
                                earthquakeItem.setDate(parser.nextText());
                                break;
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG && tagName.equalsIgnoreCase("item")) {
                    earthQuakeItems.add(earthquakeItem);
                }

                eventType = parser.next();
            }

            // Inflate the recycler view here with the items
            EarthquakeAdapter adapter = new EarthquakeAdapter(earthQuakeItems,this);
            recyclerView.setAdapter(adapter);
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }
}