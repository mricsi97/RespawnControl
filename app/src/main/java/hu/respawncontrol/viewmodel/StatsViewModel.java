package hu.respawncontrol.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import hu.respawncontrol.model.Repository;

public class StatsViewModel extends AndroidViewModel {

    private Repository repository;

    public StatsViewModel(@NonNull Application application) {
        super(application);

        repository = new Repository(application);
    }


}
