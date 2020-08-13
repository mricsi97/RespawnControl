package hu.respawncontrol;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import hu.respawncontrol.adapter.ResultAdapter;
import hu.respawncontrol.data.Item;
import hu.respawncontrol.data.Result;

public class TimeTrialResultDialog extends DialogFragment {

    private static final String TAG = "TimeTrialResultDialog";

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private SimpleDateFormat timeFormatter = new SimpleDateFormat("ss.SS", Locale.ROOT);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_time_trial_result, container, false);

        if(getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        Bundle bundle = getArguments();
        if(bundle == null) {
            Log.e(TAG, "No argument was passed to the dialog fragment.");
            return null;
        }

        final ArrayList<Item> itemsTested = bundle.getParcelableArrayList(TimeTrialActivity.ITEMS_TESTED);
        final long[] solveTimes = bundle.getLongArray(TimeTrialActivity.SOLVE_TIMES);
        if(itemsTested == null || solveTimes == null) {
            Log.e(TAG, "Dialog did not receive valid arguments.");
            return null;
        }

        long[] solveTimeDifferences = new long[solveTimes.length];
        for(int i = 0; i < solveTimes.length; i++) {
            long difference;
            if(i == 0) {
                difference = solveTimes[i];
            } else {
                difference = solveTimes[i] - solveTimes[i-1];
            }
            solveTimeDifferences[i] = difference;
        }

        long solveTimeSum = solveTimes[solveTimes.length-1];
        long averageSolveTime = solveTimeSum / solveTimes.length;

        ArrayList<Result> results = new ArrayList<>();
        for(int i = 0; i < solveTimes.length; i++) {
            Result result = new Result(solveTimeDifferences[i], itemsTested.get(i));
            results.add(result);
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerResult);

        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ResultAdapter(results);
        recyclerView.setAdapter(adapter);

        final TextView tvAverageTime = (TextView) view.findViewById(R.id.tvAverageTime);
        tvAverageTime.setText(timeFormatter.format(averageSolveTime));

        final ImageButton btnReplay = (ImageButton) view.findViewById(R.id.btnReplay_result);
        btnReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TimeTrialActivity)getActivity()).restartTimeTrial();
                dismiss();
            }
        });

        return view;
    }
}
