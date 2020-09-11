package hu.respawncontrol.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

import hu.respawncontrol.R;
import hu.respawncontrol.model.room.entity.Item;
import hu.respawncontrol.view.adapter.ItemStatAdapter;
import hu.respawncontrol.viewmodel.ItemStatsViewModel;

public class ItemStatsFragment extends Fragment {

    private ItemStatsViewModel viewModel;
    private ItemStatAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_stats, container, false);

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerItemStats);
        adapter = new ItemStatAdapter();
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()))
                .get(ItemStatsViewModel.class);

        TabLayout timePeriodTabLayout = view.findViewById(R.id.timePeriodTabLayout_itemStats);
        timePeriodTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                viewModel.getItemsHavingResults(position).observe(getViewLifecycleOwner(),
                        new Observer<List<Item>>() {
                            @Override
                            public void onChanged(List<Item> items) {
                                adapter.setItems(items);
                            }
                        });
                viewModel.getItemStats(position).observe(getViewLifecycleOwner(),
                        new Observer<List<Long>[]>() {
                            @Override
                            public void onChanged(List<Long>[] timeLists) {
                                inflateList(timeLists);
                            }
                        });
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                viewModel.getItemsHavingResults(position).observe(getViewLifecycleOwner(),
                        new Observer<List<Item>>() {
                            @Override
                            public void onChanged(List<Item> items) {
                                adapter.setItems(items);
                            }
                        });
                viewModel.getItemStats(position).observe(getViewLifecycleOwner(),
                        new Observer<List<Long>[]>() {
                            @Override
                            public void onChanged(List<Long>[] timeLists) {
                                inflateList(timeLists);
                            }
                        });
            }
        });

        timePeriodTabLayout.selectTab(timePeriodTabLayout.getTabAt(0));

        return view;
    }

    private void inflateList(List<Long>[] timeLists) {
        adapter.setTimeLists(timeLists);
    }
}
