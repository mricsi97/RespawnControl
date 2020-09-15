package hu.respawncontrol.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import hu.respawncontrol.R;
import hu.respawncontrol.model.room.entity.Score;

public class ScoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private boolean isTimeTrial;

    private SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm ''yy/MM/dd", Locale.ROOT);
    private SimpleDateFormat timeTrialTimeFormatter = new SimpleDateFormat("m:ss.SS", Locale.ROOT);
    private SimpleDateFormat endlessTimeFormatter = new SimpleDateFormat("s.S", Locale.ROOT);

    private List<Score> scores;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.score_item, parent, false);
        return new ScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Score score = getScore(position);
        Long date = score.getDate();
        Long time = score.getTime();
        int testAmount = score.getTestAmount();

        ((ScoreViewHolder) holder).tvDate.setText(dateFormatter.format(date));
        if (isTimeTrial) {
            ((ScoreViewHolder) holder).tvScore.setText(timeTrialTimeFormatter.format(time));
            ((ScoreViewHolder) holder).tvDifficulty.setText(String.valueOf(testAmount));
        } else {
            ((ScoreViewHolder) holder).tvScore.setText(String.valueOf(testAmount));
            ((ScoreViewHolder) holder).tvDifficulty.setText(endlessTimeFormatter.format(time));
        }
    }

    public void setScores(List<Score> scores) {
        this.scores = scores;
        notifyDataSetChanged();
    }

    public void setGameMode(boolean isTimeTrial) {
        this.isTimeTrial = isTimeTrial;
    }

    private Score getScore(int position) {
        return scores.get(position);
    }

    @Override
    public int getItemCount() {
        if (scores == null) {
            return 0;
        }
        return scores.size();
    }

    public static class ScoreViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDate;
        private TextView tvScore;
        private TextView tvDifficulty;

        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDate = (TextView) itemView.findViewById(R.id.tvDate_scores);
            tvScore = (TextView) itemView.findViewById(R.id.tvScore_scores);
            tvDifficulty = (TextView) itemView.findViewById(R.id.tvDifficulty_scores);
        }
    }
}
