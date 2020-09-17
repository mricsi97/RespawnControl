package hu.respawncontrol.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import hu.respawncontrol.model.Repository;
import hu.respawncontrol.model.room.helper.ItemGroupWithItems;

public class ItemGroupsViewModel extends AndroidViewModel {
    private Repository repository;

    private LiveData<List<ItemGroupWithItems>> itemGroupsWithItems;

    public ItemGroupsViewModel(@NonNull Application application) {
        super(application);

        repository = new Repository(application);
    }

    public LiveData<List<ItemGroupWithItems>> getAllItemGroupsWithItems() {
        if(itemGroupsWithItems == null) {
            itemGroupsWithItems = repository.getAllItemGroupsWithItems();
        }
        return itemGroupsWithItems;
    }
}
