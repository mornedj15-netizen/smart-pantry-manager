package com.example.smartpantrymanager.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.R;
import com.example.smartpantrymanager.model.PantryItem;

import java.util.List;

/** Binds the list of pantry items from the database to the RecyclerView rows. */
public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.ViewHolder> {

    /** Lets the Activity react to taps without the adapter knowing about screens. */
    public interface Listener {
        void onEdit(PantryItem item);
        void onDelete(PantryItem item);
    }

    private List<PantryItem> items;
    private final Listener listener;

    public PantryAdapter(List<PantryItem> items, Listener listener) {
        this.items = items;
        this.listener = listener;
    }

    /** Swaps in fresh data after the database changes. */
    public void setItems(List<PantryItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PantryItem item = items.get(position);
        holder.tvName.setText(item.getName());
        holder.tvQuantity.setText(formatQuantity(item.getQuantity()) + " " + item.getUnit());

        String expiry = item.getExpiryDate();
        holder.tvExpiry.setText(expiry == null || expiry.isEmpty()
                ? "No expiry date" : "Expires: " + expiry);

        holder.itemView.setOnClickListener(v -> listener.onEdit(item));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /** Shows 3 instead of 3.0, but keeps 2.5 as 2.5. */
    private String formatQuantity(double q) {
        return (q % 1 == 0) ? String.valueOf((long) q) : String.valueOf(q);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvName, tvQuantity, tvExpiry;
        final ImageButton btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvExpiry = itemView.findViewById(R.id.tvExpiry);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}