package hu.respawncontrol.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import hu.respawncontrol.R;
import hu.respawncontrol.data.room.entity.Player;
import hu.respawncontrol.data.room.entity.Result;
import hu.respawncontrol.data.room.helper.PlayerAndBestResult;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardHolder> {
    private List<PlayerAndBestResult> playersAndBestResultsSorted = new ArrayList<>();

    private SimpleDateFormat timeFormatter = new SimpleDateFormat("ss.SS", Locale.ROOT);

    @NonNull
    @Override
    public LeaderboardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.leaderboard_item, parent, false);
        return new LeaderboardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardHolder holder, int position) {
        PlayerAndBestResult playerAndBestResult = playersAndBestResultsSorted.get(position);
        Player player = playerAndBestResult.player;
        Result bestResult = playerAndBestResult.bestResult;

        holder.tvRank.setText(position);
        holder.tvPlayerName.setText(player.getName());
        holder.tvBestTime.setText(timeFormatter.format(bestResult.getSolveTime()));
    }

    @Override
    public int getItemCount() {
        return playersAndBestResultsSorted.size();
    }

    public void setPlayersAndBestResultsSorted(List<PlayerAndBestResult> playersAndBestResultsSorted) {
        this.playersAndBestResultsSorted = playersAndBestResultsSorted;
        notifyDataSetChanged();
    }

    class LeaderboardHolder extends RecyclerView.ViewHolder {
        private TextView tvRank;
        private TextView tvPlayerName;
        private TextView tvBestTime;

        public LeaderboardHolder(@NonNull View itemView) {
            super(itemView);
            tvRank = itemView.findViewById(R.id.tvRank);
            tvPlayerName = itemView.findViewById(R.id.tvPlayerName);
            tvBestTime = itemView.findViewById(R.id.tvBestTime);
        }
    }
}
