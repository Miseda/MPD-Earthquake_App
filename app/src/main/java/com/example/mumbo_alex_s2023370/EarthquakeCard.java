//STUDENT INFORMATION
//NAME: ALEX MISEDA MUMBO
//STUDENT ID: S2023370

package com.example.mumbo_alex_s2023370;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Color;


public class EarthquakeCard {
    private String name;
    private String description;
    private String date;
    private String location;
    private String strength;
    private String depth;

    public EarthquakeCard(String name, String description, String date) {
        this.name = name;
        this.description = description;
        this.date = date;
        setLocation();
        setStrength();
    }


    private void setLocation() {
        // Split the description string into parts using semicolon as a delimiter
        String[] parts = description.split(";");

        // Check if the parts array contains at least two elements (i.e., location information)
        if (parts.length >= 2) {
            // Split the second part of the description string (which contains location information) into parts using colon as a delimiter
            String[] locParts = parts[1].split(":");

            // Check if the locParts array contains at least two elements (i.e., location value)
            if (locParts.length >= 2) {
                // Remove any leading/trailing spaces from the location value and assign it to the location variable
                this.location = locParts[1].trim();
            }
        }
    }

    public void setStrength() {
        String[] parts = description.split(";");
        for (String part : parts) {
            if (part.contains("Magnitude")) {
                // Extract the magnitude value from the string
                String magnitudeString = part.substring(part.indexOf(":") + 1).trim();
                this.strength = magnitudeString;
                break;
            }
        }
    }

    // Method to get color for the strength of the earthquake
    public int getColorForStrength() {
        if (this.strength == null) {
            return Color.GRAY;
        }
        double strengthValue = Double.parseDouble(this.strength);
        if (strengthValue < 4.0) {
            return Color.parseColor("#00CC00"); // Bright green
        } else if (strengthValue < 6.0) {
            return Color.parseColor("#FFCC00"); // Bright yellow
        } else if (strengthValue < 8.0) {
            return Color.parseColor("#FF6600"); // Bright shade of orange
        } else {
            return Color.parseColor("#FF0033"); // Bright red
        }
    }

    public String getLocationName() {
        if (this.location == null) {
            return "";
        }

        // Split the location string using comma as a delimiter
        String[] locParts = this.location.split(",");

        // Check if the locParts array contains at least one element
        if (locParts.length >= 1) {
            // Remove any leading/trailing spaces from the location name and return it
            return locParts[0].trim();
        } else {
            // Return an empty string if location is not set or does not contain a location name
            return "";
        }
    }

    public void setDepth() {
        String[] parts = description.split(";");
        for (String part : parts) {
            if (part.contains("Depth")) {
                // Extract the magnitude value from the string
                String depthString = part.substring(part.indexOf(":") + 1).trim();
                this.depth = depthString;
                break;
            }
        }
    }




    // Getter and setter methods for all instance variables
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        setStrength();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStrength() {
        return strength;
    }

    public String getDepth(){ return depth; }






    // toString() method to return a string representation of the object
    @Override
    public String toString() {
        return "EarthquakeCard{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", location='" + location + '\'' +
                ", strength='" + strength + '\'' +
                ", depth='" + depth + '\''+
        '}';
    }
}
