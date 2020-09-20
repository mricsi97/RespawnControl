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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import hu.respawncontrol.R;
import hu.respawncontrol.model.room.helper.ItemGroupWithItems;
import hu.respawncontrol.view.adapter.ItemGroupAdapter;
import hu.respawncontrol.viewmodel.ItemGroupsViewModel;

public class ItemGroupsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_itemgroups, container, false);

        final RecyclerView recyclerItemGroup = view.findViewById(R.id.recyclerItemGroup_itemgroups);
        recyclerItemGroup.setHasFixedSize(true);
        final ItemGroupAdapter adapter = new ItemGroupAdapter();
        recyclerItemGroup.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerItemGroup.setLayoutManager(layoutManager);

        final ItemGroupsViewModel viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()))
                .get(ItemGroupsViewModel.class);

        viewModel.getAllItemGroupsWithItems().observe(getViewLifecycleOwner(),
                new Observer<List<ItemGroupWithItems>>() {
                    @Override
                    public void onChanged(List<ItemGroupWithItems> itemGroupWithItems) {
                        adapter.setItemGroups(itemGroupWithItems);
                    }
                });

        adapter.setOnItemClickListener(new ItemGroupAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // TODO: edit group
            }
        });

        final FloatingActionButton fab = view.findViewById(R.id.btnAddItemGroup);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new AddItemGroupFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}
