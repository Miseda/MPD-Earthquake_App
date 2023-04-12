//STUDENT INFORMATION
//NAME: ALEX MISEDA MUMBO
//STUDENT ID: S2023370

package com.example.mumbo_alex_s2023370;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EarthquakeAdapter extends RecyclerView.Adapter<EarthquakeAdapter.EarthquakeViewHolder> {

    // The adapter needs a list of EarthquakeCard objects to display and a Context object to launch the activity.
    private List<EarthquakeCard> earthquakeList;
    private Context context;

    public EarthquakeAdapter(List<EarthquakeCard> earthquakeList, Context context) {
        this.earthquakeList = earthquakeList;
        this.context = context;
    }

    // onCreateViewHolder is called when a new ViewHolder object needs to be created.
    // It inflates the layout for each item in the RecyclerView.
    @NonNull
    @Override
    public EarthquakeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.earthquake_list_item, parent, false);
        return new EarthquakeViewHolder(itemView);
    }

    // onBindViewHolder is called for each item in the RecyclerView.
    // It binds the data to the ViewHolder object.

    @Override
    public void onBindViewHolder(@NonNull EarthquakeViewHolder holder, int position) {
        // Get the EarthquakeCard object at the given position.
        EarthquakeCard earthquake = earthquakeList.get(position);

        // Set the earthquake name, description, and date in the appropriate TextViews.
        holder.earthquakeNameTextView.setText(earthquake.getName());
        holder.earthquakeDateTextView.setText(earthquake.getDate());

        // Extract the location and strength from the description string.
        String description = earthquake.getDescription();
        int locationStartIndex = description.indexOf("Location:");
        int locationEndIndex = description.indexOf(",", locationStartIndex);
        String location = description.substring(locationStartIndex + 10, locationEndIndex).trim();
        earthquake.setStrength();
        String strength = earthquake.getStrength();
        earthquake.setDepth();
        String depth = earthquake.getDepth();


        // Set the location and strength in the appropriate TextViews.
        holder.earthquakeLocationTextView.setText(location);
        holder.earthquakeStrengthTextView.setText(strength);
        holder.earthquakeStrengthTextView.setTextColor(earthquake.getColorForStrength());
        holder.earthquakeDepthTextView.setText(depth);

        // Set an OnClickListener on the card view.
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch the MaximizedView activity with the earthquake data as extras.
                Intent intent = new Intent(context, MaximizedView.class);
                intent.putExtra("name", earthquake.getName());
                intent.putExtra("date", earthquake.getDate());
                intent.putExtra("description", earthquake.getDescription());
                context.startActivity(intent);
            }
        });
    }




    @Override
    public int getItemCount() {
        return earthquakeList.size();
    }

    public static class EarthquakeViewHolder extends RecyclerView.ViewHolder {
        TextView earthquakeNameTextView;
        TextView earthquakeDateTextView;
        TextView earthquakeLocationTextView;
        TextView earthquakeStrengthTextView;
        TextView earthquakeDepthTextView;
        CardView card;

        public EarthquakeViewHolder(View itemView) {
            super(itemView);
            earthquakeNameTextView = itemView.findViewById(R.id.earthquakeNameTextView);
            earthquakeDateTextView = itemView.findViewById(R.id.earthquakeDateTextView);
            earthquakeLocationTextView = itemView.findViewById(R.id.earthquakeLocationTextView);
            earthquakeStrengthTextView = itemView.findViewById(R.id.earthquakeStrengthTextView);
            earthquakeDepthTextView = itemView.findViewById(R.id.earthquakeDepthTextView);
            card = itemView.findViewById(R.id.card);
        }
    }

}
