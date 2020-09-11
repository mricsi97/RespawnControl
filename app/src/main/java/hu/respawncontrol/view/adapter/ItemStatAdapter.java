package hu.respawncontrol.view.adapter;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import hu.respawncontrol.R;
import hu.respawncontrol.model.room.entity.Item;

public class ItemStatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;

    private SimpleDateFormat timeFormatter = new SimpleDateFormat("ss.SS", Locale.ROOT);

    private List<Item> items;
    private List<Long> averageTimes;
    private List<Long> bestTimes;
    private List<Long> worstTimes;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_ITEM) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_stat_item, parent, false);
            return new ItemStatViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_stat_header, parent, false);
            return new HeaderViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int type = holder.getItemViewType();

        if (type == TYPE_ITEM) {
            Item item = getItem(position);
            Long average = getAverage(position);
            Long best = getBest(position);
            Long worst = getWorst(position);

            // Set image
            Resources res = ((ItemStatViewHolder) holder).itemView.getContext().getResources();
            Drawable image = res.getDrawable(item.getImageResourceId());
            ((ItemStatViewHolder) holder).ivItem.setImageDrawable(image);

            ((ItemStatViewHolder)holder).tvAverage.setText(timeFormatter.format(average));
            ((ItemStatViewHolder)holder).tvBest.setText(timeFormatter.format(best));
            ((ItemStatViewHolder)holder).tvWorst.setText(timeFormatter.format(worst));
        } else if (type == TYPE_HEADER) {
            ((HeaderViewHolder)holder).tvItemHeader.setText("Item");
            ((HeaderViewHolder)holder).tvAverageHeader.setText("Average");
            ((HeaderViewHolder)holder).tvBestHeader.setText("Best");
            ((HeaderViewHolder)holder).tvWorstHeader.setText("Worst");
        }
    }

    private Item getItem(int position) {
        return items.get(position-1);
    }

    private Long getAverage(int position) {
        return averageTimes.get(position-1);
    }

    private Long getBest(int position) {
        return bestTimes.get(position-1);
    }

    private Long getWorst(int position) {
        return worstTimes.get(position-1);
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

//    public void setTimeLists(List<List<Long>> timeLists) {
//        averageTimes = timeLists.get(0);
//        bestTimes = timeLists.get(1);
//        worstTimes = timeLists.get(2);
//        notifyDataSetChanged();
//    }

    public void setTimeLists(List<Long>[] timeLists) {
        averageTimes = timeLists[0];
        bestTimes = timeLists[1];
        worstTimes = timeLists[2];
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public static class ItemStatViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivItem;
        private TextView tvAverage;
        private TextView tvBest;
        private TextView tvWorst;

        public ItemStatViewHolder(@NonNull View itemView) {
            super(itemView);

            ivItem = (ImageView) itemView.findViewById(R.id.ivItem_stats);
            tvAverage = (TextView) itemView.findViewById(R.id.tvAverage);
            tvBest = (TextView) itemView.findViewById(R.id.tvBest);
            tvWorst = (TextView) itemView.findViewById(R.id.tvWorst);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItemHeader;
        private TextView tvAverageHeader;
        private TextView tvBestHeader;
        private TextView tvWorstHeader;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);

            tvItemHeader = (TextView) itemView.findViewById(R.id.tvItemHeader);
            tvAverageHeader = (TextView) itemView.findViewById(R.id.tvAverageHeader);
            tvBestHeader = (TextView) itemView.findViewById(R.id.tvBestHeader);
            tvWorstHeader = (TextView) itemView.findViewById(R.id.tvWorstHeader);
        }
    }
}
