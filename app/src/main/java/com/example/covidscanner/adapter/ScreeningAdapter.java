package com.example.covidscanner.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covidscanner.R;
import com.example.covidscanner.classes.Screening;

import java.util.List;

public class ScreeningAdapter extends RecyclerView.Adapter<ScreeningAdapter.ViewHolder>
{
    private List<Screening> screenings;

    public interface ItemClicked
    {
        void onItemClicked(int index);
    }

    public ScreeningAdapter (Context context, List<Screening> list)
    {
        screenings = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvTemp, tvTravel, tvContact, tvSymptoms;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTemp = itemView.findViewById(R.id.tvTemp);
            tvTravel = itemView.findViewById(R.id.tvTravel);
            tvContact = itemView.findViewById(R.id.tvContact);
            tvSymptoms = itemView.findViewById(R.id.tvSymptoms);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    @NonNull
    @Override
    public ScreeningAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScreeningAdapter.ViewHolder holder, int position) {

        holder.itemView.setTag(screenings.get(position));

        holder.tvTemp.setText(screenings.get(position).getTemperature() + " ");

        if (screenings.get(position).getTemperature() >= 38.0)
        {
            holder.tvTemp.setTextColor(Color.parseColor(String.valueOf(R.color.error_text)));
        }
        else
        {
            holder.tvTemp.setTextColor(Color.parseColor(String.valueOf(R.color.normal_text)));
        }

        if (screenings.get(position).isInContactWithCovidPeople())
        {
            holder.tvContact.setTextColor(Color.parseColor(String.valueOf(R.color.error_text)));
        }
        else
        {
            holder.tvContact.setTextColor(Color.parseColor(String.valueOf(R.color.normal_text)));
        }

        if (screenings.get(position).isHighRiskCountry())
        {
            holder.tvTravel.setTextColor(Color.parseColor(String.valueOf(R.color.error_text)));
        }
        else
        {
            holder.tvTravel.setTextColor(Color.parseColor(String.valueOf(R.color.error_text)));
        }

        if (screenings.get(position).isHaveSymptoms())
        {
            holder.tvSymptoms.setTextColor(Color.parseColor(String.valueOf(R.color.error_text)));
        }
        else
        {
            holder.tvSymptoms.setTextColor(Color.parseColor(String.valueOf(R.color.error_text)));
        }

    }

    @Override
    public int getItemCount() {
        return screenings.size();
    }
}
