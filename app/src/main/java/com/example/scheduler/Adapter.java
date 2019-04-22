package com.example.scheduler;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    Context context;
    ArrayList<String> rows;
    ArrayList<String> times;

    public Adapter(Context context, ArrayList<String> rows, ArrayList<String> times) {
        this.context = context;
        this.rows = rows;
        this.times = times;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        viewHolder.t.setText(rows.get(i) + times.get(i));
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView t;

        public MyViewHolder(View view) {
            super(view);
            t = view.findViewById(R.id.text_view);
        }
    }
}