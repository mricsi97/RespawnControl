package hu.respawncontrol.adapter;

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
import java.util.ArrayList;
import java.util.Locale;

import hu.respawncontrol.R;
import hu.respawncontrol.data.room.entity.Item;
import hu.respawncontrol.data.room.entity.Result;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {

    private ArrayList<Result> results;
    private ArrayList<Item> testedItems;

    private SimpleDateFormat timeFormatter = new SimpleDateFormat("ss.SS", Locale.ROOT);

    public void setResults(ArrayList<Result> results) {
        this.results = results;
    }

    public void setTestedItems(ArrayList<Item> testedItems) {
        this.testedItems = testedItems;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.result_item, parent, false);

        return new ResultViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        Result result = results.get(position);
        Long solveTime = result.getSolveTime();
        Item item = testedItems.get(position);

        holder.tvTestNumber.setText((position+1) + ".");
        holder.tvSolveTime.setText(timeFormatter.format(solveTime));

        Resources res = holder.itemView.getContext().getResources();
        Drawable image = res.getDrawable(item.getImageResourceId());
        holder.ivItem.setImageDrawable(image);

        holder.tvItemName.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public static class ResultViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTestNumber;
        private TextView tvSolveTime;
        private ImageView ivItem;
        private TextView tvItemName;

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTestNumber = (TextView) itemView.findViewById(R.id.tvTestNumber);
            tvSolveTime = (TextView) itemView.findViewById(R.id.tvSolveTime);
            ivItem = (ImageView) itemView.findViewById(R.id.ivItem_result);
            tvItemName = (TextView) itemView.findViewById(R.id.tvItemName);
        }
    }
}
