package hu.respawncontrol.view.adapter;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hu.respawncontrol.R;
import hu.respawncontrol.model.room.entity.Item;
import hu.respawncontrol.model.room.entity.ItemGroup;
import hu.respawncontrol.model.room.helper.ItemGroupWithItems;

public class ItemGroupAdapter extends RecyclerView.Adapter<ItemGroupAdapter.ItemGroupViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener onItemClickListener;

    private List<ItemGroupWithItems> itemGroups;

    @NonNull
    @Override
    public ItemGroupAdapter.ItemGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemgroup_item, parent, false);

        return new ItemGroupViewHolder(rowView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemGroupViewHolder holder, int position) {
        ItemGroupWithItems itemGroupWithItems = itemGroups.get(position);
        ItemGroup itemGroup = itemGroupWithItems.itemGroup;
        List<Item> items = itemGroupWithItems.items;

        if(!itemGroup.isEditable()) {
            holder.lockImage.setVisibility(View.VISIBLE);
        }

        holder.tvName.setText(itemGroup.getItemGroupName());

        // Display item images
        Resources res = holder.itemView.getResources();
        for(Item item : items) {
            ImageView imageView = (ImageView) LayoutInflater.from(holder.itemView.getContext())
                    .inflate(R.layout.item_icon, holder.imageHolderLayout, false);
            Drawable image = ResourcesCompat.getDrawable(res, res.getIdentifier(item.getImageResourceName(),
                    "drawable", holder.itemView.getContext().getPackageName()), null);
            imageView.setImageDrawable(image);
            holder.imageHolderLayout.addView(imageView);
        }
    }

    public void setItemGroups(List<ItemGroupWithItems> itemGroups) {
        this.itemGroups = itemGroups;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        if(itemGroups == null) {
            return 0;
        }
        return itemGroups.size();
    }

    public static class ItemGroupViewHolder extends RecyclerView.ViewHolder {
        private ImageView lockImage;
        private TextView tvName;
        private LinearLayout imageHolderLayout;

        public ItemGroupViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            lockImage = itemView.findViewById(R.id.lockImage);
            tvName = itemView.findViewById(R.id.tvItemGroupName_itemgroups);
            imageHolderLayout = itemView.findViewById(R.id.imageHolderLayout_itemgroups);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
