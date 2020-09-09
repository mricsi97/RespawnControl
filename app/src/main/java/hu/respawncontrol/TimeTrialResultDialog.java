package hu.respawncontrol;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import hu.respawncontrol.adapter.ResultAdapter;
import hu.respawncontrol.data.TimeTrialViewModel;
import hu.respawncontrol.data.room.entity.Item;
import hu.respawncontrol.data.room.entity.Result;

public class TimeTrialResultDialog extends DialogFragment {

    private static final String TAG = "TimeTrialResultDialog";

    private SimpleDateFormat timeFormatter = new SimpleDateFormat("ss.SS", Locale.ROOT);


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_time_trial_result, container, false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerResult);
        final ResultAdapter resultAdapter = new ResultAdapter();
        recyclerView.setAdapter(resultAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        final TextView tvAverageTime = (TextView) view.findViewById(R.id.tvAverageTime);

        TimeTrialViewModel viewModel = new ViewModelProvider(getActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(TimeTrialViewModel.class);

        viewModel.calculateSolveTimeDifferences();
        viewModel.saveScore();
        viewModel.saveResults();

        viewModel.getResults().observe(this,
                new Observer<List<Result>>() {
                    @Override
                    public void onChanged(List<Result> results) {
                        resultAdapter.setResults((ArrayList<Result>) results);
                    }
                });

        viewModel.getTestItems().observe(this,
                new Observer<List<Item>>() {
                    @Override
                    public void onChanged(List<Item> items) {
                        resultAdapter.setTestedItems((ArrayList<Item>) items);
                    }
                });

        viewModel.getAverageSolveTime().observe(this,
                new Observer<Long>() {
                    @Override
                    public void onChanged(Long aLong) {
                        tvAverageTime.setText(timeFormatter.format(aLong));
                    }
                });

        final ImageButton btnReplay = (ImageButton) view.findViewById(R.id.btnReplay_result);
        btnReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TimeTrialActivity) getActivity()).restartTimeTrial(false);
                dismiss();
            }
        });

        return view;
    }
}
