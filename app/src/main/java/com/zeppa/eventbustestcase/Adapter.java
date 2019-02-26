package com.zeppa.eventbustestcase;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zeppa.eventbustestcase.events.DataEvent;

import java.util.ArrayList;
import java.util.List;

class Adapter extends RecyclerView.Adapter<Adapter.Holder> {
    private final List<DataEvent> list = new ArrayList<>();

    Adapter() {
        setHasStableIds(true);
    }

    void add(DataEvent event) {
        list.add(event);
        notifyItemInserted(list.size() - 1);
    }

    void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.data_list_item, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        holder.bind(list.get(i));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getRevision();
    }

    static class Holder extends RecyclerView.ViewHolder {
        private final TextView number;

        Holder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.number);
        }

        void bind(DataEvent event) {
            number.setText(String.valueOf(event.getRevision()));
        }
    }
}
