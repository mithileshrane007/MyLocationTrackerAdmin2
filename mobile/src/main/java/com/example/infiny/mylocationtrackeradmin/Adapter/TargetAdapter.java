package com.example.infiny.mylocationtrackeradmin.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.infiny.mylocationtrackeradmin.Interfaces.IClickListener;
import com.example.infiny.mylocationtrackeradmin.R;

import java.util.ArrayList;

/**
 * Created by infiny on 13/1/17.
 */

public class TargetAdapter extends RecyclerView.Adapter<TargetAdapter.MyViewHolder> implements Filterable {

    public ArrayList<String> arrayList;
    public ArrayList<String> filteredData;
    TextView tv_no_records;
    IClickListener iClickListener;
    Context context;

    public TargetAdapter(Context context, ArrayList<String> arrayList, IClickListener iClickListener, TextView tv_no_records) {
        this.context=context;
        this.filteredData=arrayList;
        this.arrayList = new ArrayList<String>();

        this.tv_no_records=tv_no_records;
        this.arrayList.addAll(filteredData);

        this.iClickListener=iClickListener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_row_target, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.tv_name_target.setText(filteredData.get(position));
    }

    @Override
    public int getItemCount() {
        return filteredData.size();
    }

    @Override
    public Filter getFilter() {

        Filter filter=new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults f = new FilterResults();


                filteredData.clear();
                Log.d("data",charSequence.toString());
                if (charSequence.toString().length() == 0) {
                    filteredData.addAll(arrayList);
                }
                else
                {
                    for (String wp : arrayList)
                    {

                        if (containsChar(wp,charSequence.toString()))
                        {
                            filteredData.add(wp);
                        }else {
                            Log.d("data", ""+wp.compareToIgnoreCase(charSequence.toString()));
                        }
//                        if (wp.contains(charSequence.toString()))
//                        {
//                            filteredData.add(wp);
//                        }else {
//                            Log.d("data", ""+StringU   wp.contains(charSequence.toString()));
//
//                        }
                    }

                }

                f.values = filteredData.toArray();
                f.count = filteredData.size();
                return f;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filterResults.count > 0) {
                    Log.println(Log.INFO, "Results", "FOUND");
                    tv_no_records.setVisibility(View.GONE);
                    notifyDataSetChanged();
                } else {
                    Log.println(Log.INFO, "Results", "-");
                    tv_no_records.setVisibility(View.VISIBLE);
                }
            }
        };
//        return new FilterData(arrayList,filteredData);
        return filter;
    }
    public boolean containsChar(String stringMain, String search) {
        if (search.length() == 0)
            return false;
        else
                return (stringMain.toLowerCase().contains(search.toLowerCase()))?true:false;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_target;
        CardView ll_single_target_view;
        TextView tv_name_target;
        public MyViewHolder(View itemView) {
            super(itemView);
            ll_single_target_view= (CardView) itemView.findViewById(R.id.ll_single_target_view);
            iv_target= (ImageView) itemView.findViewById(R.id.iv_target);
            tv_name_target= (TextView) itemView.findViewById(R.id.tv_name_target);

            ll_single_target_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iClickListener.click(arrayList.get(getAdapterPosition()));
                }
            });

        }
    }

    class FilterData extends Filter{
        ArrayList<String> arrayList;
        ArrayList<String> filteredData;
        public FilterData(ArrayList<String> arrayList, ArrayList<String> filteredData) {
            this.arrayList=arrayList;
            this.filteredData=filteredData;
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            return null;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            Toast.makeText(context,"publishResults ::"+charSequence,Toast.LENGTH_SHORT).show();

        }
    }
}
