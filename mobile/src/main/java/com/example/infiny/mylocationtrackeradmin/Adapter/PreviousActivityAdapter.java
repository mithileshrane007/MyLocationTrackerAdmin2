package com.example.infiny.mylocationtrackeradmin.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.infiny.mylocationtrackeradmin.Models.Target;
import com.example.infiny.mylocationtrackeradmin.R;

import java.util.ArrayList;

/**
 * Created by infiny on 16/1/17.
 */
public class PreviousActivityAdapter  extends RecyclerView.Adapter<PreviousActivityAdapter.MyViewHolder> {
    ArrayList<Target> targetArrayList;
    @Override
    public PreviousActivityAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_row_previous_detail, parent, false);

        return new PreviousActivityAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PreviousActivityAdapter.MyViewHolder holder, int position) {
        holder.tv_entry_number.setText(position+1);
        holder.tv_entry_date.setText(targetArrayList.get(position).getDate_str());
        holder.tv_avg_hours.setText(targetArrayList.get(position).getAvg_hr());
    }

    @Override
    public int getItemCount() {
        return targetArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_entry_number,tv_entry_date,tv_avg_hours;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_entry_date= (TextView) itemView.findViewById(R.id.tv_entry_date);
            tv_avg_hours= (TextView) itemView.findViewById(R.id.tv_avg_hours);
            tv_entry_number= (TextView) itemView.findViewById(R.id.tv_entry_number);
        }
    }
}
