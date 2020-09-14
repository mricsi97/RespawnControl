package hu.respawncontrol.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import hu.respawncontrol.R;
import hu.respawncontrol.model.room.entity.Score;

public class ScoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;

    private boolean isTimeTrial;

    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yy/MM/dd HH:mm", Locale.ROOT);
    private SimpleDateFormat timeTrialTimeFormatter = new SimpleDateFormat("m:ss.SS", Locale.ROOT);
    private SimpleDateFormat endlessTimeFormatter = new SimpleDateFormat("s.S", Locale.ROOT);

    private List<Score> scores;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_ITEM) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.score_item, parent, false);
            return new ScoreViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.score_header, parent, false);
            return new HeaderViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int type = holder.getItemViewType();

        if (type == TYPE_ITEM) {
            Score score = getScore(position);
            Long date = score.getDate();
            Long time = score.getTime();
            int testAmount = score.getTestAmount();

            ((ScoreViewHolder)holder).tvDate.setText("'" + dateFormatter.format(date));
            if(isTimeTrial) {
                ((ScoreViewHolder)holder).tvScore.setText(timeTrialTimeFormatter.format(time));
                ((ScoreViewHolder)holder).tvDifficulty.setText(String.valueOf(testAmount));
            } else {
                ((ScoreViewHolder)holder).tvScore.setText(String.valueOf(testAmount));
                ((ScoreViewHolder)holder).tvDifficulty.setText(endlessTimeFormatter.format(time));
            }
        } else if (type == TYPE_HEADER) {
            ((HeaderViewHolder)holder).tvDateHeader.setText("Date");
            ((HeaderViewHolder)holder).tvScoreHeader.setText("Score");

            if(isTimeTrial) {
                ((HeaderViewHolder)holder).tvDifficultyHeader.setText("Test Amount");
            } else {
                ((HeaderViewHolder)holder).tvDifficultyHeader.setText("Difficulty");
            }
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
        return scores.get(position-1);
    }

    @Override
    public int getItemCount() {
        if(scores == null) {
            return 0;
        }
        return scores.size() + 1;
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

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDateHeader;
        private TextView tvScoreHeader;
        private TextView tvDifficultyHeader;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDateHeader = (TextView) itemView.findViewById(R.id.tvDateHeader_scores);
            tvScoreHeader = (TextView) itemView.findViewById(R.id.tvScoreHeader_scores);
            tvDifficultyHeader = (TextView) itemView.findViewById(R.id.tvDifficultyHeader_scores);
        }
    }
}
