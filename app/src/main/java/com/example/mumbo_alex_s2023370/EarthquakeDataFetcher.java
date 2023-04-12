//STUDENT INFORMATION
//NAME: ALEX MISEDA MUMBO
//STUDENT ID: S2023370

package com.example.mumbo_alex_s2023370;

import android.os.Handler;
import android.os.Looper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class EarthquakeDataFetcher {
    public CompletionResponse onComplete;

    // Constructor to set the response handler
    public EarthquakeDataFetcher(CompletionResponse onComplete) {
        this.onComplete = onComplete;
    }

    // Method to fetch the earthquake data from the server
    public void execute() {
        // Create a new thread to run the network request
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Define variables to store the response and the network connection
                HttpURLConnection urlConnection = null;
                String response = null;

                try {
                    // Create a URL object for the earthquake data feed
                    URL url = new URL("http://quakes.bgs.ac.uk/feeds/WorldSeismology.xml");

                    // Open a connection to the server
                    urlConnection = (HttpURLConnection) url.openConnection();

                    // Read the response from the server
                    BufferedReader rd = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream()));
                    String line;

                    // Store the response in a StringBuilder object
                    final StringBuilder sb = new StringBuilder();
                    while ((line = rd.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    response = sb.toString();
                } catch (Exception e) {
                    // If there is an error, print the stack trace
                    e.printStackTrace();
                } finally {
                    // Disconnect the network connection if it is open
                    if (urlConnection != null) urlConnection.disconnect();
                }

                // Create a handler on the main thread to handle the response
                Handler handler = new Handler(Looper.getMainLooper());
                final String finalResponse = response;
                // Use the handler to post a Runnable that calls the response handler
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onComplete.callback(finalResponse);
                    }
                });
            }
        });
        // Start the network request thread
        thread.start();
    }

    // Define an interface to handle the response
    interface CompletionResponse {
        public void callback(String response);
    }

}
