package hu.respawncontrol.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import hu.respawncontrol.R;
import hu.respawncontrol.model.room.entity.Item;

public class ItemStatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private boolean waitingForItems;
    private boolean waitingForResults;

    private SimpleDateFormat timeFormatter = new SimpleDateFormat("ss.SS", Locale.ROOT);

    private List<Item> items;
    private List<Long> averageTimes;
    private List<Long> bestTimes;
    private List<Long> worstTimes;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_stat_item, parent, false);
        return new ItemStatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Item item = getItem(position);
        Long average = getAverage(position);
        Long best = getBest(position);
        Long worst = getWorst(position);

        // Set image
        Context context = ((ItemStatViewHolder) holder).itemView.getContext();
        Resources res = context.getResources();
        Drawable image = ResourcesCompat.getDrawable(res, res.getIdentifier(item.getImageResourceName(),
                "drawable", context.getPackageName()), null);
        ((ItemStatViewHolder) holder).ivItem.setImageDrawable(image);

        ((ItemStatViewHolder) holder).tvAverage.setText(timeFormatter.format(average));
        ((ItemStatViewHolder) holder).tvBest.setText(timeFormatter.format(best));
        ((ItemStatViewHolder) holder).tvWorst.setText(timeFormatter.format(worst));
    }

    public void setItems(List<Item> items) {
        this.items = items;
        waitingForItems = false;

        if (!waitingForResults) {
            notifyDataSetChanged();
        }
    }

    public void setTimeLists(List<Long>[] timeLists) {
        averageTimes = timeLists[0];
        bestTimes = timeLists[1];
        worstTimes = timeLists[2];
        waitingForResults = false;

        if (!waitingForItems) {
            notifyDataSetChanged();
        }
    }

    public void resetState() {
        waitingForItems = true;
        waitingForResults = true;
    }

    private Item getItem(int position) {
        return items.get(position);
    }

    private Long getAverage(int position) {
        return averageTimes.get(position);
    }

    private Long getBest(int position) {
        return bestTimes.get(position);
    }

    private Long getWorst(int position) {
        return worstTimes.get(position);
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    public static class ItemStatViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivItem;
        private TextView tvAverage;
        private TextView tvBest;
        private TextView tvWorst;

        public ItemStatViewHolder(@NonNull View itemView) {
            super(itemView);

            ivItem = (ImageView) itemView.findViewById(R.id.ivItem_itemStats);
            tvAverage = (TextView) itemView.findViewById(R.id.tvAverage_itemStats);
            tvBest = (TextView) itemView.findViewById(R.id.tvBest_itemStats);
            tvWorst = (TextView) itemView.findViewById(R.id.tvWorst_itemStats);
        }
    }
}
