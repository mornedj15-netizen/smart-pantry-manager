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
import android.graphics.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/** Binds the list of pantry items from the database to the RecyclerView rows. */
public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.ViewHolder> {

    /** Lets the Activity react to taps without the adapter knowing about screens. */
    public interface Listener {
        void onEdit(PantryItem item);
        void onDelete(PantryItem item);
    }

    private List<PantryItem> items;
    private final Listener listener;
    private boolean highlightExpiring = true;

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
        holder.tvExpiry.setTextColor(holder.defaultExpiryColor); // reset: rows are recycled

        if (expiry == null || expiry.isEmpty()) {
            holder.tvExpiry.setText("No expiry date");
        } else {
            long days = daysUntil(expiry);
            if (highlightExpiring && days < 0) {
                holder.tvExpiry.setText("EXPIRED: " + expiry);
                holder.tvExpiry.setTextColor(Color.RED);
            } else if (highlightExpiring && days <= 3) {
                holder.tvExpiry.setText("Expires soon: " + expiry);
                holder.tvExpiry.setTextColor(Color.RED);
            } else {
                holder.tvExpiry.setText("Expires: " + expiry);
            }
        }

        holder.itemView.setOnClickListener(v -> listener.onEdit(item));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(item));
    }

    public void setHighlightExpiring(boolean highlight) {
        this.highlightExpiring = highlight;
    }

    /** Days until the expiry date: negative if already expired, 0 if it expires today. */
    private long daysUntil(String expiry) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(expiry);
            if (date == null) return Long.MAX_VALUE;
            long dayMs = 24L * 60 * 60 * 1000;
            // An item is still good until the END of its expiry day, so add one day
            return (long) Math.floor((date.getTime() + dayMs - System.currentTimeMillis()) / (double) dayMs);
        } catch (ParseException e) {
            return Long.MAX_VALUE;
        }
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
        final int defaultExpiryColor;   // new

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvExpiry = itemView.findViewById(R.id.tvExpiry);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            defaultExpiryColor = tvExpiry.getCurrentTextColor();   // new
        }
    }
}