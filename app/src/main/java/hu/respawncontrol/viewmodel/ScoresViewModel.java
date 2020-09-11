package hu.respawncontrol.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import hu.respawncontrol.model.Repository;

public class ScoresViewModel extends AndroidViewModel {
    private Repository repository;

    public ScoresViewModel(@NonNull Application application) {
        super(application);

        repository = new Repository(application);
    }
}
